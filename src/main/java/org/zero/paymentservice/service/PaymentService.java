package org.zero.paymentservice.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zero.paymentservice.entity.History;
import org.zero.paymentservice.entity.Transaction;
import org.zero.paymentservice.exception.RequestException;
import org.zero.paymentservice.mapper.CheckoutMapper;
import org.zero.paymentservice.mapper.PaymentMapper;
import org.zero.paymentservice.model.Checkout;
import org.zero.paymentservice.model.OrderStatus;
import org.zero.paymentservice.model.Pay;
import org.zero.paymentservice.model.liqPay.LiqPayCheckout;
import org.zero.paymentservice.model.liqPay.LiqPayComplete;
import org.zero.paymentservice.model.liqPay.LiqPaySignature;
import org.zero.paymentservice.model.liqPay.LiqPayStatus;
import org.zero.paymentservice.model.liqPay.LiqPayVerify;
import org.zero.paymentservice.proxy.PaymentKafkaProducer;
import org.zero.paymentservice.repository.HistoryRepository;
import org.zero.paymentservice.repository.TransactionRepository;

@Service
@RequiredArgsConstructor
public class PaymentService {
  private final Logger logger = LoggerFactory.getLogger(PaymentService.class);
  private final TransactionRepository transactionRepository;
  private final PaymentKafkaProducer paymentKafkaProducer;
  private final HistoryRepository historyRepository;
  private final LiqPayService liqPayService;

  @Transactional
  public Transaction createPaymentTransaction(Checkout checkout) {
    Transaction createdTransaction = saveTransaction(checkout);
    saveTransactionStatusRow(createdTransaction);

    return createdTransaction;
  }

  public LiqPaySignature getPaymentCheckout(String orderId) {
    Optional<Transaction> transaction = transactionRepository.findFirstByOrderId(orderId);
    if (transaction.isEmpty()) throw new RequestException("Payment transaction not found");

    return this.getPaymentCheckout(transaction.get());
  }

  public LiqPaySignature getPaymentCheckout(Transaction transaction) {
    LiqPayCheckout liqPayCheckout = CheckoutMapper.map(transaction);
    LiqPaySignature responseBody = liqPayService.getPaymentCheckout(liqPayCheckout);

    if (responseBody == null) throw new RequestException("Error creating checkout");
    return responseBody;
  }

  public void completePayment(Pay pay) {
    Optional<Transaction> transaction = transactionRepository.findFirstByOrderId(pay.getOrderId());
    if (transaction.isEmpty()) throw new RequestException("Payment transaction not found");

    Optional<OrderStatus> currentStatus =
        historyRepository.getPaymentStatus(transaction.get().getId());
    if (isPresentAndSuccess(currentStatus)) throw new RequestException("Payment already completed");

    LiqPayComplete body = PaymentMapper.map(pay);
    logger.info(liqPayService.completePayment(body).toString());
  }

  public void updatePaymentStatus(LiqPaySignature data) {
    LiqPayVerify result = liqPayService.verifyCallback(data);
    if (!result.verifyResult()) throw new RequestException("LiqPay verify failed");

    Optional<Transaction> transaction =
        transactionRepository.findFirstByOrderId(result.data().getOrder_id());
    if (transaction.isEmpty()) throw new RequestException("LiqPay transaction not found");

    saveTransactionStatusRow(result, transaction);
    paymentKafkaProducer.sendPaymentHoldEvent(
        transaction.get().getOrderId(), Double.valueOf(result.data().getAmount()));
  }

  private void saveTransactionStatusRow(LiqPayVerify result, Optional<Transaction> transaction) {
    String status = LiqPayStatus.getStatusByCode(result.data().getStatus());
    transaction.ifPresent(value -> this.saveTransactionStatusRow(status, value));
  }

  private static boolean isPresentAndSuccess(Optional<OrderStatus> currentStatus) {
    return currentStatus.isPresent() && currentStatus.get().getStatus().equals("SUCCESS");
  }

  private void saveTransactionStatusRow(Transaction createdTransaction) {
    this.saveTransactionStatusRow("DEFINED", createdTransaction);
  }

  private void saveTransactionStatusRow(String status, Transaction transaction) {
    History currentStatus = new History();
    currentStatus.setStatus(status);
    currentStatus.setTransaction(transaction);
    historyRepository.save(currentStatus);
  }

  private Transaction saveTransaction(Checkout checkout) {
    Transaction transaction = CheckoutMapper.map(checkout);
    return transactionRepository.save(transaction);
  }
}

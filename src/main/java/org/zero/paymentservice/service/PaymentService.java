package org.zero.paymentservice.service;

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
import org.zero.paymentservice.model.liqPay.*;
import org.zero.paymentservice.repository.HistoryRepository;
import org.zero.paymentservice.repository.TransactionRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private final TransactionRepository transactionRepository;
    private final HistoryRepository historyRepository;
    private final LiqPayService liqPayService;

    @Transactional
    public Transaction createPaymentTransaction(Checkout checkout) {
        var createdTransaction = createTransaction(checkout);
        saveTransactionStatusRow(createdTransaction);

        return createdTransaction;
    }

    public LiqPaySignature getPaymentCheckout(String orderId) {
        var transaction = transactionRepository.findByOrderId(orderId);
        if (transaction.isEmpty()) throw new RequestException("Payment transaction not found");

        return this.getPaymentCheckout(transaction.get());
    }

    public LiqPaySignature getPaymentCheckout(Transaction transaction) {
        LiqPayCheckout liqPayCheckout = CheckoutMapper.map(transaction);
        var responseBody = liqPayService.getPaymentCheckout(liqPayCheckout);
        if (responseBody == null) throw new RequestException("Error creating checkout");
        return responseBody;
    }

    public void completePayment(Pay pay) {
        var transaction = transactionRepository.findByOrderId(pay.getOrderId());
        if (transaction.isEmpty()) throw new RequestException("Payment transaction not found");

        var currentStatus = historyRepository.getPaymentStatus(transaction.get().getId());
        if (isPresentAndSuccess(currentStatus)) throw new RequestException("Payment already completed");

        var body = PaymentMapper.map(pay);
        logger.info(liqPayService.completePayment(body).toString());
    }

    public void updatePaymentStatus(LiqPaySignature data) {
        var result = liqPayService.verifyCallback(data);
        if (!result.verifyResult()) throw new RequestException("LiqPay verify failed");

        var transaction = transactionRepository.findByOrderId(result.data().getOrder_id());
        if (transaction.isEmpty()) throw new RequestException("LiqPay transaction not found");
        saveTransactionStatusRow(result, transaction);
    }




    private void saveTransactionStatusRow(LiqPayVerify result, Optional<Transaction> transaction) {
        var status = LiqPayStatus.getStatusByCode(result.data().getStatus());
        transaction.ifPresent(value -> this.saveTransactionStatusRow(status, value));
    }

    private static boolean isPresentAndSuccess(Optional<OrderStatus> currentStatus) {
        return currentStatus.isPresent() && currentStatus.get().getStatus().equals("SUCCESS");
    }

    private void saveTransactionStatusRow(Transaction createdTransaction) {
        this.saveTransactionStatusRow("DEFINED", createdTransaction);
    }

    private void saveTransactionStatusRow(String status, Transaction transaction) {
        var currentStatus = new History();
        currentStatus.setStatus(status);
        currentStatus.setTransaction(transaction);
        historyRepository.save(currentStatus);
    }

    private Transaction createTransaction(Checkout checkout) {
        var transaction = new Transaction();
        transaction.setAmount(checkout.amount());
        transaction.setOrderId(checkout.orderId());
        transaction.setRecipientId(checkout.recipientId());
        transaction.setPayerId(checkout.payerId());
        return transactionRepository.save(transaction);
    }
}

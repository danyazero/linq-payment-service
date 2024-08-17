package org.zero.paymentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zero.paymentservice.entity.History;
import org.zero.paymentservice.entity.Transaction;
import org.zero.paymentservice.exception.RequestException;
import org.zero.paymentservice.model.Checkout;
import org.zero.paymentservice.model.Pay;
import org.zero.paymentservice.model.TransactionInfo;
import org.zero.paymentservice.model.liqPay.LiqPayCheckout;
import org.zero.paymentservice.model.liqPay.LiqPayComplete;
import org.zero.paymentservice.model.liqPay.LiqPaySignature;
import org.zero.paymentservice.model.liqPay.LiqPayStatus;
import org.zero.paymentservice.repository.HistoryRepository;
import org.zero.paymentservice.repository.TransactionRepository;
import org.zero.paymentservice.utils.SignatureVerification;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final TransactionRepository transactionRepository;
    private final HistoryRepository historyRepository;
    private final LiqPayService liqPayService;

    @Transactional
    public void createPaymentTransaction(Checkout checkout) {

        var transaction = new Transaction();
        transaction.setAmount(checkout.amount());
        transaction.setOrderId(checkout.orderId());
        transaction.setRecipientId(checkout.recipientId());
        transaction.setPayerId(checkout.payerId());
        var createdTransaction = transactionRepository.save(transaction);

        var currentStatus = new History();
        currentStatus.setStatus("DEFINED");
        currentStatus.setTransaction(createdTransaction);
        currentStatus = historyRepository.save(currentStatus);
    }

    public LiqPaySignature getPaymentCheckout(String orderId) {
        var transaction = transactionRepository.findByOrderId(orderId);
        if (transaction.isEmpty()) throw new RequestException("Payment transaction not found");

        LiqPayCheckout liqPayCheckout = new LiqPayCheckout(transaction.get().getAmount().toString(), "UAH", "Замовлення на сервісі linq", orderId);
        var responseBody = liqPayService.getPaymentCheckout(liqPayCheckout);
        if (responseBody == null) throw new RequestException("Error creating checkout");
        return responseBody;
    }

    public void completePayment(Pay pay) {
        var transaction = transactionRepository.findByOrderId(pay.getOrderId());
        if (transaction.isEmpty()) throw new RequestException("Payment transaction not found");
        var currentStatus = historyRepository.getPaymentStatus(transaction.get().getId());
        if (currentStatus.isPresent() && currentStatus.get().getStatus().equals("SUCCESS")) throw new RequestException("Payment already completed");
        var paymentResult = liqPayService.completePayment(new LiqPayComplete(String.valueOf(pay.getAmount()), pay.getOrderId()));
    }

    public void updatePaymentStatus(LiqPaySignature data) {
        var result = liqPayService.verifyCallback(data);
        if (!result.verifyResult()) throw new RequestException("LiqPay verify failed");

        var transaction = transactionRepository.findByOrderId(result.data().getOrder_id());
        if (transaction.isEmpty()) throw new RequestException("LiqPay transaction not found");
        var newStatus = new History();
        newStatus.setStatus(LiqPayStatus.getStatusByCode(result.data().getStatus()));
        newStatus.setTransaction(transaction.get());
        newStatus = historyRepository.save(newStatus);
    }
}

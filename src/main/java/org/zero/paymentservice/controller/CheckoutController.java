package org.zero.paymentservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.zero.paymentservice.entity.History;
import org.zero.paymentservice.entity.Transaction;
import org.zero.paymentservice.exception.RequestException;
import org.zero.paymentservice.model.Checkout;
import org.zero.paymentservice.model.Pay;
import org.zero.paymentservice.model.liqPay.LiqPayCheckout;
import org.zero.paymentservice.model.liqPay.LiqPayComplete;
import org.zero.paymentservice.model.liqPay.LiqPayCurrentStatus;
import org.zero.paymentservice.model.liqPay.LiqPaySignature;
import org.zero.paymentservice.repository.HistoryRepository;
import org.zero.paymentservice.repository.TransactionRepository;
import org.zero.paymentservice.service.LiqPayService;
import org.zero.paymentservice.service.PaymentService;
import org.zero.paymentservice.utils.SignatureVerification;
import org.zero.paymentservice.utils.URIDecoder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "${CORS.FRONT_URL}")
@RequestMapping("/checkout")
public class CheckoutController {
    private final SignatureVerification signatureVerification;
    private final PaymentService paymentService;

//    @PutMapping
//    public LiqPaySignature getCheckout(@RequestBody Checkout checkout, HttpServletRequest request) {
//        signatureVerification.verify(request, checkout);
//
//    }

    @GetMapping
    public LiqPaySignature getCheckout(@RequestParam String orderId) {
        return paymentService.getPaymentCheckout(orderId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void updatePaymentStatus(@RequestBody String data, HttpServletRequest request) {
        System.out.println("started");

        var parsedData = URIDecoder.apply(data, LiqPaySignature.class);
        System.out.println(parsedData);
        paymentService.updatePaymentStatus(parsedData);
    }

    @PatchMapping
    @SneakyThrows
    public void completePayment(@RequestBody Pay pay, HttpServletRequest request) {
        signatureVerification.verify(request, pay);
        paymentService.completePayment(new Pay(pay.getOrderId(), pay.getAmount()));
    }

}

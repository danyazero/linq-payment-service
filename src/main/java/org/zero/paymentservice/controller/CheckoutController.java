package org.zero.paymentservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.zero.paymentservice.model.Checkout;
import org.zero.paymentservice.model.Order;
import org.zero.paymentservice.model.Pay;
import org.zero.paymentservice.model.liqPay.LiqPaySignature;
import org.zero.paymentservice.service.PaymentService;
import org.zero.paymentservice.utils.DeliveryPriceProvider;
import org.zero.paymentservice.utils.SignatureVerification;
import org.zero.paymentservice.utils.URIDecoder;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "${CORS.FRONT_URL}")
@RequestMapping("/api/v1/checkout")
public class CheckoutController {
    private final PaymentService paymentService;

    @PutMapping
    public LiqPaySignature createPaymentAndGetCheckout(@RequestBody Order order) {
        var deliveryPrice = DeliveryPriceProvider.provide(order);
        var checkout = new Checkout(
                order.getOrderId(),
                order.getSellerUserId(),
                order.getRecipientUserId(),
                order.getCartPrice() + deliveryPrice
        );
        var createdTransaction = paymentService.createPaymentTransaction(checkout);
        return paymentService.getPaymentCheckout(createdTransaction);
    }

    @GetMapping
    public LiqPaySignature getCheckout(@RequestParam String orderId) {
        return paymentService.getPaymentCheckout(orderId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void updatePaymentStatus(@RequestBody String data, HttpServletRequest request) {
        var parsedData = URIDecoder.apply(data, LiqPaySignature.class);
        paymentService.updatePaymentStatus(parsedData);
    }

    @PatchMapping
    @SneakyThrows
    public void completePayment(@RequestBody Pay pay, HttpServletRequest request) {
        paymentService.completePayment(new Pay(pay.getOrderId(), pay.getAmount()));
    }

}

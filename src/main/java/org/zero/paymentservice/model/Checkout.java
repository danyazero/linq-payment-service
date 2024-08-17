package org.zero.paymentservice.model;

public record Checkout(
        String orderId,
        String recipientId,
        String payerId,
        Double amount
){}
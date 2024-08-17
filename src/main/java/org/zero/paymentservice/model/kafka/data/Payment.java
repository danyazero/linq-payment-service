package org.zero.paymentservice.model.kafka.data;

public record Payment(
        String orderId,
        Double amount
) {}

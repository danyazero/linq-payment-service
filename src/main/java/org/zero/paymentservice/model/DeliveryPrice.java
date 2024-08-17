package org.zero.paymentservice.model;

public record DeliveryPrice(
        String sender,
        String recipient,
        Double price,
        Integer parcelType
) {
}

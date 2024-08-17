package org.zero.paymentservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.zero.paymentservice.model.Checkout;
import org.zero.paymentservice.model.kafka.Event;
import org.zero.paymentservice.model.kafka.OrderEvent;
import org.zero.paymentservice.service.PaymentService;
import org.zero.paymentservice.utils.DeliveryPriceProvider;

@Component
@RequiredArgsConstructor
public class OrderKafkaConsumer {
    private final PaymentService paymentService;

//    @KafkaListener(topics = "orders", groupId = "payment-service", containerFactory = "orderListenerContainerFactory")
//    public void listen(OrderEvent event) {
//        System.out.println(event.toString());
//
//        Double deliveryPrice = DeliveryPriceProvider.provide(event.getEventData());
//        System.out.println("Delivery price: "+deliveryPrice);
//        var checkout = new Checkout(
//                event.getEventData().getOrderId(),
//                event.getEventData().getSellerUserId(),
//                event.getEventData().getRecipientUserId(),
//                event.getEventData().getCartPrice() + deliveryPrice
//        );
//
//
//        paymentService.createPaymentTransaction(checkout);
//    }



}

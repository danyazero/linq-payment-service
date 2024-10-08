package org.zero.paymentservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.zero.paymentservice.model.kafka.DeliveryEvent;
import org.zero.paymentservice.service.PaymentService;

@Component
@RequiredArgsConstructor
public class OrderKafkaConsumer {
  private final PaymentService paymentService;

  @KafkaListener(
      topics = "orders",
      groupId = "payment-service",
      containerFactory = "deliveryListenerContainerFactory")
  public void listen(DeliveryEvent event) {
    System.out.println(event.toString());
  }
}

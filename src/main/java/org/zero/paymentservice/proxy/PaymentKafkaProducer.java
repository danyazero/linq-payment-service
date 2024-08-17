package org.zero.paymentservice.proxy;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.zero.paymentservice.model.kafka.Event;
import org.zero.paymentservice.model.kafka.PaymentEvent;
import org.zero.paymentservice.model.kafka.data.Payment;

@Component
@RequiredArgsConstructor
public class PaymentKafkaProducer {
    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public void sendPaymentHoldEvent(String orderId) {
        var event = new PaymentEvent("PaymentHold", 1, new Payment(orderId));
        System.out.println("called");
        kafkaTemplate.send("payments", event);
    }
}

package org.zero.paymentservice.model.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.zero.paymentservice.model.kafka.data.Payment;

@Setter
@Getter
@AllArgsConstructor
public class PaymentEvent extends Event {
    private Payment eventData;

    public PaymentEvent(String eventType, Integer eventVersion, Payment eventData) {
        super(eventType, eventVersion);
        this.eventData = eventData;
    }
}

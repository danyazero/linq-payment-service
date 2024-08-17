package org.zero.paymentservice.model.kafka;

import lombok.Getter;
import lombok.Setter;
import org.zero.paymentservice.model.kafka.data.Order;

@Getter
@Setter
public class OrderEvent extends Event {
    private Order eventData;
}


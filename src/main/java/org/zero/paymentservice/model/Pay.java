package org.zero.paymentservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Pay {
    private String orderId;
    private Double amount;
}

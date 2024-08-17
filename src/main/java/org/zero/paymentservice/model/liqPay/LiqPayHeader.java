package org.zero.paymentservice.model.liqPay;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LiqPayHeader {
    private Integer version;
    private LiqPayAction action;
    private String public_key;
}

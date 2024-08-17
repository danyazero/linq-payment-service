package org.zero.paymentservice.model.liqPay;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class LiqPayComplete extends LiqPayHeader {
    private final String amount;
    private final String order_id;
}

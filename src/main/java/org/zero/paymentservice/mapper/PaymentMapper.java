package org.zero.paymentservice.mapper;

import org.zero.paymentservice.model.Pay;
import org.zero.paymentservice.model.liqPay.LiqPayComplete;

public class PaymentMapper {
    public static LiqPayComplete map(Pay pay) {
        return new LiqPayComplete(String.valueOf(pay.getAmount()), pay.getOrderId());
    }
}

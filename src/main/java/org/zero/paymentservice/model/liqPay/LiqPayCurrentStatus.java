package org.zero.paymentservice.model.liqPay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class LiqPayCurrentStatus extends LiqPayHeader {
    @JsonProperty("order_id")
    private final String orderId;
}

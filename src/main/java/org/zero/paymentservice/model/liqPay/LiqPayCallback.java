package org.zero.paymentservice.model.liqPay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LiqPayCallback extends LiqPayHeader {
    private String amount;
    private String order_id;
    private String status;
}

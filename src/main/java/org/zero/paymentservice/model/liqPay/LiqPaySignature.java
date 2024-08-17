package org.zero.paymentservice.model.liqPay;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LiqPaySignature {
        private String data;
        private String signature;

    public String data() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String signature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}

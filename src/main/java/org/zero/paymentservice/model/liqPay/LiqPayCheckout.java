package org.zero.paymentservice.model.liqPay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class LiqPayCheckout extends LiqPayHeader {
  private final String amount;
  private final String currency;
  private final String description;
  private final String order_id;

  @JsonProperty("server_url")
  private String serverURI;
}

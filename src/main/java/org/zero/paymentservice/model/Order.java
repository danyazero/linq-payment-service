package org.zero.paymentservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {
    private String orderId;
    private String sellerUserId;
    private String recipientUserId;
    private String sellerWarehouseId;
    private String recipientWarehouseId;
    private String deliveryDescription;
    private String orderDescription;
    private Double cartPrice;
    private Double deliveryPrice;
    private Integer parcelType;
}

package org.zero.paymentservice.model;

import org.zero.paymentservice.entity.History;

/**
 * Projection for {@link org.zero.paymentservice.entity.Transaction}
 */
public interface TransactionInfo {
    Integer getId();

    String getOrderId();

    Double getAmount();

    History getCurrentStatus();
}
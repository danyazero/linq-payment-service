package org.zero.paymentservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "order_id", nullable = false, length = Integer.MAX_VALUE, unique = true)
    private String orderId;

    @Column(name = "recipient_id", nullable = false, length = Integer.MAX_VALUE)
    private String recipientId;

    @Column(name = "payer_id", nullable = false, length = Integer.MAX_VALUE)
    private String payerId;

    @Column(name = "amount", nullable = false)
    private Double amount;


}
package org.zero.paymentservice.mapper;

import org.zero.paymentservice.entity.Transaction;
import org.zero.paymentservice.model.Checkout;
import org.zero.paymentservice.model.Order;
import org.zero.paymentservice.model.liqPay.LiqPayCheckout;

public class CheckoutMapper {
    public static LiqPayCheckout map(Transaction transaction) {
        return new LiqPayCheckout(
                transaction.getAmount().toString(),
                "UAH",
                "Замовлення на сервісі linq",
                transaction.getOrderId());
    }

    public static Checkout map(Order order, Double deliveryPrice) {
        return new Checkout(
                order.getOrderId(),
                order.getSellerUserId(),
                order.getRecipientUserId(),
                order.getCartPrice() + deliveryPrice);
    }

    public static Transaction map(Checkout checkout) {
        var transaction = new Transaction();
        transaction.setAmount(checkout.amount());
        transaction.setOrderId(checkout.orderId());
        transaction.setRecipientId(checkout.recipientId());
        transaction.setPayerId(checkout.payerId());
        return transaction;
    }
}

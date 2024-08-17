package org.zero.paymentservice.utils;

public class IdempotencyValueProvider {

    public static String generate(String... values) {
        return String.join(".", values);
    }
}

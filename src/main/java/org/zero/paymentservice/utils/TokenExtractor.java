package org.zero.paymentservice.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public class TokenExtractor {
    public static Optional<String> extract(HttpServletRequest request) {
        var header = request.getHeader("Signature");
        if (header != null) {
            return Optional.of(header);
        }
        return Optional.empty();
    }
}

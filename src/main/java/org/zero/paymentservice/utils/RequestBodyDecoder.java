package org.zero.paymentservice.utils;

import lombok.SneakyThrows;

public class RequestBodyDecoder {

    @SneakyThrows
    public static String compile(Object object, String secretToken) {
        var text = Serializer.apply(object);
        System.out.println(text);
        var encodedData = Base64Encoder.apply(text);
        var rawToken = secretToken + encodedData + secretToken;
        return SHAEncoder.apply(rawToken);
    }
}

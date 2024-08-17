package org.zero.paymentservice.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zero.paymentservice.exception.RequestException;
import org.zero.paymentservice.model.liqPay.*;
import org.zero.paymentservice.repository.HistoryRepository;
import org.zero.paymentservice.repository.TransactionRepository;
import org.zero.paymentservice.utils.*;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@RequiredArgsConstructor
public class LiqPayService {
    private final HttpClient client = HttpClient.newBuilder().build();

    @Value("${liqpay.server.url}")
    private String serverLink;
    @Value("${liqpay.privateKey}")
    private String privateKey;
    @Value("${liqpay.publicKey}")
    private String publicKey;
    @Value("${liqpay.apiVersion}")
    private String apiVersion;

    public LiqPaySignature getPaymentCheckout(LiqPayCheckout object) {
        fillHeader(object, LiqPayAction.hold);
        String serializedObject = Serializer.apply(object);
        return getLiqPaySignature(Base64Encoder.apply(serializedObject));
    }

    public LiqPaySignature getPaymentStatus(LiqPayCurrentStatus object) {
        fillHeader(object, LiqPayAction.status);
        String serializedObject = Serializer.apply(object);
        System.out.println(serializedObject);
        return getLiqPaySignature(Base64Encoder.apply(serializedObject));
    }

    public LiqPayCallback completePayment(LiqPayComplete object) {
        var result = this.getCompletePaymentRequestBody(object);
        var uri = URI.create(serverLink);
        var requestBody = URIEncoder.apply(result);
        HttpRequest requestTemplate = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(requestTemplate, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RequestException("Error sending complete payment request");
        }
        return Deserializer.apply(response.body(), LiqPayCallback.class);
    }

    private LiqPaySignature getCompletePaymentRequestBody(LiqPayComplete object) {
        fillHeader(object, LiqPayAction.hold_completion);
        String serializedObject = Serializer.apply(object);
        return getLiqPaySignature(Base64Encoder.apply(serializedObject));
    }

    private <T extends LiqPayHeader> void fillHeader(T object, LiqPayAction action) {
        object.setAction(action);
        object.setVersion(Integer.parseInt(apiVersion));
        object.setPublic_key(publicKey);
    }

    public LiqPayVerify verifyCallback(LiqPaySignature signature) {
        String data = null;
        data = tryDecodeBase64(signature);
        System.out.println(data);
        LiqPayCallback serialized = Deserializer.apply(data, LiqPayCallback.class);
        var check = getLiqPaySignature(signature.data());
        System.out.println(check);
        return new LiqPayVerify(
                serialized,
                check.signature().equals(signature.signature())
        );
    }

    private static String tryDecodeBase64(LiqPaySignature signature) {
        try {
            return Base64Decoder.apply(signature.data());
        } catch (Exception e) {
            throw new RequestException("Base64 decoding exception");
        }
    }

    private LiqPaySignature getLiqPaySignature(String encodedData) {
        var signature = SHAEncoder.apply(privateKey + encodedData + privateKey, SHAEncoder.Encryption.SHA1);
        return new LiqPaySignature(
                encodedData,
                signature
        );
    }
}

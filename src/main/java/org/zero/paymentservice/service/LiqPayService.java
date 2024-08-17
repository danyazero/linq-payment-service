package org.zero.paymentservice.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.zero.paymentservice.exception.RequestException;
import org.zero.paymentservice.model.DeliveryPrice;
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
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LiqPayService {
    Logger logger = LoggerFactory.getLogger(LiqPayService.class);

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

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
        var response = new RestTemplate().exchange(uri, HttpMethod.POST, entity, LiqPayCallback.class);

        if (response.getStatusCode() != HttpStatusCode.valueOf(200))
            throw new RequestException("Error sending complete payment request");

        return response.getBody();
    }

    public LiqPayVerify verifyCallback(LiqPaySignature signature) {
        String data = tryDecodeBase64(signature);
        logger.info(data);
        LiqPayCallback serialized = Deserializer.apply(data, LiqPayCallback.class);
        var check = getLiqPaySignature(signature.data());
        return new LiqPayVerify(
                serialized,
                check.signature().equals(signature.signature())
        );
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

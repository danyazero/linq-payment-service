package org.zero.paymentservice.utils;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.zero.paymentservice.exception.RequestException;
import org.zero.paymentservice.model.DeliveryPrice;
import org.zero.paymentservice.model.Order;

public class DeliveryPriceProvider {

    public static Double provide(Order order) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8084/document")
                .queryParam("sender", Base64Encoder.apply(order.getSellerWarehouseId()))
                .queryParam("recipient", Base64Encoder.apply(order.getRecipientWarehouseId()))
                .queryParam("price", order.getCartPrice())
                .queryParam("parcelType", order.getParcelType());

        HttpEntity<DeliveryPrice> entity = new HttpEntity<>(headers);

        var response = restTemplate.exchange(builder.toUriString(),
                HttpMethod.GET,
                entity, Double.class);

        if (response.getStatusCode() != HttpStatusCode.valueOf(200))
            throw new RequestException("Error delivery price calculate");
        return response.getBody();
    }
}

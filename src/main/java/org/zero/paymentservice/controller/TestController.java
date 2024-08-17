package org.zero.paymentservice.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
import org.zero.paymentservice.model.URLParameter;
import org.zero.paymentservice.proxy.PaymentKafkaProducer;
import org.zero.paymentservice.service.PaymentService;
import org.zero.paymentservice.utils.URIDecoder;
import org.zero.paymentservice.utils.URIEncoder;


@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final PaymentKafkaProducer paymentKafkaProducer;

    @PostMapping("/{value}")
    public void test(@PathVariable String value) {
        paymentKafkaProducer.sendPaymentHoldEvent(value);
    }
}

package org.zero.paymentservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zero.paymentservice.exception.RequestException;
import org.zero.paymentservice.mapper.CheckoutMapper;
import org.zero.paymentservice.model.Order;
import org.zero.paymentservice.model.Pay;
import org.zero.paymentservice.model.liqPay.LiqPaySignature;
import org.zero.paymentservice.service.PaymentService;
import org.zero.paymentservice.utils.DeliveryPriceProvider;
import org.zero.paymentservice.utils.IdempotencyProvider;
import org.zero.paymentservice.utils.IdempotencyValueProvider;
import org.zero.paymentservice.utils.URIDecoder;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "${CORS.FRONT_URL}")
@RequestMapping("/api/v1/checkout")
public class CheckoutController {
  private final PaymentService paymentService;
  private final IdempotencyProvider idempotencyProvider;

  @PutMapping
  public LiqPaySignature createPaymentAndGetCheckout(
      @RequestBody Order order, @RequestHeader("X-Request-ID") String idempotencyKey) {
    var idempotencyValue =
        IdempotencyValueProvider.generate(
            order.getOrderId(), order.getRecipientUserId(), order.getCartPrice().toString());
    var idempotency = idempotencyProvider.check(idempotencyKey, idempotencyValue);

    if (idempotency.exist()) {
      if (idempotency.equals()) {
        try {
          return paymentService.getPaymentCheckout(order.getOrderId());
        } catch (Exception e) {
          idempotencyProvider.delete(idempotencyKey);
          throw new RequestException("Трансакцію не знайденою Спробуйте пізніше.");
        }
      } else throw new RequestException("Трансакцію вже створено із іншими даними.");
    }

    var deliveryPrice = DeliveryPriceProvider.provide(order);
    var checkout = CheckoutMapper.map(order, deliveryPrice);
    var createdTransaction = paymentService.createPaymentTransaction(checkout);
    idempotencyProvider.save(idempotencyKey, idempotencyValue);

    return paymentService.getPaymentCheckout(createdTransaction);
  }

  @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public void updatePaymentStatus(@RequestBody String data, HttpServletRequest request) {
    var parsedData = URIDecoder.apply(data, LiqPaySignature.class);
    paymentService.updatePaymentStatus(parsedData);
  }

  @PatchMapping
  @SneakyThrows
  public void completePayment(@RequestBody Pay pay, HttpServletRequest request) {
    paymentService.completePayment(pay);
  }
}

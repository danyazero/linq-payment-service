package org.zero.paymentservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.zero.paymentservice.model.kafka.DeliveryEvent;
import org.zero.paymentservice.model.kafka.Event;
import org.zero.paymentservice.model.kafka.PaymentEvent;
import org.zero.paymentservice.model.kafka.data.Delivery;
import org.zero.paymentservice.model.kafka.data.Payment;
import org.zero.paymentservice.utils.KafkaPropsProvider;

@Configuration
public class KafkaConfig {

  @Bean
  public ProducerFactory<String, PaymentEvent> paymentProducerFactory(
      KafkaProperties kafkaProperties) {
    return KafkaPropsProvider.producer(PaymentEvent.class, Payment.class);
  }

  @Bean
  public KafkaTemplate<String, PaymentEvent> paymentKafkaTemplate(
      ProducerFactory<String, PaymentEvent> producerFactory) {
    return new KafkaTemplate<>(producerFactory);
  }

  @Bean
  public NewTopic paymentTopic() {
    return new NewTopic("payments", 1, (short) 1);
  }

  public ConsumerFactory<String, DeliveryEvent> orderConsumerFactory() {
    return KafkaPropsProvider.consumer(DeliveryEvent.class, Delivery.class, Event.class);
  }

  @Bean("deliveryListenerContainerFactory")
  public KafkaListenerContainerFactory<?> deliveryListenerContainerFactory() {
    var factory = new ConcurrentKafkaListenerContainerFactory<String, DeliveryEvent>();
    factory.setConsumerFactory(orderConsumerFactory());

    return factory;
  }
}

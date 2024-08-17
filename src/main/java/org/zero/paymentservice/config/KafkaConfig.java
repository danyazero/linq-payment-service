package org.zero.paymentservice.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.zero.paymentservice.model.kafka.PaymentEvent;
import org.zero.paymentservice.model.kafka.data.Payment;
import org.zero.paymentservice.utils.KafkaPropsProvider;


@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    @Bean
    public ProducerFactory<String, PaymentEvent> paymentProducerFactory(KafkaProperties kafkaProperties) {
        return  KafkaPropsProvider.producer(PaymentEvent.class, Payment.class);
    }

    @Bean
    public KafkaTemplate<String, PaymentEvent> paymentKafkaTemplate(ProducerFactory<String, PaymentEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public NewTopic paymentTopic() {
        return new NewTopic("payments", 1, (short) 1);
    }

//    public ConsumerFactory<String, OrderEvent> orderConsumerFactory() {
//        return KafkaPropsProvider.consumer(OrderEvent.class, Event.class);
//    }
//
////    @Bean("orderListenerContainerFactory")
//    public KafkaListenerContainerFactory<?> orderListenerContainerFactory() {
//        var factory = new ConcurrentKafkaListenerContainerFactory<String, OrderEvent>();
//        factory.setConsumerFactory(orderConsumerFactory());
//
//        return factory;
//    }

}

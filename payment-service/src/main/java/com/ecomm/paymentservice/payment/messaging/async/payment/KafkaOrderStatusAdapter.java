package com.ecomm.paymentservice.payment.messaging.async.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaOrderStatusAdapter implements OrderStatusPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topics.payment-confirmed}")
    private String paymentConfirmedTopic;

    @Value("${app.kafka.topics.payment-refunded}")
    private String paymentRefundedTopic;

    @Override
    public void confirmOrder(UUID orderId) {
        PaymentConfirmedEvent event = new PaymentConfirmedEvent(UUID.randomUUID(), orderId, Instant.now());
        send(paymentConfirmedTopic, orderId, event);
    }

    @Override
    public void cancelOrder(UUID orderId) {
        PaymentRefundedEvent event = new PaymentRefundedEvent(UUID.randomUUID(), orderId, Instant.now());
        send(paymentRefundedTopic, orderId, event);
    }

    private void send(String topic, UUID orderId, Object event) {
        kafkaTemplate.send(topic, orderId.toString(), event)
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        log.error("Hadisə göndərilmədi: topic={}, orderId={}", topic, orderId, exception);
                        return;
                    }
                    log.info("Hadisə göndərildi: topic={}, orderId={}, partition={}, offset={}",
                            topic,
                            orderId,
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                });
    }
}
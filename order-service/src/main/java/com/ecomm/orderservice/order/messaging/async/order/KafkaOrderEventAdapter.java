package com.ecomm.orderservice.order.messaging.async.order;

import com.ecomm.orderservice.order.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaOrderEventAdapter implements OrderEventPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topics.order-placed}")
    private String orderPlacedTopic;

    @Value("${app.kafka.topics.order-cancelled}")
    private String orderCancelledTopic;

    @Override
    public void publishOrderPlaced(UUID orderId, UUID userId, List<UUID> productIds) {
        OrderPlacedEvent event = new OrderPlacedEvent(
                UUID.randomUUID(), orderId, userId, productIds, Instant.now());

        publish(orderPlacedTopic, userId.toString(), orderId, event);
    }

    @Override
    public void publishOrderCancelled(Order order) {
        List<OrderCancelledEvent.Item> items = order.getItems().stream()
                .map(item -> new OrderCancelledEvent.Item(item.getProductId(), item.getQuantity()))
                .toList();

        OrderCancelledEvent event = new OrderCancelledEvent(
                UUID.randomUUID(), order.getOrderId(), order.getUserId(), items, Instant.now());

        publish(orderCancelledTopic, order.getOrderId().toString(), order.getOrderId(), event);
    }

    private void publish(String topic, String key, UUID orderId, Object event) {
        kafkaTemplate.send(topic, key, event)
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        log.error("Hadisə göndərilmədi: topic={}, orderId={}", topic, orderId, exception);
                        return;
                    }
                    log.info("Hadisə göndərildi: topic={}, orderId={}, partition={}, offset={}",
                            topic, orderId,
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                });
    }
}
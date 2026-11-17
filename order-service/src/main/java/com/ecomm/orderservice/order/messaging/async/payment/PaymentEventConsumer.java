package com.ecomm.orderservice.order.messaging.async.payment;

import com.ecomm.orderservice.order.entity.OrderStatus;
import com.ecomm.orderservice.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final OrderService orderService;

    @KafkaListener(
            topics = "${app.kafka.topics.payment-confirmed}",
            properties = "spring.json.value.default.type=com.ecomm.orderservice.order.messaging.async.payment.PaymentConfirmedEvent")
    public void onPaymentConfirmed(PaymentConfirmedEvent event) {
        log.info("Ödəniş təsdiqi qəbul edildi: eventId={}, orderId={}", event.eventId(), event.orderId());
        orderService.updateOrderStatus(event.orderId(), OrderStatus.CONFIRMED);
    }

    @KafkaListener(
            topics = "${app.kafka.topics.payment-refunded}",
            properties = "spring.json.value.default.type=com.ecomm.orderservice.order.messaging.async.payment.PaymentRefundedEvent")
    public void onPaymentRefunded(PaymentRefundedEvent event) {
        log.info("Ödənişin geri qaytarılması qəbul edildi: eventId={}, orderId={}", event.eventId(), event.orderId());
        orderService.updateOrderStatus(event.orderId(), OrderStatus.CANCELLED);
    }
}

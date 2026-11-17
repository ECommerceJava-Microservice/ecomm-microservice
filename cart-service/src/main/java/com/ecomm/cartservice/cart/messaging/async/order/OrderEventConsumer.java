package com.ecomm.cartservice.cart.messaging.async.order;

import com.ecomm.cartservice.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final CartService cartService;

    @KafkaListener(
            topics = "${app.kafka.topics.order-placed}",
            properties = "spring.json.value.default.type=com.ecomm.cartservice.cart.messaging.async.order.OrderPlacedEvent"
    )
    public void onOrderPlaced(OrderPlacedEvent event) {
        cartService.removeItemsByProductIds(event.userId(), event.productIds());
    }
}

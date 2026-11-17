package com.ecomm.productservice.product.messaging.async.order;

import com.ecomm.productservice.product.dto.request.StockChangeRequest;
import com.ecomm.productservice.product.messaging.async.ProcessedEvent;
import com.ecomm.productservice.product.messaging.async.ProcessedEventRepository;
import com.ecomm.productservice.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final ProductService productService;
    private final ProcessedEventRepository processedEventRepository;

    @Transactional
    @KafkaListener(
            topics = "${app.kafka.topics.order-cancelled}",
            properties = "spring.json.value.default.type=com.ecomm.productservice.product.messaging.async.order.OrderCancelledEvent"
    )
    public void onOrderCancelled(OrderCancelledEvent event) {
        if (processedEventRepository.existsById(event.eventId())) {
            log.info("Təkrar hadisə atıldı: eventId={}, orderId={}", event.eventId(), event.orderId());
            return;
        }

        for (OrderCancelledEvent.Item item : event.items()) {
            productService.increaseStock(new StockChangeRequest(item.productId(), item.quantity()));
        }

        processedEventRepository.save(new ProcessedEvent(event.eventId(), LocalDateTime.now()));
        log.info("Ləğv olunmuş sifarişin stoku bərpa edildi: eventId={}, orderId={}", event.eventId(), event.orderId());
    }
}

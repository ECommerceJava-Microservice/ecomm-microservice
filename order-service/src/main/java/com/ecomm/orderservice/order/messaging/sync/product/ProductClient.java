package com.ecomm.orderservice.order.messaging.sync.product;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PatchExchange;

@HttpExchange("/api/v1/internal/products")
public interface ProductClient {

    @PatchExchange("/stock/decrease")
    void decreaseStock(@RequestBody StockChangeRequest request);

    @PatchExchange("/stock/increase")
    void increaseStock(@RequestBody StockChangeRequest request);
}

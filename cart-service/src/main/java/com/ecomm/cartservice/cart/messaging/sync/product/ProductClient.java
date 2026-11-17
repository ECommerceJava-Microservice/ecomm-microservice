package com.ecomm.cartservice.cart.messaging.sync.product;

import com.ecomm.cartservice.common.response.ApiResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import java.util.UUID;

@HttpExchange("/api/v1/products")
public interface ProductClient {

    @GetExchange("/{productId}")
    ApiResponse<ProductInfo> getProduct(@PathVariable UUID productId);
}

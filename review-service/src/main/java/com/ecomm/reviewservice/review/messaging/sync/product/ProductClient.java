package com.ecomm.reviewservice.review.messaging.sync.product;

import com.ecomm.reviewservice.common.response.ApiResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.UUID;

@HttpExchange("/api/v1/products")
public interface ProductClient {
    @GetExchange("/{productId}")
    ApiResponse<ProductInfo> getProductById(@PathVariable UUID productId);
}

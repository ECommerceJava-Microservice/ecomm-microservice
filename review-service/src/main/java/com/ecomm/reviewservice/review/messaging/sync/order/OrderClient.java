package com.ecomm.reviewservice.review.messaging.sync.order;

import com.ecomm.reviewservice.common.response.ApiResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.UUID;

@HttpExchange("/api/v1/orders")
public interface OrderClient {

    @GetExchange("/delivered/{productId}")
    ApiResponse<Boolean> hasDeliveredProduct(@PathVariable UUID productId);
}

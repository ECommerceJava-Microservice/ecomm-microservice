package com.ecomm.paymentservice.payment.messaging.sync.order;

import com.ecomm.paymentservice.common.response.ApiResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.UUID;

@HttpExchange("/api/v1/orders")
public interface OrderClient {

    @GetExchange("/{orderId}")
    ApiResponse<OrderInfo> getOrderById(@PathVariable UUID orderId);
}

package com.ecomm.orderservice.order.messaging.sync.cart;

import com.ecomm.orderservice.common.response.ApiResponse;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/api/v1/cart")
public interface CartClient {

    @GetExchange
    ApiResponse<CartInfo> getMyCart();
}

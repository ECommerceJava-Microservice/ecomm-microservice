package com.ecomm.orderservice.order.messaging.sync.cart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CartInfo(
        List<CartItemInfo> items
) {
}

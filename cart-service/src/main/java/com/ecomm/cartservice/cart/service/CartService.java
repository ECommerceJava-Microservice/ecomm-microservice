package com.ecomm.cartservice.cart.service;

import com.ecomm.cartservice.cart.dto.request.CartItemRequest;
import com.ecomm.cartservice.cart.dto.response.CartResponse;
import com.ecomm.cartservice.common.response.ApiResponse;

import java.util.List;
import java.util.UUID;

public interface CartService {

    ApiResponse<CartResponse> getMyCart();

    ApiResponse<CartResponse> addItem(CartItemRequest cartItemRequest);

    ApiResponse<CartResponse> updateItem(UUID cartItemId, CartItemRequest cartItemRequest);

    ApiResponse<CartResponse> removeItem(UUID cartItemId);

    ApiResponse<Void> clearCart();

//    Kafka üçün
    void removeItemsByProductIds(UUID userId, List<UUID> productIds);
}

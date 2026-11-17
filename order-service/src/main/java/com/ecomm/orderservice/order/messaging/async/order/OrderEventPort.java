package com.ecomm.orderservice.order.messaging.async.order;

import com.ecomm.orderservice.order.entity.Order;

import java.util.List;
import java.util.UUID;

public interface OrderEventPort {

    void publishOrderPlaced(UUID orderId, UUID userId, List<UUID> productIds);

    void publishOrderCancelled(Order order);
}
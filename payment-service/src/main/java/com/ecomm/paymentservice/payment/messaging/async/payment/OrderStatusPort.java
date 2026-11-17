package com.ecomm.paymentservice.payment.messaging.async.payment;

import java.util.UUID;

public interface OrderStatusPort {
    void confirmOrder(UUID orderId);
    void cancelOrder(UUID orderId);
}
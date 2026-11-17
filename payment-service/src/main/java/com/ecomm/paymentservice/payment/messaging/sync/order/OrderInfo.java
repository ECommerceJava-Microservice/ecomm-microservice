package com.ecomm.paymentservice.payment.messaging.sync.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderInfo(
        UUID orderId,
        String orderStatus,
        BigDecimal totalAmount
) {

    private static final String STATUS_PENDING = "PENDING";

    public boolean isPending() {
        return STATUS_PENDING.equals(orderStatus);
    }
}

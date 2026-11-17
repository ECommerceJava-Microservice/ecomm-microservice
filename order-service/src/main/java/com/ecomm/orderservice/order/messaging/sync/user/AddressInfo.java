package com.ecomm.orderservice.order.messaging.sync.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AddressInfo(
        UUID addressId,
        String title,
        String city,
        String street
) {
}

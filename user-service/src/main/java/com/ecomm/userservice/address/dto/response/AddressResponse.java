package com.ecomm.userservice.address.dto.response;

import java.util.UUID;

public record AddressResponse(
        UUID addressId,
        String title,
        String country,
        String city,
        String street,
        String zipCode,
        String phone,
        boolean isDefault
) {
}

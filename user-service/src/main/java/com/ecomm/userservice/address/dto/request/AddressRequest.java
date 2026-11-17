package com.ecomm.userservice.address.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressRequest(

        @NotBlank
        @Size(max = 100)
        String title,

        @NotBlank
        @Size(max = 50)
        String country,

        @NotBlank
        @Size(max = 100)
        String city,

        @NotBlank
        @Size(max = 200)
        String street,

        @Size(max = 20)
        String zipCode,

        @NotBlank
        @Size(max = 20)
        String phone,

        boolean isDefault

) {
}

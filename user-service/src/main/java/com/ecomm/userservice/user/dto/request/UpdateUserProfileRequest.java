package com.ecomm.userservice.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserProfileRequest(

        @NotBlank(message = "Ad boş ola bilməz")
        @Size(max = 50, message = "Ad maksimum 50 simvol ola bilər")
        String firstName,

        @NotBlank(message = "Soyad boş ola bilməz")
        @Size(max = 50, message = "Soyad maksimum 50 simvol ola bilər")
        String lastName

) {
}

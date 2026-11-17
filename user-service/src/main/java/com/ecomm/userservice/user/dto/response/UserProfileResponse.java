package com.ecomm.userservice.user.dto.response;

import java.util.UUID;

public record UserProfileResponse(
        UUID userId,
        String firstName,
        String lastName
) {
}
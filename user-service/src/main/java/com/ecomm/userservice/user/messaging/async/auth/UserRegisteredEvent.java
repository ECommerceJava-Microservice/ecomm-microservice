package com.ecomm.userservice.user.messaging.async.auth;

import java.util.UUID;

public record UserRegisteredEvent(
        UUID eventId,
        UUID userId,
        String firstName,
        String lastName
) {
}

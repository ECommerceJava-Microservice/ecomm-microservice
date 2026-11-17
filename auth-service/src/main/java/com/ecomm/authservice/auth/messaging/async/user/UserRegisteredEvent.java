package com.ecomm.authservice.auth.messaging.async.user;

import java.time.Instant;
import java.util.UUID;

public record UserRegisteredEvent(
        UUID eventId,
        UUID userId,
        String email,
        String firstName,
        String lastName,
        Instant occurredAt
) {
}

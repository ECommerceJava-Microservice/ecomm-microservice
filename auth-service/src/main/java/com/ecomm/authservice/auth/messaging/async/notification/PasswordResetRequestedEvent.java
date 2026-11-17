package com.ecomm.authservice.auth.messaging.async.notification;

import java.time.Instant;
import java.util.UUID;

public record PasswordResetRequestedEvent(
        UUID eventId,
        String email,
        String token,
        Instant occurredAt
) {
}
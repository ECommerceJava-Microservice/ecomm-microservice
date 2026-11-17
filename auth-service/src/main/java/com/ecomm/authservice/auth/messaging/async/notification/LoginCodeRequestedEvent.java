package com.ecomm.authservice.auth.messaging.async.notification;

import java.time.Instant;
import java.util.UUID;

public record LoginCodeRequestedEvent(
        UUID eventId,
        String email,
        String code,
        Instant occurredAt
) {
}

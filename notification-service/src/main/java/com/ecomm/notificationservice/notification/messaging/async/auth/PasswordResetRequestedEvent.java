package com.ecomm.notificationservice.notification.messaging.async.auth;

import java.util.UUID;

public record PasswordResetRequestedEvent(
        UUID eventId,
        String email,
        String token
) {
}

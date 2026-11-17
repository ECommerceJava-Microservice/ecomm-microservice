package com.ecomm.notificationservice.notification.messaging.async.auth;

import java.util.UUID;

public record LoginCodeRequestedEvent(
        UUID eventId,
        String email,
        String code
) {
}
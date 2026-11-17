package com.ecomm.authservice.auth.messaging.async.user;

import java.util.UUID;

public interface UserRegisteredEventPort {
    void publish(UUID userId, String email, String firstName, String lastName);
}

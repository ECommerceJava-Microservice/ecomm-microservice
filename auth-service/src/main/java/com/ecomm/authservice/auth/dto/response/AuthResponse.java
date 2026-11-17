package com.ecomm.authservice.auth.dto.response;

import java.util.List;
import java.util.UUID;

public record AuthResponse(
        UUID id,
        String email,
        List<String> roles
) {}

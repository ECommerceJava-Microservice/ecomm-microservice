package com.ecomm.userservice.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CurrentUser {

    private final UUID userId;
    private final String email;
}
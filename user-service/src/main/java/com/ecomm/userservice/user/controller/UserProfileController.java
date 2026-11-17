package com.ecomm.userservice.user.controller;

import com.ecomm.userservice.common.response.ApiResponse;
import com.ecomm.userservice.common.response.PageResponse;
import com.ecomm.userservice.user.dto.request.UpdateUserProfileRequest;
import com.ecomm.userservice.user.dto.request.UserProfileFilterRequest;
import com.ecomm.userservice.user.dto.response.UserProfileResponse;
import com.ecomm.userservice.user.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/users/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getMe() {
        return ResponseEntity.ok(userProfileService.getMe());
    }

    @PatchMapping("/users/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateMe(
            @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        return ResponseEntity.ok(userProfileService.updateMe(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<PageResponse<UserProfileResponse>>> getAllUsers(
            @Valid UserProfileFilterRequest request
    ) {
        return ResponseEntity.ok(userProfileService.getAllUsers(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUserById(
            @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(
                userProfileService.getUserById(userId)
        );
    }
}

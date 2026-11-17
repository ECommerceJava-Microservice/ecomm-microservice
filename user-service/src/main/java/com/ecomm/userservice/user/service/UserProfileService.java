package com.ecomm.userservice.user.service;

import com.ecomm.userservice.common.response.ApiResponse;
import com.ecomm.userservice.common.response.PageResponse;
import com.ecomm.userservice.user.dto.request.CreateUserProfileRequest;
import com.ecomm.userservice.user.dto.request.UpdateUserProfileRequest;
import com.ecomm.userservice.user.dto.request.UserProfileFilterRequest;
import com.ecomm.userservice.user.dto.response.UserProfileResponse;

import java.util.UUID;

public interface UserProfileService {

    ApiResponse<UserProfileResponse> getMe();

    ApiResponse<UserProfileResponse> createProfile(CreateUserProfileRequest request);

    ApiResponse<UserProfileResponse> updateMe(UpdateUserProfileRequest request);

    ApiResponse<PageResponse<UserProfileResponse>> getAllUsers(UserProfileFilterRequest request);

    ApiResponse<UserProfileResponse> getUserById(UUID userId);
}
package com.ecomm.userservice.user.service.impl;

import com.ecomm.userservice.common.response.ApiResponse;
import com.ecomm.userservice.common.response.PageResponse;
import com.ecomm.userservice.security.SecurityUtils;
import com.ecomm.userservice.user.dto.request.CreateUserProfileRequest;
import com.ecomm.userservice.user.dto.request.UpdateUserProfileRequest;
import com.ecomm.userservice.user.dto.request.UserProfileFilterRequest;
import com.ecomm.userservice.user.dto.response.UserProfileResponse;
import com.ecomm.userservice.user.entity.UserProfile;
import com.ecomm.userservice.user.exception.UserProfileNotFoundException;
import com.ecomm.userservice.user.repository.UserProfileRepository;
import com.ecomm.userservice.user.repository.specification.UserProfileSpecifications;
import com.ecomm.userservice.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;

    @Override
    public ApiResponse<UserProfileResponse> getMe() {
        return ApiResponse.success(
                mapToResponse(getCurrentUserProfile()),
                "İstifadəçi məlumatları."
        );
    }

    @Override
    @Transactional
    public ApiResponse<UserProfileResponse> createProfile(CreateUserProfileRequest request) {

        if (userProfileRepository.existsById(request.userId())) {
            return ApiResponse.success(
                    mapToResponse(findUserProfileById(request.userId())),
                    "Profil artıq mövcuddur."
            );
        }

        UserProfile userProfile = UserProfile.builder()
                .userId(request.userId())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();

        userProfileRepository.save(userProfile);

        return ApiResponse.success(
                mapToResponse(userProfile),
                "Profil yaradıldı."
        );
    }

    @Override
    @Transactional
    public ApiResponse<UserProfileResponse> updateMe(
            UpdateUserProfileRequest request
    ) {

        UserProfile userProfile = getCurrentUserProfile();

        if (request.firstName() != null && !request.firstName().isBlank()) {
            userProfile.setFirstName(request.firstName());
        }

        if (request.lastName() != null && !request.lastName().isBlank()) {
            userProfile.setLastName(request.lastName());
        }

        userProfileRepository.save(userProfile);

        return ApiResponse.success(
                mapToResponse(userProfile),
                "Profil yeniləndi."
        );
    }

    @Override
    public ApiResponse<PageResponse<UserProfileResponse>> getAllUsers(
            UserProfileFilterRequest request
    ) {

        Sort sort = "asc".equalsIgnoreCase(request.sortOrder())
                ? Sort.by(request.sortBy()).ascending()
                : Sort.by(request.sortBy()).descending();

        Pageable pageable = PageRequest.of(
                request.pageNumber(),
                request.pageSize(),
                sort
        );

        Specification<UserProfile> specification =
                UserProfileSpecifications.hasName(request.name());

        Page<UserProfile> page =
                userProfileRepository.findAll(specification, pageable);

        List<UserProfileResponse> content = page.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        PageResponse<UserProfileResponse> pageResponse =
                new PageResponse<>(
                        content,
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements(),
                        page.getTotalPages(),
                        page.isLast()
                );

        return ApiResponse.success(
                pageResponse,
                "Bütün istifadəçi profilləri."
        );
    }

    @Override
    public ApiResponse<UserProfileResponse> getUserById(UUID userId) {
        return ApiResponse.success(
                mapToResponse(findUserProfileById(userId)),
                "İstifadəçi məlumatları."
        );
    }

    private UserProfile getCurrentUserProfile() {
        UUID userId = SecurityUtils.getCurrentUserId();
        return findUserProfileById(userId);
    }

    private UserProfile findUserProfileById(UUID userId) {
        return userProfileRepository.findById(userId)
                .orElseThrow(() ->
                        new UserProfileNotFoundException(
                                "İstifadəçi profili tapılmadı: " + userId
                        )
                );
    }

    private UserProfileResponse mapToResponse(UserProfile userProfile) {
        return new UserProfileResponse(
                userProfile.getUserId(),
                userProfile.getFirstName(),
                userProfile.getLastName()
        );
    }
}
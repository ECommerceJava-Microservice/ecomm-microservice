package com.ecomm.userservice.address.service.impl;

import com.ecomm.userservice.address.dto.request.AddressRequest;
import com.ecomm.userservice.address.dto.response.AddressResponse;
import com.ecomm.userservice.address.entity.Address;
import com.ecomm.userservice.address.exception.AddressAccessDeniedException;
import com.ecomm.userservice.address.exception.AddressNotFoundException;
import com.ecomm.userservice.address.repository.AddressRepository;
import com.ecomm.userservice.address.service.AddressService;
import com.ecomm.userservice.common.response.ApiResponse;
import com.ecomm.userservice.security.SecurityUtils;
import com.ecomm.userservice.user.entity.UserProfile;
import com.ecomm.userservice.user.exception.UserProfileNotFoundException;
import com.ecomm.userservice.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    @Transactional
    public ApiResponse<AddressResponse> addAddress(
            AddressRequest addressRequest
    ) {

        UserProfile userProfile = getCurrentUserProfile();

        if (addressRequest.isDefault()) {
            clearDefault(userProfile);
        }

        Address address = Address.builder()
                .userProfile(userProfile)
                .title(addressRequest.title())
                .country(addressRequest.country())
                .city(addressRequest.city())
                .street(addressRequest.street())
                .zipCode(addressRequest.zipCode())
                .phone(addressRequest.phone())
                .isDefault(addressRequest.isDefault())
                .build();

        return ApiResponse.success(
                mapToResponse(addressRepository.save(address)),
                "Ünvan əlavə edildi."
        );
    }

    @Override
    @Transactional
    public ApiResponse<AddressResponse> updateAddress(
            UUID addressId,
            AddressRequest addressRequest
    ) {

        Address address = findAddressByIdAndUser(addressId);

        if (addressRequest.isDefault()) {
            clearDefault(address.getUserProfile());
        }

        address.setTitle(addressRequest.title());
        address.setCountry(addressRequest.country());
        address.setCity(addressRequest.city());
        address.setStreet(addressRequest.street());
        address.setZipCode(addressRequest.zipCode());
        address.setPhone(addressRequest.phone());
        address.setDefault(addressRequest.isDefault());

        return ApiResponse.success(
                mapToResponse(addressRepository.save(address)),
                "Ünvan yeniləndi."
        );
    }

    @Override
    @Transactional
    public ApiResponse<UUID> deleteAddress(UUID addressId) {

        Address address = findAddressByIdAndUser(addressId);

        addressRepository.delete(address);

        return ApiResponse.success(
                addressId,
                "Ünvan silindi."
        );
    }

    @Override
    public ApiResponse<List<AddressResponse>> getMyAddresses() {

        UserProfile userProfile = getCurrentUserProfile();

        List<AddressResponse> addresses =
                addressRepository.findByUserProfile(userProfile)
                        .stream()
                        .map(this::mapToResponse)
                        .toList();

        return ApiResponse.success(
                addresses,
                "Ünvanlarınız."
        );
    }

    @Override
    @Transactional
    public ApiResponse<AddressResponse> setDefaultAddress(UUID addressId) {

        Address address = findAddressByIdAndUser(addressId);

        clearDefault(address.getUserProfile());

        address.setDefault(true);

        return ApiResponse.success(
                mapToResponse(addressRepository.save(address)),
                "Əsas ünvan təyin edildi."
        );
    }

    private void clearDefault(UserProfile userProfile) {
        addressRepository
                .findByUserProfileAndIsDefaultTrue(userProfile)
                .ifPresent(address -> {
                    address.setDefault(false);
                    addressRepository.save(address);
                });
    }

    private Address findAddressByIdAndUser(UUID addressId) {

        UserProfile userProfile = getCurrentUserProfile();

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() ->
                        new AddressNotFoundException(
                                "Ünvan tapılmadı: " + addressId
                        )
                );

        if (!address.getUserProfile()
                .getUserId()
                .equals(userProfile.getUserId())) {

            throw new AddressAccessDeniedException();
        }

        return address;
    }

    private UserProfile getCurrentUserProfile() {

        UUID userId = SecurityUtils.getCurrentUserId();

        return userProfileRepository.findById(userId)
                .orElseThrow(() ->
                        new UserProfileNotFoundException(
                                "İstifadəçi profili tapılmadı: " + userId
                        )
                );
    }

    private AddressResponse mapToResponse(Address address) {
        return new AddressResponse(
                address.getAddressId(),
                address.getTitle(),
                address.getCountry(),
                address.getCity(),
                address.getStreet(),
                address.getZipCode(),
                address.getPhone(),
                address.isDefault()
        );
    }
}
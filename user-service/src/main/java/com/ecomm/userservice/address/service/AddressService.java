package com.ecomm.userservice.address.service;

import com.ecomm.userservice.address.dto.request.AddressRequest;
import com.ecomm.userservice.address.dto.response.AddressResponse;
import com.ecomm.userservice.common.response.ApiResponse;

import java.util.List;
import java.util.UUID;

public interface AddressService {

    ApiResponse<AddressResponse> addAddress(AddressRequest addressRequest);

    ApiResponse<AddressResponse> updateAddress(UUID addressId, AddressRequest addressRequest);

    ApiResponse<UUID> deleteAddress(UUID addressId);

    ApiResponse<List<AddressResponse>> getMyAddresses();

    ApiResponse<AddressResponse> setDefaultAddress(UUID addressId);
}

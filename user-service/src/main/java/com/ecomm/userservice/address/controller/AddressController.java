package com.ecomm.userservice.address.controller;

import com.ecomm.userservice.address.dto.request.AddressRequest;
import com.ecomm.userservice.address.dto.response.AddressResponse;
import com.ecomm.userservice.address.service.AddressService;
import com.ecomm.userservice.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getMyAddresses() {
        return ResponseEntity.ok(addressService.getMyAddresses());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponse>> addAddress(
            @Valid @RequestBody AddressRequest addressRequest
    ) {
        return ResponseEntity.ok(
                addressService.addAddress(addressRequest)
        );
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(
            @PathVariable UUID addressId,
            @Valid @RequestBody AddressRequest addressRequest
    ) {
        return ResponseEntity.ok(
                addressService.updateAddress(addressId, addressRequest)
        );
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<UUID>> deleteAddress(
            @PathVariable UUID addressId
    ) {
        return ResponseEntity.ok(
                addressService.deleteAddress(addressId)
        );
    }

    @PatchMapping("/{addressId}/default")
    public ResponseEntity<ApiResponse<AddressResponse>> setDefaultAddress(
            @PathVariable UUID addressId
    ) {
        return ResponseEntity.ok(
                addressService.setDefaultAddress(addressId)
        );
    }
}

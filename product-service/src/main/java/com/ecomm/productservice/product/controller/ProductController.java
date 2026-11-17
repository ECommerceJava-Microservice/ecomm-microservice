package com.ecomm.productservice.product.controller;

import com.ecomm.productservice.common.response.ApiResponse;
import com.ecomm.productservice.common.response.PageResponse;
import com.ecomm.productservice.product.dto.request.ProductFilterRequest;
import com.ecomm.productservice.product.dto.request.ProductRequest;
import com.ecomm.productservice.product.dto.response.ProductResponse;
import com.ecomm.productservice.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PostMapping("/categories/{categoryId}/products")
    public ResponseEntity<ApiResponse<ProductResponse>> addProduct(
            @PathVariable UUID categoryId,
            @Valid @RequestBody ProductRequest productRequest) {
        ProductResponse response = productService.addProduct(categoryId, productRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Məhsul uğurla əlavə edildi"));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/products/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable UUID productId,
            @Valid @RequestBody ProductRequest productRequest) {
        ProductResponse response = productService.updateProduct(productId, productRequest);
        return ResponseEntity.ok(ApiResponse.success(response, "Məhsul məlumatları uğurla yeniləndi"));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> deleteProduct(
            @PathVariable UUID productId) {
        ProductResponse response = productService.deleteProduct(productId);
        return ResponseEntity.ok(ApiResponse.success(response, "Məhsul sistemdən uğurla silindi."));
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @PathVariable UUID productId) {
        ProductResponse response = productService.getProductById(productId);
        return ResponseEntity.ok(ApiResponse.success(response, "Məhsul uğurla gətirildi"));
    }

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getAllProducts(
            @Valid ProductFilterRequest request) {
        PageResponse<ProductResponse> response = productService.getAllProducts(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Məhsullar uğurla gətirildi"));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PatchMapping("/products/{productId}/image")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProductImage(
            @PathVariable UUID productId,
            @RequestParam("image") MultipartFile image) {
        ProductResponse response = productService.updateProductImage(productId, image);
        return ResponseEntity.ok(ApiResponse.success(response, "Məhsulun şəkli uğurla yeniləndi"));
    }
}

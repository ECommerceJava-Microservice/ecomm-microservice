package com.ecomm.productservice.product.controller;

import com.ecomm.productservice.common.response.ApiResponse;
import com.ecomm.productservice.product.dto.request.StockChangeRequest;
import com.ecomm.productservice.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//Order-service üçün
@RestController
@RequestMapping("/api/v1/internal/products")
@RequiredArgsConstructor
public class InternalProductController {

    private final ProductService productService;

    @PatchMapping("/stock/decrease")
    public ResponseEntity<ApiResponse<Void>> decreaseStock(
            @Valid @RequestBody StockChangeRequest request) {
        return ResponseEntity.ok(productService.decreaseStock(request));
    }

    @PatchMapping("/stock/increase")
    public ResponseEntity<ApiResponse<Void>> increaseStock(
            @Valid @RequestBody StockChangeRequest request) {
        return ResponseEntity.ok(productService.increaseStock(request));
    }
}

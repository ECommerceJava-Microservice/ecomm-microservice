package com.ecomm.productservice.category.controller;

import com.ecomm.productservice.category.dto.request.CategoryFilterRequest;
import com.ecomm.productservice.category.dto.request.CategoryRequest;
import com.ecomm.productservice.category.dto.response.CategoryResponse;
import com.ecomm.productservice.category.service.CategoryService;
import com.ecomm.productservice.common.response.ApiResponse;
import com.ecomm.productservice.common.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryRequest categoryRequest) {

        CategoryResponse created = categoryService.createCategory(categoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Kateqoriya uğurla yaradıldı"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @Valid @RequestBody CategoryRequest categoryRequest,
            @PathVariable UUID categoryId) {

        CategoryResponse updated = categoryService.updateCategory(categoryRequest, categoryId);
        return ResponseEntity.ok(ApiResponse.success(updated, "Kateqoriya uğurla yeniləndi"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponse>> deleteCategory(
            @PathVariable UUID categoryId) {

        CategoryResponse deleted = categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(deleted, "Kateqoriya uğurla silindi"));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<PageResponse<CategoryResponse>>> getAllCategories(
            @Valid CategoryFilterRequest request) {

        PageResponse<CategoryResponse> response = categoryService.getAllCategories(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Kateqoriyalar uğurla gətirildi"));
    }

    @GetMapping("/categories/tree")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategoryTree() {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getCategoryTree(), "Kateqoriya ağacı"));
    }
}

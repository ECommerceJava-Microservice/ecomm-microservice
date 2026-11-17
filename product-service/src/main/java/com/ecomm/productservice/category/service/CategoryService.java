package com.ecomm.productservice.category.service;

import com.ecomm.productservice.category.dto.request.CategoryFilterRequest;
import com.ecomm.productservice.category.dto.request.CategoryRequest;
import com.ecomm.productservice.category.dto.response.CategoryResponse;
import com.ecomm.productservice.common.response.PageResponse;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    CategoryResponse createCategory(@Valid CategoryRequest categoryRequest);

    CategoryResponse updateCategory(@Valid CategoryRequest categoryRequest, UUID categoryId);

    CategoryResponse deleteCategory(UUID categoryId);

    PageResponse<CategoryResponse> getAllCategories(CategoryFilterRequest request);

    List<CategoryResponse> getCategoryTree();
}

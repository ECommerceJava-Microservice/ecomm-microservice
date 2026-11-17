package com.ecomm.productservice.category.service.impl;

import com.ecomm.productservice.category.dto.request.CategoryFilterRequest;
import com.ecomm.productservice.category.dto.request.CategoryRequest;
import com.ecomm.productservice.category.dto.response.CategoryResponse;
import com.ecomm.productservice.category.entity.Category;
import com.ecomm.productservice.category.exception.CategoryAlreadyExistsException;
import com.ecomm.productservice.category.exception.CategoryNotFoundException;
import com.ecomm.productservice.category.mapper.CategoryMapper;
import com.ecomm.productservice.category.repository.CategoryRepository;
import com.ecomm.productservice.category.service.CategoryService;
import com.ecomm.productservice.common.constants.AppConstants;
import com.ecomm.productservice.common.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        if (categoryRepository.existsByCategoryName(categoryRequest.categoryName())) {
            throw new CategoryAlreadyExistsException();
        }

        Category category = categoryMapper.toEntity(categoryRequest);

        // ID generatordan gəlsin deyə öncə müvəqqəti boş path ilə qeyd edirik
        category.setPath("");
        Category savedCategory = categoryRepository.save(category);

        if (categoryRequest.parentId() == null) {
            savedCategory.setPath(savedCategory.getCategoryId() + "/");
        } else {
            Category parent = categoryRepository.findById(categoryRequest.parentId())
                    .orElseThrow(() -> new CategoryNotFoundException(
                            "Ana kateqoriya tapılmadı: " + categoryRequest.parentId()));
            savedCategory.setPath(parent.getPath() + savedCategory.getCategoryId() + "/");
        }
        return categoryMapper.toResponse(categoryRepository.save(savedCategory));
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(CategoryRequest categoryRequest, UUID categoryId) {
        if (categoryRepository.existsByCategoryNameAndCategoryIdNot(categoryRequest.categoryName(), categoryId)) {
            throw new CategoryAlreadyExistsException();
        }
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Kateqoriya tapılmadı: " + categoryId));

        categoryMapper.updateEntity(categoryRequest, category);

        if (categoryRequest.parentId() == null) {
            category.setPath(category.getCategoryId() + "/");
        } else {
            Category parent = categoryRepository.findById(categoryRequest.parentId())
                    .orElseThrow(() -> new CategoryNotFoundException(
                            "Ana kateqoriya tapılmadı: " + categoryRequest.parentId()));
            category.setPath(parent.getPath() + category.getCategoryId() + "/");
        }

        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryResponse deleteCategory(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Kateqoriya tapılmadı: " + categoryId));

        CategoryResponse response = categoryMapper.toResponse(category);
        categoryRepository.delete(category);
        return response;
    }

    @Override
    public PageResponse<CategoryResponse> getAllCategories(CategoryFilterRequest request) {
        Sort sort = request.getSortOrder().equalsIgnoreCase(AppConstants.SORT_ASC)
                ? Sort.by(request.getSortBy()).ascending()
                : Sort.by(request.getSortBy()).descending();

        Pageable pageable = PageRequest.of(request.getPageNumber(), request.getPageSize(), sort);
        Page<Category> page = categoryRepository.findAll(pageable);

        List<CategoryResponse> content = page.getContent().stream()
                .map(categoryMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    @Override
    public List<CategoryResponse> getCategoryTree() {
        List<Category> allCategories = categoryRepository.findAll();

        Map<UUID, CategoryResponse> responseMap = new HashMap<>();
        List<CategoryResponse> rootCategories = new ArrayList<>();

        for (Category category : allCategories) {
            CategoryResponse response = categoryMapper.toResponse(category);
            responseMap.put(response.categoryId(), response);
        }

        for (CategoryResponse response : responseMap.values()) {
            if (response.parentId() == null) {
                // Valideyni yoxdursa, deməli əsas kateqoriyadır
                rootCategories.add(response);
            } else {
                CategoryResponse parent = responseMap.get(response.parentId());
                if (parent != null) {
                    parent.children().add(response);
                }
            }
        }

        return rootCategories;
    }
}

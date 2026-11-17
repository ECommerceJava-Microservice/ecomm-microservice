package com.ecomm.productservice.product.service.impl;

import com.ecomm.productservice.category.entity.Category;
import com.ecomm.productservice.category.exception.CategoryNotFoundException;
import com.ecomm.productservice.category.repository.CategoryRepository;
import com.ecomm.productservice.common.constants.AppConstants;
import com.ecomm.productservice.common.response.ApiResponse;
import com.ecomm.productservice.common.response.PageResponse;
import com.ecomm.productservice.file.dto.response.FileResponse;
import com.ecomm.productservice.file.service.FileService;
import com.ecomm.productservice.product.dto.request.ProductFilterRequest;
import com.ecomm.productservice.product.dto.request.ProductRequest;
import com.ecomm.productservice.product.dto.request.StockChangeRequest;
import com.ecomm.productservice.product.dto.response.ProductResponse;
import com.ecomm.productservice.product.entity.Product;
import com.ecomm.productservice.product.exception.InsufficientStockException;
import com.ecomm.productservice.product.exception.ProductAccessDeniedException;
import com.ecomm.productservice.product.exception.ProductNotFoundException;
import com.ecomm.productservice.product.mapper.ProductMapper;
import com.ecomm.productservice.product.repository.ProductRepository;
import com.ecomm.productservice.product.repository.specification.ProductSpecifications;
import com.ecomm.productservice.product.service.ProductService;
import com.ecomm.productservice.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final FileService fileService;

    @Value("${project.image}")
    private String path;

    @Override
    @Transactional
    public ProductResponse addProduct(UUID categoryId, ProductRequest request) {

        UUID userId = SecurityUtils.getCurrentUserId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Kateqoriya tapılmadı: " + categoryId));
        Product product = productMapper.toEntity(request);
        product.setCategory(category);
        product.setCreatedBy(userId);
        if (product.getImage() == null || product.getImage().isBlank()) {
            product.setImage(null);
            product.setImageId(null);
        }
        product.setSpecialPrice(
                calculateSpecialPrice(product.getPrice(), product.getDiscount())
        );
        Product saved = productRepository.save(product);
        log.info("Product created: {} by {}", saved.getProductId(), userId);
        return productMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(UUID productId, ProductRequest request) {
        Product product = findProduct(productId);
        checkOwnership(product);
        productMapper.updateEntity(request, product);
        product.setSpecialPrice(calculateSpecialPrice(product.getPrice(), product.getDiscount()));
        Product updated = productRepository.save(product);
        return productMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public ProductResponse deleteProduct(UUID productId) {
        Product product = findProduct(productId);
        checkOwnership(product);
        String oldImageId = product.getImageId();
        ProductResponse response = productMapper.toResponse(product);
        productRepository.delete(product);
        if (oldImageId != null && !oldImageId.isBlank()) {
            fileService.deleteImage(oldImageId);
        }
        return response;
    }

    @Override
    public ProductResponse getProductById(UUID productId) {
        return productMapper.toResponse(findProduct(productId));
    }

    @Override
    @Transactional
    public ProductResponse updateProductImage(UUID productId, MultipartFile image) {
        Product product = findProduct(productId);
        checkOwnership(product);
        String oldImageId = product.getImageId();
        FileResponse fileResponse = fileService.uploadImage(path, image);
        product.setImage(fileResponse.fileUrl());
        product.setImageId(fileResponse.fileId());

        Product savedProduct = productRepository.save(product);
        if (oldImageId != null && !oldImageId.isBlank()) {
            fileService.deleteImage(oldImageId);
        }
        return productMapper.toResponse(savedProduct);
    }

    @Override
    public PageResponse<ProductResponse> getAllProducts(ProductFilterRequest request) {
        Sort sort = request.getSortOrder().equalsIgnoreCase(AppConstants.SORT_ASC)
                ? Sort.by(request.getSortBy()).ascending()
                : Sort.by(request.getSortBy()).descending();

        Pageable pageable = PageRequest.of(
                request.getPageNumber(),
                request.getPageSize(),
                sort
        );

        Specification<Product> spec = Specification
                .where(ProductSpecifications.hasName(request.getName()))
                .and(ProductSpecifications.hasPriceBetween(request.getMinPrice(), request.getMaxPrice()))
                .and(ProductSpecifications.hasCategory(request.getCategoryId(), categoryRepository));

        Page<Product> page = productRepository.findAll(spec, pageable);

        List<ProductResponse> content = page.getContent()
                .stream()
                .map(productMapper::toResponse)
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

    //Order-service üçün
    @Override
    @Transactional
    public ApiResponse<Void> decreaseStock(StockChangeRequest request) {
        int updated = productRepository.decreaseStock(request.productId(), request.quantity());

        if (updated == 0) {
            if (!productRepository.existsById(request.productId())) {
                throw new ProductNotFoundException();
            }
            throw new InsufficientStockException("Stokda kifayət qədər məhsul yoxdur");
        }

        return ApiResponse.success(null, "Stok azaldıldı");
    }

    //Order-service üçün
    @Override
    @Transactional
    public ApiResponse<Void> increaseStock(StockChangeRequest request) {
        int updated = productRepository.increaseStock(request.productId(), request.quantity());

        if (updated == 0) {
            throw new ProductNotFoundException();
        }

        return ApiResponse.success(null, "Stok artırıldı");
    }

    // Köməkçi metodlar--------------

    private Product findProduct(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Məhsul tapılmadı: " + id));
    }

    private void checkOwnership(Product product) {

        UUID userId = SecurityUtils.getCurrentUserId();

        boolean isAdmin = Objects.requireNonNull(SecurityContextHolder.getContext()
                        .getAuthentication())
                .getAuthorities()
                .stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_ADMIN"));

        if (isAdmin) return;

        if (!product.getCreatedBy().equals(userId)) {
            log.error("İcazəsiz giriş: {} istifadəçisi {} məhsuluna müdaxilə etmək cəhdi", userId, product.getProductId());
            throw new ProductAccessDeniedException();
        }
    }

    private BigDecimal calculateSpecialPrice(BigDecimal price, BigDecimal discount) {
        if (discount == null || discount.compareTo(BigDecimal.ZERO) <= 0) {
            return price;
        }
        BigDecimal discountAmount = price.multiply(discount)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return price.subtract(discountAmount);
    }
}
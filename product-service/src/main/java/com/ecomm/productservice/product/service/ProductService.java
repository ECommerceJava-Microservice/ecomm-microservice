package com.ecomm.productservice.product.service;

import com.ecomm.productservice.common.response.ApiResponse;
import com.ecomm.productservice.common.response.PageResponse;
import com.ecomm.productservice.product.dto.request.ProductFilterRequest;
import com.ecomm.productservice.product.dto.request.ProductRequest;
import com.ecomm.productservice.product.dto.request.StockChangeRequest;
import com.ecomm.productservice.product.dto.response.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ProductService {

    ProductResponse addProduct(UUID categoryId, @Valid ProductRequest productRequest);

    ProductResponse updateProduct(UUID productId, @Valid ProductRequest productRequest);

    ProductResponse deleteProduct(UUID productId);

    ProductResponse getProductById(UUID productId);

    ProductResponse updateProductImage(UUID productId, MultipartFile image);

    PageResponse<ProductResponse> getAllProducts(ProductFilterRequest request);

    //Order-service üçün
    ApiResponse<Void> decreaseStock(StockChangeRequest request);

    //Order-service üçün
    ApiResponse<Void> increaseStock(StockChangeRequest request);
}

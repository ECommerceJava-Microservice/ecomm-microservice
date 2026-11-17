package com.ecomm.productservice.product.mapper;

import com.ecomm.productservice.product.dto.request.ProductRequest;
import com.ecomm.productservice.product.dto.response.ProductResponse;
import com.ecomm.productservice.product.entity.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "category.categoryId", target = "categoryId")
    @Mapping(source = "category.categoryName", target = "categoryName")
    ProductResponse toResponse(Product product);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "specialPrice", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "imageId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(ProductRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "specialPrice", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "imageId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ProductRequest request, @MappingTarget Product product);
}

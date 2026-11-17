package com.ecomm.productservice.product.dto.request;

import com.ecomm.productservice.common.constants.AppConstants;
import com.ecomm.productservice.common.request.PageFilterRequest;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ProductFilterRequest extends PageFilterRequest {

    private String name;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private UUID categoryId;

    public ProductFilterRequest() {
        setSortBy(AppConstants.SORT_PRODUCTS_BY);
    }
}

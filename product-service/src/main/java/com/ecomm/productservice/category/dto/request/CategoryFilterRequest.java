package com.ecomm.productservice.category.dto.request;

import com.ecomm.productservice.common.constants.AppConstants;
import com.ecomm.productservice.common.request.PageFilterRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryFilterRequest extends PageFilterRequest {

    public CategoryFilterRequest() {
        setSortBy(AppConstants.SORT_CATEGORIES_BY);
    }
}

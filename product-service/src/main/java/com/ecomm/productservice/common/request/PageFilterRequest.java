package com.ecomm.productservice.common.request;

import com.ecomm.productservice.common.constants.AppConstants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageFilterRequest {

    @Min(value = 0, message = "Səhifə nömrəsi 0-dan kiçik ola bilməz")
    private int pageNumber = 0;

    @Min(value = 1, message = "Səhifə ölçüsü ən azı 1 olmalıdır")
    @Max(value = 100, message = "Səhifə ölçüsü 100-dən çox ola bilməz")
    private int pageSize = 18;

    private String sortBy;

    private String sortOrder = AppConstants.SORT_ASC;
}
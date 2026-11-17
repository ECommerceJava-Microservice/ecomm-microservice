package com.ecomm.reviewservice.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ReviewPageRequest(

        @Min(value = 0, message = "Səhifə nömrəsi 0-dan kiçik ola bilməz")
        Integer pageNumber,

        @Min(value = 1, message = "Səhifə ölçüsü ən azı 1 olmalıdır")
        @Max(value = 100, message = "Səhifə ölçüsü ən çox 100 ola bilər")
        Integer pageSize
) {

    public ReviewPageRequest {
        if (pageNumber == null) {
            pageNumber = 0;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
    }
}

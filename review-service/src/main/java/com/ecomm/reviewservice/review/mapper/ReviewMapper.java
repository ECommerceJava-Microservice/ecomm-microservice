package com.ecomm.reviewservice.review.mapper;

import com.ecomm.reviewservice.review.dto.response.ReviewResponse;
import com.ecomm.reviewservice.review.entity.Review;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    ReviewResponse toResponse(Review review, String productName);
}

package com.ecomm.reviewservice.review.service;

import com.ecomm.reviewservice.common.response.ApiResponse;
import com.ecomm.reviewservice.common.response.PageResponse;
import com.ecomm.reviewservice.review.dto.request.ReviewRequest;
import com.ecomm.reviewservice.review.dto.response.ReviewResponse;

import java.util.UUID;

public interface ReviewService {

    ApiResponse<ReviewResponse> createReview(ReviewRequest reviewRequest);

    ApiResponse<ReviewResponse> updateReview(UUID reviewId, ReviewRequest reviewRequest);

    ApiResponse<UUID> deleteReview(UUID reviewId);

    ApiResponse<PageResponse<ReviewResponse>> getProductReviews(UUID productId, int page, int size);

    ApiResponse<Double> getAverageRating(UUID productId);
}

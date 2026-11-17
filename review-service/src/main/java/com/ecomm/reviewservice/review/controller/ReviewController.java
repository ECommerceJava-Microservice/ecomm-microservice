package com.ecomm.reviewservice.review.controller;

import com.ecomm.reviewservice.common.response.ApiResponse;
import com.ecomm.reviewservice.common.response.PageResponse;
import com.ecomm.reviewservice.review.dto.request.ReviewPageRequest;
import com.ecomm.reviewservice.review.dto.request.ReviewRequest;
import com.ecomm.reviewservice.review.dto.response.ReviewResponse;
import com.ecomm.reviewservice.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @Valid @RequestBody ReviewRequest reviewRequest) {
        return ResponseEntity.ok(reviewService.createReview(reviewRequest));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
            @PathVariable UUID reviewId,
            @Valid @RequestBody ReviewRequest reviewRequest) {
        return ResponseEntity.ok(reviewService.updateReview(reviewId, reviewRequest));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<UUID>> deleteReview(
            @PathVariable UUID reviewId) {
        return ResponseEntity.ok(reviewService.deleteReview(reviewId));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<PageResponse<ReviewResponse>>> getProductReviews(
            @PathVariable UUID productId,
            @Valid ReviewPageRequest pageRequest) {
        return ResponseEntity.ok(reviewService.getProductReviews(
                productId, pageRequest.pageNumber(), pageRequest.pageSize()));
    }


    @GetMapping("/product/{productId}/average")
    public ResponseEntity<ApiResponse<Double>> getAverageRating(
            @PathVariable UUID productId) {
        return ResponseEntity.ok(reviewService.getAverageRating(productId));
    }
}

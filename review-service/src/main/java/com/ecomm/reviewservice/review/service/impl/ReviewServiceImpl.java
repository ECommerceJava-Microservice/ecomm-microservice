package com.ecomm.reviewservice.review.service.impl;

import com.ecomm.reviewservice.common.response.ApiResponse;
import com.ecomm.reviewservice.common.response.PageResponse;
import com.ecomm.reviewservice.review.messaging.sync.order.OrderClient;
import com.ecomm.reviewservice.review.messaging.sync.product.ProductClient;
import com.ecomm.reviewservice.review.messaging.sync.product.ProductInfo;
import com.ecomm.reviewservice.review.messaging.sync.user.UserClient;
import com.ecomm.reviewservice.review.dto.request.ReviewRequest;
import com.ecomm.reviewservice.review.dto.response.ReviewResponse;
import com.ecomm.reviewservice.review.entity.Review;
import com.ecomm.reviewservice.review.exception.ProductNotPurchasedException;
import com.ecomm.reviewservice.review.exception.ReviewAccessDeniedException;
import com.ecomm.reviewservice.review.exception.ReviewAlreadyExistsException;
import com.ecomm.reviewservice.review.exception.ReviewNotFoundException;
import com.ecomm.reviewservice.review.mapper.ReviewMapper;
import com.ecomm.reviewservice.review.repository.ReviewRepository;
import com.ecomm.reviewservice.review.service.ReviewService;
import com.ecomm.reviewservice.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ProductClient productClient;
    private final UserClient userClient;
    private final OrderClient orderClient;

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ApiResponse<ReviewResponse> createReview(ReviewRequest reviewRequest) {
        UUID userId = SecurityUtils.getCurrentUserId();
        ProductInfo product = findProduct(reviewRequest.productId());

        if (!Boolean.TRUE.equals(orderClient.hasDeliveredProduct(reviewRequest.productId()).data())) {
            throw new ProductNotPurchasedException();
        }
        if (reviewRepository.existsByUserIdAndProductId(userId, reviewRequest.productId())) {
            throw new ReviewAlreadyExistsException();
        }

        Review review = Review.builder()
                .userId(userId)
                .productId(reviewRequest.productId())
                .userFullName(userClient.getCurrentUser().data().fullName())
                .rating(reviewRequest.rating())
                .comment(reviewRequest.comment())
                .build();

        Review savedReview = reviewRepository.save(review);
        return ApiResponse.success(
                reviewMapper.toResponse(savedReview, product.productName()), "Rəy əlavə edildi");
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ApiResponse<ReviewResponse> updateReview(UUID reviewId, ReviewRequest reviewRequest) {
        Review review = findReview(reviewId);
        if (!review.getUserId().equals(SecurityUtils.getCurrentUserId())) {
            throw new ReviewAccessDeniedException();
        }
        ProductInfo product = findProduct(review.getProductId());

        review.setUserFullName(userClient.getCurrentUser().data().fullName());
        review.setRating(reviewRequest.rating());
        review.setComment(reviewRequest.comment());

        Review savedReview = reviewRepository.save(review);
        return ApiResponse.success(
                reviewMapper.toResponse(savedReview, product.productName()), "Rəy yeniləndi");
    }

    @Override
    @Transactional
    public ApiResponse<UUID> deleteReview(UUID reviewId) {
        Review review = findReview(reviewId);
        if (!SecurityUtils.isAdmin() && !review.getUserId().equals(SecurityUtils.getCurrentUserId())) {
            throw new ReviewAccessDeniedException();
        }
        reviewRepository.delete(review);
        return ApiResponse.success(reviewId, "Rəy silindi");
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ApiResponse<PageResponse<ReviewResponse>> getProductReviews(UUID productId, int page, int size) {
        ProductInfo product = findProduct(productId);
        Page<Review> reviewPage = reviewRepository.findByProductId(productId,
                PageRequest.of(page, size, Sort.by("createdAt").descending()));

        List<ReviewResponse> content = reviewPage.getContent().stream()
                .map(review -> reviewMapper.toResponse(review, product.productName()))
                .toList();

        return ApiResponse.success(new PageResponse<>(
                content,
                reviewPage.getNumber(),
                reviewPage.getSize(),
                reviewPage.getTotalElements(),
                reviewPage.getTotalPages(),
                reviewPage.isLast()), "Məhsulun rəyləri");
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ApiResponse<Double> getAverageRating(UUID productId) {
        findProduct(productId);
        Double average = reviewRepository.findAverageRatingByProductId(productId);
        return ApiResponse.success(average != null ? average : 0.0, "Ortalama reytinq");
    }

    // Köməkçi metodlar --------------------

    private Review findReview(UUID reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);
    }

    private ProductInfo findProduct(UUID productId) {
        return productClient.getProductById(productId).data();
    }
}
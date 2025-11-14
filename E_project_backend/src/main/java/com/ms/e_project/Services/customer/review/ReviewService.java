package com.ms.e_project.Services.customer.review;

import com.ms.e_project.dto.OrderProductResponseDto;
import com.ms.e_project.dto.ReviewDto;

public interface ReviewService {
    OrderProductResponseDto getOrderdProductsDetailsByOrderId(Long orderId);
    ReviewDto giveReview(ReviewDto reviewDto);
    boolean hasUserReviewed(Long userId, Long productId);
}

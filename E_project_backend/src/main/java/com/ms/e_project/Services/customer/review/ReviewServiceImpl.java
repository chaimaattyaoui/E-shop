package com.ms.e_project.Services.customer.review;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ms.e_project.Entities.*;
import com.ms.e_project.Repository.OrderRepository;
import com.ms.e_project.Repository.ProductRepository;
import com.ms.e_project.Repository.ReviewRepository;
import com.ms.e_project.Repository.UserRepository;
import com.ms.e_project.dto.OrderProductResponseDto;
import com.ms.e_project.dto.ProductDto;
import com.ms.e_project.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final Cloudinary cloudinary;

    @Override
    public OrderProductResponseDto getOrderdProductsDetailsByOrderId(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        OrderProductResponseDto orderProductResponseDto = new OrderProductResponseDto();
        if (optionalOrder.isPresent()) {
            orderProductResponseDto.setOrderAmount(optionalOrder.get().getAmount());
            List<ProductDto> productDtoList = new ArrayList<>();
            for (CartItems cartItems : optionalOrder.get().getCartItems()) {
                ProductDto productDto = new ProductDto();
                productDto.setId(cartItems.getProduct().getId());
                productDto.setName(cartItems.getProduct().getName());
                productDto.setPrice(cartItems.getPrice());
                productDto.setQuantity(cartItems.getQuantity());
                productDto.setImageUrl(cartItems.getProduct().getImageUrl());
                productDtoList.add(productDto);
            }
            orderProductResponseDto.setProductDtoList(productDtoList);
        }
        return orderProductResponseDto;
    }

    @Override
    public boolean hasUserReviewed(Long userId, Long productId) {
        return reviewRepository.findAllByProductId(productId)
                .stream()
                .anyMatch(review -> review.getUser().getId().equals(userId));
    }

    @Override
    public ReviewDto giveReview(ReviewDto reviewDto) {
        Review review; // Declare review outside the if-else block

        if (hasUserReviewed(reviewDto.getUserId(), reviewDto.getProductId())) {
            // Update existing review
            review = reviewRepository.findAllByProductId(reviewDto.getProductId())
                    .stream()
                    .filter(r -> r.getUser().getId().equals(reviewDto.getUserId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Review not found"));
            review.setRating(reviewDto.getRating());
            review.setDescription(reviewDto.getDescription());
            if (reviewDto.getImg() != null) {
                // Handle image update if provided
                review.setImageUrl(processImage(reviewDto.getImg()));
            }
            reviewRepository.save(review);
        } else {
            // Create new review
            review = new Review();
            review.setRating(reviewDto.getRating());
            review.setDescription(reviewDto.getDescription());

            // Fetch User and Product from repositories
            Optional<User> optionalUser = userRepository.findById(reviewDto.getUserId());
            Optional<Product> optionalProduct = productRepository.findById(reviewDto.getProductId());

            if (optionalUser.isPresent() && optionalProduct.isPresent()) {
                review.setUser(optionalUser.get());
                review.setProduct(optionalProduct.get());
                if (reviewDto.getImg() != null) {
                    review.setImageUrl(processImage(reviewDto.getImg()));
                }
                reviewRepository.save(review);
            } else {
                throw new RuntimeException("User or Product not found");
            }
        }
        return review.getDto();
    }

    private String processImage(MultipartFile img) {
        try {
            var uploadResult = cloudinary.uploader()
                    .upload(img.getBytes(), ObjectUtils.asMap("folder", "reviews"));
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Cloudinary upload failed: " + e.getMessage());
        }
    }
}
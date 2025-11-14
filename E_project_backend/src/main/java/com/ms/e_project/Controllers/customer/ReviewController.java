package com.ms.e_project.Controllers.customer;


import com.ms.e_project.Services.customer.review.ReviewService;
import com.ms.e_project.dto.OrderProductResponseDto;
import com.ms.e_project.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/ordered-products/{orderId}")
    public ResponseEntity<OrderProductResponseDto> getOrderdProductsDetailsByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(reviewService.getOrderdProductsDetailsByOrderId(orderId));
    }
/*
    @PostMapping("/review")
    public ResponseEntity<?> giveReview(@ModelAttribute ReviewDto reviewDto) {
        ReviewDto reviewDto1 = reviewService.giveReview(reviewDto);
        if (reviewDto1 == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewDto1);


    }*/

    @PostMapping("/review")
    public ResponseEntity<?> giveReview(@ModelAttribute ReviewDto reviewDto) {
        try {
            ReviewDto result = reviewService.giveReview(reviewDto);
            if (result == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
            }
            // Determine if it's an update or creation based on the ID (assuming ID is set for existing reviews)
            if (result.getId() != null) {
                return ResponseEntity.status(HttpStatus.OK).body(result); // Return 200 OK for updates
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body(result); // Return 201 Created for new reviews
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

}


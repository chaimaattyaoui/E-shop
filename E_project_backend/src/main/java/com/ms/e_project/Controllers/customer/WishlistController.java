package com.ms.e_project.Controllers.customer;

import com.ms.e_project.Services.customer.wishlist.WishlistService;
import com.ms.e_project.dto.WishlistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class WishlistController {
    private final WishlistService wishlistService;



    @PostMapping("/wishlist")
public ResponseEntity<?> addProductToWishlist(@RequestBody WishlistDto wishlistDto) {
    WishlistDto postedWishlistDto = wishlistService.addProductToWishlist(wishlistDto);
    if (postedWishlistDto == null)
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Already in Wishlist");
    return ResponseEntity.status(HttpStatus.CREATED).body(postedWishlistDto);
}


    @GetMapping("/wishlist/{userId}")
    public ResponseEntity<List<WishlistDto>> getWishlistByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(wishlistService.getWishlistByUserId(userId));
    }
}

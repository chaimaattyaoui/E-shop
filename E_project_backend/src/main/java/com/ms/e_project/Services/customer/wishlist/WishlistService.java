package com.ms.e_project.Services.customer.wishlist;

import com.ms.e_project.dto.WishlistDto;

import java.util.List;

public interface WishlistService {

    WishlistDto addProductToWishlist(WishlistDto wishlistDto);
    List<WishlistDto> getWishlistByUserId(Long userId);
}

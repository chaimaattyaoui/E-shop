package com.ms.e_project.Services.customer.wishlist;

import com.ms.e_project.Entities.Product;
import com.ms.e_project.Entities.User;
import com.ms.e_project.Entities.Wishlist;
import com.ms.e_project.Repository.ProductRepository;
import com.ms.e_project.Repository.UserRepository;
import com.ms.e_project.Repository.WishlistRepository;
import com.ms.e_project.dto.WishlistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final WishlistRepository wishlistRepository;

    public WishlistDto addProductToWishlist(WishlistDto wishlistDto) {
        Optional<Product> optionalProduct = productRepository.findById(wishlistDto.getProductId());
        Optional<User> optionalUser = userRepository.findById(wishlistDto.getUserId());

        if (optionalProduct.isPresent() && optionalUser.isPresent()) {
            Product product = optionalProduct.get();
            User user = optionalUser.get();

            // ✅ Vérifie si ce produit est déjà dans la wishlist de cet utilisateur
            Optional<Wishlist> existingWishlist = wishlistRepository.findByUserAndProduct(user, product);
            if (existingWishlist.isPresent()) {
                // déjà dans la wishlist → renvoyer null pour signaler le front
                return null;
            }

            Wishlist wishlist = new Wishlist();
            wishlist.setProduct(product);
            wishlist.setUser(user);

            return wishlistRepository.save(wishlist).getWishlistDto();
        }

        return null;
    }

    public List<WishlistDto> getWishlistByUserId(Long userId) {
        return wishlistRepository.findAllByUserId(userId).stream().map(Wishlist::getWishlistDto).collect(Collectors.toList());
    }
}

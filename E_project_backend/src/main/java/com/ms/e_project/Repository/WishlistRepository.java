package com.ms.e_project.Repository;

import com.ms.e_project.Entities.Product;
import com.ms.e_project.Entities.User;
import com.ms.e_project.Entities.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findAllByUserId(Long userId);
    Optional<Wishlist> findByUserAndProduct(User user, Product product);
}

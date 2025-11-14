package com.ms.e_project.Repository;


import com.ms.e_project.Entities.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    boolean existsByCode(String code);
    Optional<Coupon> findByCode(String code);
    List<Coupon> findAllByExpirationDateAfter(Date date);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.coupon.id = :couponId")
    long countOrdersUsingCoupon(@Param("couponId") Long couponId);
}

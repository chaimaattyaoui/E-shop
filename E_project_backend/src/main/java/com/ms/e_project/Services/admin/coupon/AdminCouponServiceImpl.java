package com.ms.e_project.Services.admin.coupon;

import com.ms.e_project.Entities.Coupon;
import com.ms.e_project.Repository.CouponRepository;
import com.ms.e_project.exceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCouponServiceImpl implements AdminCouponService {

    private final CouponRepository couponRepository;

    @Override
    public Coupon createCoupon(Coupon coupon) {
        if (couponRepository.existsByCode(coupon.getCode())) {
            throw new ValidationException("Coupon code already exists");
        }
        coupon.setCreatedAt(new Date()); // ✅ date actuelle
        return couponRepository.save(coupon);
    }

    @Override
    public void deleteCoupon(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Coupon not found"));

        // Check if the coupon is associated with any orders
        if (couponRepository.countOrdersUsingCoupon(id) > 0) {
            throw new ValidationException("Cannot delete coupon as it is associated with existing orders");
        }

        couponRepository.delete(coupon);
    }

    @Override
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }
}
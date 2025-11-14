package com.ms.e_project.Services.admin.coupon;

import com.ms.e_project.Entities.Coupon;

import java.util.List;

public interface AdminCouponService {
     Coupon createCoupon(Coupon coupon);
    List<Coupon> getAllCoupons();
    void deleteCoupon(Long id);
}

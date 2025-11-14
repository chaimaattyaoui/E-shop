package com.ms.e_project.Controllers.admin;

import com.ms.e_project.Entities.Coupon;
import com.ms.e_project.Services.admin.coupon.AdminCouponService;
import com.ms.e_project.exceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/coupons")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AdminCouponController {
    private final AdminCouponService adminCouponService;

    @PostMapping

    public ResponseEntity<?> createCoupon(@RequestBody Coupon coupon) {
        try {
            Coupon createdCoupon = adminCouponService.createCoupon(coupon);
            return ResponseEntity.ok(createdCoupon);
        } catch (ValidationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping

    public ResponseEntity<List<Coupon>> getAllCoupons() {
        return ResponseEntity.ok(adminCouponService.getAllCoupons());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable Long id) {
        try {
            adminCouponService.deleteCoupon(id);
            return ResponseEntity.ok("Coupon deleted successfully");
        } catch (ValidationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage()); // Use 409 Conflict for resource dependency
        }
    }


}

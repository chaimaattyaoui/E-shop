package com.ms.e_project.Controllers.customer;


import com.ms.e_project.Entities.Coupon;
import com.ms.e_project.Entities.Product;
import com.ms.e_project.Repository.CouponRepository;
import com.ms.e_project.Services.customer.CustomerProductService;
import com.ms.e_project.dto.ProductDetailDto;
import com.ms.e_project.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class CustomerProductController {
    private final CustomerProductService customerProductService;

/*

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getAllProduct() {
        List<ProductDto> productDtos = customerProductService.getAllProducts();
        return ResponseEntity.ok(productDtos);
    }

 */

    @GetMapping("/search/{name}")
    public ResponseEntity<List<ProductDto>> getAllProductByName(@PathVariable String name) {
        List<ProductDto> productDtos = customerProductService.searchProductByTitle(name);
        return ResponseEntity.ok(productDtos);
    }

    @GetMapping("/product/{productId}")

    public ResponseEntity<ProductDetailDto> getProductDetailById(@PathVariable long productId) {
        ProductDetailDto productDetailDto = customerProductService.getProductDetailById(productId);
        if (productDetailDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(productDetailDto);
    }


    @GetMapping("/products-with-ratings")
    public ResponseEntity<List<Product>> getAllProductsWithRatings() {
        return ResponseEntity.ok(customerProductService.getAllProductsWithRatings());
    }

}

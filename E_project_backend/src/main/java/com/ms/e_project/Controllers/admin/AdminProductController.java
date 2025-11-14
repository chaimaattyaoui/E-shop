package com.ms.e_project.Controllers.admin;

import com.ms.e_project.Entities.Product;
import com.ms.e_project.Repository.ProductRepository;
import com.ms.e_project.Services.admin.adminproduct.AdminProductService;
import com.ms.e_project.Services.admin.faq.FAQService;
import com.ms.e_project.dto.FAQDto;
import com.ms.e_project.dto.ProductDetailDto;
import com.ms.e_project.dto.ProductDto;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AdminProductController {
    private final AdminProductService adminProductService;
    private final ProductRepository productRepository;
    private final FAQService faqService;

    @PostMapping("/product")

    public ResponseEntity<ProductDto> addProduct(@ModelAttribute ProductDto productDto) throws IOException {
        ProductDto productDto1 = adminProductService.addProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDto1);
    }

/*
    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getAllProduct() {
        List<ProductDto> productDtos = adminProductService.getAllProducts();
        return ResponseEntity.ok(productDtos);
    }
    */

    @GetMapping("/product/detail/{productId}")

    public ResponseEntity<ProductDetailDto> getProductDetailById(@PathVariable long productId) {
        ProductDetailDto productDetailDto = adminProductService.getProductDetailById(productId);
        if (productDetailDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(productDetailDto);
    }


    @GetMapping("/search/{name}")
    public ResponseEntity<List<ProductDto>> getAllProductByName(@PathVariable String name) {
        List<ProductDto> productDtos = adminProductService.getAllProductByName(name);
        return ResponseEntity.ok(productDtos);
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        boolean deleted = adminProductService.deleteProduct(productId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();

    }

    @PostMapping("/faq/{productId}")
    public ResponseEntity<FAQDto> postFAQ(@PathVariable Long productId , @RequestBody FAQDto faqDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(faqService.postFAQ(productId, faqDto));
    }


    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {
        ProductDto productDto = adminProductService.getProductById(productId);
        if (productDto != null) {
            return ResponseEntity.ok(productDto);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/product/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long productId,
                                                    @ModelAttribute ProductDto productDto) throws IOException {
        ProductDto updatedProduct = adminProductService.updateProduct(productId, productDto);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.notFound().build();
        }

    }
    @GetMapping("/products-with-ratings")
    public ResponseEntity<List<Product>> getAllProductsWithRatings() {
        return ResponseEntity.ok(adminProductService.getAllProductsWithRatings());
    }


}

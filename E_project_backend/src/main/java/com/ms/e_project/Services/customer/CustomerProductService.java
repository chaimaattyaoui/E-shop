package com.ms.e_project.Services.customer;

import com.ms.e_project.Entities.Product;
import com.ms.e_project.dto.ProductDetailDto;
import com.ms.e_project.dto.ProductDto;

import java.util.List;

public interface CustomerProductService {

   /* List<ProductDto> getAllProducts();*/

    List<ProductDto> searchProductByTitle(String title);
    ProductDetailDto getProductDetailById(Long productId);
    List<Product> getAllProductsWithRatings();
}

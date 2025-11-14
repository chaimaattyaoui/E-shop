package com.ms.e_project.Services.admin.adminproduct;

import com.ms.e_project.Entities.Product;
import com.ms.e_project.dto.ProductDetailDto;
import com.ms.e_project.dto.ProductDto;

import java.io.IOException;
import java.util.List;

public interface AdminProductService {
    ProductDto addProduct(ProductDto productDto) throws IOException;
   /* List<ProductDto> getAllProducts();*/
   ProductDetailDto getProductDetailById(Long productId);
    List<ProductDto> getAllProductByName(String name);
    boolean deleteProduct(Long id);
    ProductDto getProductById(Long productId);
    ProductDto updateProduct(Long productId, ProductDto productDto) throws IOException;
    List<Product> getAllProductsWithRatings();


}

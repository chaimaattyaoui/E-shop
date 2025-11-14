package com.ms.e_project.Services.customer;


import com.ms.e_project.Entities.FAQ;
import com.ms.e_project.Entities.Product;
import com.ms.e_project.Entities.Review;
import com.ms.e_project.Repository.FAQRepository;
import com.ms.e_project.Repository.ProductRepository;
import com.ms.e_project.Repository.ReviewRepository;
import com.ms.e_project.dto.ProductDetailDto;
import com.ms.e_project.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerProductServiceImpl implements CustomerProductService {


    private final ProductRepository productRepository;
    private final FAQRepository faqRepository;
    private final ReviewRepository reviewRepository;

/*
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(Product::getDto).collect(Collectors.toList());

    }
    */



    public List<ProductDto> searchProductByTitle(String name) {
        List<Product> products = productRepository.findAllByNameContaining(name);
        return products.stream().map(Product::getDto).collect(Collectors.toList());

    }

    public ProductDetailDto getProductDetailById(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            List<FAQ> faqList = faqRepository.findAllByProductId(productId);
            List<Review> reviewsList = reviewRepository.findAllByProductId(productId);
            ProductDetailDto productDetailDto = new ProductDetailDto();
            productDetailDto.setProductDto(optionalProduct.get().getDto());
            productDetailDto.setFaqDtoList(faqList.stream().map(FAQ::getFAQDto).collect(Collectors.toList()));
            productDetailDto.setReviewDtoList(reviewsList.stream().map(Review::getDto).collect(Collectors.toList()));
            return productDetailDto;
        }
        return null;

    }

    public List<Product> getAllProductsWithRatings() {
        List<Product> products = productRepository.findAll(); // Fetch all products
        for (Product product : products) {
            Double averageRating = reviewRepository.findAverageRatingByProductId(product.getId());
            product.setAverageRating(averageRating != null ? averageRating : 0.0);
        }
        return products;
    }
}

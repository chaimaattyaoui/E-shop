package com.ms.e_project.Services.admin.adminproduct;

import com.ms.e_project.Entities.Category;
import com.ms.e_project.Entities.FAQ;
import com.ms.e_project.Entities.Product;
import com.ms.e_project.Entities.Review;
import com.ms.e_project.Repository.CategoryRepository;
import com.ms.e_project.Repository.FAQRepository;
import com.ms.e_project.Repository.ProductRepository;
import com.ms.e_project.Repository.ReviewRepository;
import com.ms.e_project.dto.ProductDetailDto;
import com.ms.e_project.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final Cloudinary cloudinary;
    private final ReviewRepository reviewRepository;
    private final FAQRepository faqRepository;

    public ProductDto addProduct(ProductDto productDto) throws IOException {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());

        // Upload to Cloudinary instead of saving bytes
        String imageUrl = cloudinary.uploader()
                .upload(productDto.getImg().getBytes(), ObjectUtils.emptyMap())
                .get("url")
                .toString();

        product.setImageUrl(imageUrl);

        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow();
        product.setCategory(category);

        return productRepository.save(product).getDto();
    }

/*
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(Product::getDto)
                .collect(Collectors.toList());
    }
    */


    public List<ProductDto> getAllProductByName(String name) {
        return productRepository.findAllByNameContaining(name).stream()
                .map(Product::getDto)
                .collect(Collectors.toList());
    }

    public boolean deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public ProductDto getProductById(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get().getDto();
        }else {
            return null;
        }
    }


    public ProductDto updateProduct(Long productId, ProductDto productDto) throws IOException {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        Optional<Category> optionalCategory = categoryRepository.findById(productDto.getCategoryId());
        if (optionalProduct.isPresent() && optionalCategory.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setPrice(productDto.getPrice());
            product.setCategory(optionalCategory.get());
            if (productDto.getImg() != null && !productDto.getImg().isEmpty()) {
                String imageUrl = cloudinary.uploader()
                        .upload(productDto.getImg().getBytes(), ObjectUtils.emptyMap())
                        .get("url")
                        .toString();
                product.setImageUrl(imageUrl);
            }

            return productRepository.save(product).getDto();

        }else{
            return null;
        }
    }

    public List<Product> getAllProductsWithRatings() {
        List<Product> products = productRepository.findAll(); // Fetch all products
        for (Product product : products) {
            Double averageRating = reviewRepository.findAverageRatingByProductId(product.getId());
            product.setAverageRating(averageRating != null ? averageRating : 0.0);
        }
        return products;
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


}

package com.ms.e_project.dto;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
public class ProductDto {
    private Long id;
    private String name;
    private Long price;

    private String description;

    private String imageUrl;
    private Long categoryId;
    private String categoryName;
    private MultipartFile img;
    private Long quantity;
    private Double averageRating;
}


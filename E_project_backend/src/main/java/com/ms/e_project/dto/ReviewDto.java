package com.ms.e_project.dto;


import lombok.Data;

import org.springframework.web.multipart.MultipartFile;

@Data
public class ReviewDto {
    private Long id;
    private Long rating;
    private String description;
    private String imageUrl;
    private MultipartFile img;
    private Long userId;
    private Long productId;
    private String username;
}


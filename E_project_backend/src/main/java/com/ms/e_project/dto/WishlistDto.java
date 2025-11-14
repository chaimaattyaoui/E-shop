package com.ms.e_project.dto;

import lombok.Data;

@Data
public class WishlistDto {
    private Long userId;
    private Long productId;
    private Long id;
    private String productName;
    private String productDescription;
    private String returnedImage;
    private Long price;

}

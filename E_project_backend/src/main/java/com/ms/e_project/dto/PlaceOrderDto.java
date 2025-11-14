package com.ms.e_project.dto;

import lombok.Data;

@Data
public class PlaceOrderDto {
    private Long userId;
    private String address;
    private String orderDescription;

}

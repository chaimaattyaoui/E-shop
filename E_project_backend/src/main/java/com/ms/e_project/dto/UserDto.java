package com.ms.e_project.dto;

import com.ms.e_project.Entities.UserRole;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private UserRole userRole;
}

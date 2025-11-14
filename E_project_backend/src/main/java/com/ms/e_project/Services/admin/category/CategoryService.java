package com.ms.e_project.Services.admin.category;

import com.ms.e_project.Entities.Category;
import com.ms.e_project.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    Category createcategory(CategoryDto categoryDto);
    List<Category> getAllCategories();

}

package com.example.demo.mapper;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    
    public CategoryDTO toDTO(Category category) {
        if (category == null) {
            return null;
        }
        
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setType(category.getType());
        dto.setDescription(category.getDescription());
        dto.setDateCreation(category.getDateCreation());
        return dto;
    }
    
    public Category toEntity(CategoryDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Category category = new Category();
        category.setId(dto.getId());
        category.setType(dto.getType());
        category.setDescription(dto.getDescription());
        category.setDateCreation(dto.getDateCreation());
        return category;
    }
}
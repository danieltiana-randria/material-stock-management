package com.example.demo.services.interfaces;

import com.example.demo.dto.CategoryDTO;
import java.util.List;

public interface CategoryService {
    CategoryDTO creerCategory(CategoryDTO categoryDTO);
    CategoryDTO obtenirCategoryParId(Long id);
    List<CategoryDTO> obtenirToutesCategories();
    CategoryDTO updateCategory( CategoryDTO categoryDTO);
    void supprimerCategory(Long id);
    CategoryDTO obtenirCategoryParDefaut();
    CategoryDTO obtenirCategoryParType(String type);
}
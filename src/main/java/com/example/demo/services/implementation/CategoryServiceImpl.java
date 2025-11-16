package com.example.demo.services.implementation;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.services.interfaces.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    
@Autowired private CategoryRepository categoryRepository;
@Autowired private  CategoryMapper categoryMapper;
    
    
    @Override
    public CategoryDTO creerCategory(CategoryDTO categoryDTO) {
        if (categoryDTO.getType() == null || categoryDTO.getType().trim().isEmpty()) {
throw new IllegalArgumentException("Le type de catégorie est requis");
        }
    
if (categoryRepository.existsByType(categoryDTO.getType())) {   throw new DataIntegrityViolationException("Une catégorie avec le type  existe déjà");
        }
        Category category = categoryMapper.toEntity(categoryDTO);category.setDateCreation(LocalDateTime.now());
        
Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDTO(savedCategory);
    }
    
    @Override
    public CategoryDTO obtenirCategoryParId(Long id) {
Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'ID: " + id));
        return categoryMapper.toDTO(category);
    }
    
    @Override
    public List<CategoryDTO> obtenirToutesCategories() {
        return categoryRepository.findAll().stream()  .map(categoryMapper::toDTO).collect(Collectors.toList());
    }
    
    @Override
    public CategoryDTO updateCategory( CategoryDTO categoryDTO) {
        
        if(categoryDTO.getId() == null){
            throw new RuntimeException("Id de depot requis");
        }
        Category existingCategory = categoryRepository.findById(categoryDTO.getId()).orElseThrow(() -> new RuntimeException("Catégorie non trouvée "));
        
if (categoryDTO.getType()!=null) {
if (categoryRepository.existsByType(categoryDTO.getType())) {
throw new RuntimeException("Une catégorie  types existe déjà");
}
existingCategory.setType(categoryDTO.getType());
        }
        
        if (categoryDTO.getDescription() != null) {
            existingCategory.setDescription(categoryDTO.getDescription());
        }
        
        Category updatedCategory = categoryRepository.save(existingCategory);
        return categoryMapper.toDTO(updatedCategory);
    }
    
    @Override
    public void supprimerCategory(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'ID: " + id));
        
        if (!category.getMateriels().isEmpty()) {
throw new RuntimeException("Impossible de supprimer utilisée par des matériels");
       }
        
categoryRepository.delete(category);
    }
    
    @Override
    public CategoryDTO obtenirCategoryParDefaut() {
        return categoryRepository.findDefaultCategory().map(categoryMapper::toDTO).orElseGet(this::creerCategoryParDefaut);
    }
    
    @Override
    public CategoryDTO obtenirCategoryParType(String type) {
        Category category = categoryRepository.findByType(type).orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec le type: " + type));
        return categoryMapper.toDTO(category);
    }
    
    private CategoryDTO creerCategoryParDefaut() {
        CategoryDTO defaultCategory = new CategoryDTO();
        defaultCategory.setType("defaut");
        defaultCategory.setDescription("Catégorie par défaut");
        return creerCategory(defaultCategory);
    }
}
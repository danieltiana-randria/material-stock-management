package com.example.demo.controller;

import com.example.demo.controller.config.ApiResp;
import com.example.demo.dto.CategoryDTO;
import com.example.demo.services.interfaces.CategoryService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;

    @Autowired private ApiResp reponse;

    private Map<String, String> reponse(String message){
        Map<String, String> resp = new HashMap<>();
        resp.put("message", message);
        return resp;
    }

    @PostMapping
    public ResponseEntity<?> creerCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            CategoryDTO createdCategory = categoryService.creerCategory(categoryDTO);
            return reponse.success("categorie cré", createdCategory);
        }catch(IllegalArgumentException e){
            log.warn(e.getMessage());
            return reponse.erreur(400, e.getMessage());
        }catch(DataIntegrityViolationException e){
            log.warn(e.getMessage());
            return reponse.erreur(409, e.getMessage());

        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return reponse.erreur(500, e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenirCategory(@PathVariable Long id) {
        try {
            CategoryDTO category = categoryService.obtenirCategoryParId(id);
            return reponse.success("acategories recuperés", category);
        } catch (RuntimeException e) {
            return reponse.erreur(404, e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<?> obtenirToutesCategories() {
        try {
                   List<CategoryDTO> categories = categoryService.obtenirToutesCategories();
        return reponse.success("Donnée recuperes", categories);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return reponse.erreur(500, e.getMessage());
        }
 
    }
    
    @PutMapping
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            CategoryDTO updatedCategory = categoryService.updateCategory(categoryDTO);
            return reponse.success("Categorie modifé", updatedCategory);
        } catch (IllegalArgumentException e) {
            return reponse.erreur(400, e.getMessage());
        } catch (RuntimeException e) {
            return reponse.erreur(500, e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimerCategory(@PathVariable Long id) {
        try {
            categoryService.supprimerCategory(id);
            return reponse.success("Uspprimé", null);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/default")
    public ResponseEntity<?> obtenirCategoryParDefaut() {
        try {
            CategoryDTO defaultCategory = categoryService.obtenirCategoryParDefaut();
            return reponse.success("Catagorie defaut recuperé", defaultCategory);
        } catch (RuntimeException e) {
            return reponse.erreur(404, e.getMessage());
        }
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<?> obtenirCategoryParType(@PathVariable String type) {
        try {
            CategoryDTO category = categoryService.obtenirCategoryParType(type);
            return reponse.success("Categories recuperés", category);
        } catch (RuntimeException e) {
            log.warn(e.getMessage());
            return reponse.erreur(404, e.getMessage());
        }
    }
}
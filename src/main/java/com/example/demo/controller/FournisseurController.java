package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

import com.example.demo.controller.config.ApiResp;
import com.example.demo.dto.FournisseurDTO;
import com.example.demo.model.Fournisseur;
import com.example.demo.services.implementation.FournisseurServicesImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/api/fournisseurs")
public class FournisseurController {
    
    @Autowired 
    private FournisseurServicesImpl services;  

    @Autowired private ApiResp apiResp;

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<Fournisseur> fournisseurs = services.findAll();
          return  apiResp.success("Fournisseurs recuperés", fournisseurs);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des fournisseurs", e);
            return apiResp.erreur(500, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            Fournisseur fournisseur = services.findById(id).orElseThrow(() -> new RuntimeException("Fournisseur non trouvé " ));
return  apiResp.success("Fournisseurs recuperés", fournisseur);
        }catch(NoSuchElementException e){
            return apiResp.erreur(404, e.getMessage());
        } catch (RuntimeException e) {
            log.error(e.getMessage());
          return apiResp.erreur(500, e.getMessage());
        }
    }

    @GetMapping("/adresse/{adresse}")
    public ResponseEntity<?> getByAdresse(@PathVariable String adresse) {
        try {
List<FournisseurDTO> fournisseurs = services.findByAdresse(adresse);
        return apiResp.success("PErsonnel recuperé", fournisseurs);
} catch (RuntimeException e) {
          return apiResp.erreur(500, e.getMessage());
}
    }

    @PostMapping
    public ResponseEntity<?> createFournisseur(@RequestBody FournisseurDTO fournisseurDto) {
try {
  FournisseurDTO createdFournisseur = services.createFournisseur(fournisseurDto);
     return apiResp.success("Fournsisseur cree", createdFournisseur);
        }catch(DataIntegrityViolationException e){
            log.warn(e.getMessage());
           return apiResp.erreur(409, e.getMessage());
        } catch (RuntimeException e) {
            log.error(e.getMessage());
        return apiResp.erreur(500, e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateFournisseur(@RequestBody FournisseurDTO fournisseurDto) {
        try {
            FournisseurDTO updatedFournisseur = services.updateFournisseur(fournisseurDto);
            return ResponseEntity.ok(updatedFournisseur);
        } catch (RuntimeException e) {
            log.warn(e.getMessage());
            return apiResp.erreur(500, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFournisseur(@PathVariable Long id) {
        try {
            services.deleteFournisseur(id);
return  apiResp.success("Fournisseur supprimé", null);
        }catch(NoSuchElementException e){
return apiResp.erreur(404, e.getMessage());
        } catch (RuntimeException e) {
            log.warn("Errer de suppression", e.getMessage());
return apiResp.erreur(500, e.getMessage());
        } 
    }
    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        response.put("status", "ERROR");
        return response;
    }

    private Map<String, String> createSuccessResponse(String message) {
Map<String, String> response = new HashMap<>();  response.put("message", message);
response.put("status", "SUCCESS");
        return response;
    }
}
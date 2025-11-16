package com.example.demo.controller;


import com.example.demo.controller.config.ApiResp;
import com.example.demo.dto.MaterielDTO;
import com.example.demo.model.Materiel;
import com.example.demo.services.interfaces.MaterielService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/materiels")
public class MaterielController {

    @Autowired
    private MaterielService materielService;

    @Autowired private ApiResp reponse;


    private Map<String, Object> resp(String message, Object data){
        Map<String, Object> resp= new HashMap<>();
        return resp;
        
    }
    
    private Map<String, String> resp(String message){
        Map<String, String> resp = new HashMap<>();
        resp.put("message", message);
        return resp;
    }
    
    @PostMapping
    public ResponseEntity<?> creerMateriel(@RequestBody MaterielDTO materielDTO,@RequestHeader("personnel-id") Long personnelId) {
        try {
            MaterielDTO createdMateriel = materielService.creerMateriel(materielDTO, personnelId);
            return reponse.success("MAteriel créé", createdMateriel);
        }catch(IllegalArgumentException e){
            log.warn(e.getMessage());
            return reponse.erreur(400 , e.getMessage());
        } catch(DataIntegrityViolationException e){
            log.warn(e.getMessage());
            return reponse.erreur(409, e.getMessage());
        }catch(NoSuchElementException e){
            log.warn(e.getMessage());
            return reponse.erreur(404, e.getMessage());
        }
         catch (RuntimeException e) {
            log.warn("Erreur: "+e.getMessage(), e.getMessage());
            return reponse.erreur(500, e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getMateriel(@PathVariable Long id) {
        try {
            MaterielDTO materiel = materielService.getMaterielParId(id);
            return reponse.success("MAteriel obtenu", materiel);
        } catch (NoSuchElementException e) {
            return reponse.erreur(404, "Materiel vide");
        }
    }
    
    @GetMapping
    public ResponseEntity<?> getAllMateriels() {
        try{
            List<MaterielDTO> materiels = materielService.getAllMateriels();
            return reponse.success("MAteriel obtenu", materiels);
        }catch(NoSuchElementException e){
            return reponse.erreur(404, e.getMessage());
        }
    }
    
    @PutMapping
    public ResponseEntity<?> updateMateriel(@RequestBody MaterielDTO materielDTO, @RequestHeader("personnel-id") Long personnelId) {
        
    
        try {
            MaterielDTO updatedMateriel = materielService.updateMateriel(materielDTO, personnelId);
            return reponse.success("materiel mis a jour", updatedMateriel);
        } catch (RuntimeException e) {
            return reponse.erreur(404, e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimerMateriel(@PathVariable Long id, @RequestHeader("personnel-id") Long personnelId) {
        try {
  materielService.supprimerMateriel(id, personnelId);     
 return reponse.success("Materiel supprimé", personnelId);
} catch (RuntimeException e) {
            return reponse.erreur(404, e.getMessage());
        }
    }
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> getMaterielsParCategory(@PathVariable Long categoryId) {
try {
    List<MaterielDTO> materiels = materielService.getMaterielsParCategory(categoryId);
    return reponse.success("Materiel recuperé", materiels);
  } catch (RuntimeException e) {
    return reponse.erreur(400, e.getMessage());
        }
    }
    
    @GetMapping("/stock-faible")
    public ResponseEntity<?> getMaterielsAvecStockFaible(@RequestParam(defaultValue = "5") Integer seuil) {
List<MaterielDTO> materiels = materielService.getMaterielsAvecStockFaible(seuil);
    return reponse.success("materiel recypreré", materiels);
    }
    
    @PostMapping("/{id}/mettre-a-jour-stock")
    public ResponseEntity<?> updateStockMateriel(@PathVariable Long id) {
        try {
     materielService.updateStockMateriel(id);
            return reponse.success("Stock mis ajour", null);
        } catch (RuntimeException e) {
            return reponse.erreur(404, e.getMessage());
        }
    }
}
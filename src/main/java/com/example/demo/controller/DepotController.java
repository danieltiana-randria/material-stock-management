package com.example.demo.controller;


import com.example.demo.controller.config.ApiResp;
import com.example.demo.dto.DepotDTO;
import com.example.demo.services.implementation.DepotServiceImpl;
import com.example.demo.services.interfaces.DepotService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/api/depots")
public class DepotController {
    
    @Autowired
 private  DepotService depotService;
    @Autowired ApiResp resp;
      

    
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenirDepot(@PathVariable Long id) {
        try {
         DepotDTO depot = depotService.obtenirDepotParId(id);
     return resp.success("Depot recuperé", depot);
        } catch (RuntimeException e) {
    return resp.erreur(404, e.getMessage());
        }
    }

        @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimerDepot(@PathVariable Long id,@RequestHeader("personnel-id") Long personnelId) {
        try {
    depotService.supprimerDepot(id, personnelId);
     return resp.success("Depot supprié", personnelId);
  }catch(NoSuchElementException e){
    log.warn(e.getMessage());
return resp.erreur(404, e.getMessage());
        } catch (RuntimeException e) {return resp.erreur(500, e.getMessage());
        }
    }

        @PostMapping
    public ResponseEntity<?> creerDepot(@RequestBody DepotDTO depotDTO,   @RequestHeader("personnel-id") Long personnelId) {
        try {
     DepotDTO createdDepot = depotService.creerDepot(depotDTO, personnelId);
  return resp.success("Depot créé", createdDepot);
 }catch(DataIntegrityViolationException e){
        log.error(e.getMessage());
     return resp.erreur(409, e.getMessage());
 } catch (IllegalArgumentException e) {
    return resp.erreur(409, e.getMessage());
 }catch(NoSuchElementException e){
    return resp.erreur(404, e.getMessage());
  } catch (RuntimeException e) {
    return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<DepotDTO>> obtenirTousDepots() {
  List<DepotDTO> depots = depotService.obtenirTousDepots();
 return ResponseEntity.ok(depots);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDepot(@PathVariable Long id, @RequestBody DepotDTO depotDTO,@RequestHeader("personnel-id") Long personnelId) {
        try {
DepotDTO updatedDepot = depotService.updateDepot(depotDTO, personnelId);
            return resp.success("Depot mise a jour", updatedDepot);
}catch(DataIntegrityViolationException e){
            log.warn(e.getMessage());       return resp.erreur(409, e.getMessage());
      }catch(NoSuchElementException e){
            log.warn(e.getMessage());        return resp.erreur(404, e.getMessage());
       } catch (RuntimeException e) {      log.error(e.getMessage());
  return resp.erreur(500, e.getMessage());
        }
    }
    

    
    @PostMapping("/majArticles")
    public ResponseEntity<Void> updateTotalArticles() {
        try {
            depotService.updateTotalArticlesDisponibles();
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/total-articles-disponibles")
    public ResponseEntity<Integer> obtenirTotalArticlesDisponibles() {
        try {
 Integer total = depotService.obtenirTotalArticlesDisponibles();
return ResponseEntity.ok(total);
        } catch (RuntimeException e) {
return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{id}/taux")
    public ResponseEntity<Double> obtenirTauxOccupation(@PathVariable Long id) {
        try {
Double tauxOccupation = ((DepotServiceImpl) depotService).calculerTauxOccupation(id);
     return ResponseEntity.ok(tauxOccupation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{id}/verifier-capacite")
    public ResponseEntity<Boolean> verifierCapaciteDepot(@PathVariable Long id,
                                                       @RequestParam Integer quantite) {
        try {
            Boolean capaciteSuffisante = ((DepotServiceImpl) depotService).verifierCapaciteDepot(id, quantite);
            return ResponseEntity.ok(capaciteSuffisante);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
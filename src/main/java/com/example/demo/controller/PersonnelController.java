package com.example.demo.controller;

import com.example.demo.controller.config.ApiResp;
import com.example.demo.dto.PersonnelDTO;
import com.example.demo.services.interfaces.PersonnelService;

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

@RestController
@RequestMapping("/api/personnel")
@Slf4j
public class PersonnelController {
    
    @Autowired
    private PersonnelService personnelService;

    @Autowired
    private ApiResp resp;
    


    
    @PostMapping
    public ResponseEntity<?> creerPersonnel(@RequestBody PersonnelDTO personnelDTO) {
        try {
            PersonnelDTO createdPersonnel = personnelService.creerPersonnel(personnelDTO);
            log.info("Personnel créé");
            return resp.success("personnel créé", createdPersonnel);
        } catch(IllegalArgumentException e){
            log.warn(e.getMessage());
            return resp.erreur(400, e.getMessage());
        }catch(DataIntegrityViolationException e){
            return resp.erreur(409, e.getMessage());
        }catch (RuntimeException e) {
            log.warn("Erreur de création de personnel");
            return resp.erreur(500, e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getPersonnel(@PathVariable Long id) {
        try {
            PersonnelDTO personnel = personnelService.getPersonnelId(id);
            return resp.success("PErsonen rcuperé", personnel);
        } catch (NoSuchElementException e) {
            log.warn("Erreur de l'obtension du personnel");
            return resp.erreur(404, e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<?> getTousPersonnel() {
        try {
             List<PersonnelDTO> personnelList = personnelService.getTousPersonnel();
        return resp.success("Personnel recuperé", personnelList);
        } catch(RuntimeException e){
            return resp.erreur(500, e.getMessage());
        }
    }
    
    @PutMapping
    public ResponseEntity<?> updatePersonnel( @RequestBody PersonnelDTO personnelDTO) {
        try {
            log.info("PErsonnel recuperé");
            PersonnelDTO updatedPersonnel = personnelService.updatePersonnel(personnelDTO);
            return resp.success( "PErsonnel modfié", updatedPersonnel);
        }catch(NoSuchElementException e){
            log.error( e.getMessage());
            return resp.erreur(404, e.getMessage());
        }catch(DataIntegrityViolationException d){
            log.error(d.getMessage());
            return resp.erreur(409, d.getMessage());
        } catch (RuntimeException e) {
            log.warn("Erruer de mise a jous", e);
            return resp.erreur(400, "Erreur de mise a jor"+e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimerPersonnel(@PathVariable Long id) {
        try {
            personnelService.supprimerPersonnel(id);
            return resp.success("Personnel supprimé", null);
        }catch(NoSuchElementException e){
            return resp.erreur(404, e.getMessage());

        } catch (RuntimeException e) {
            return resp.erreur(500, e.getMessage());
        }
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getPersonnelEmail(@PathVariable String email) {
        try {
            PersonnelDTO personnel = personnelService.getPersonnelEmail(email);
            return resp.success("Personnel recuperé", personnel);
        } catch (RuntimeException e) {
            return resp.erreur(500, e.getMessage());
        }
    }
    
    @GetMapping("/depatement/{depatement}")
    public ResponseEntity<?> getPersonnelDepartement(@PathVariable String Departement) {
        try {
            List<PersonnelDTO> personnelList = personnelService.getPersonnelDepartement(Departement);
        return resp.success("PErsonnel recuperé", personnelList);
        } catch (RuntimeException e) {
        return resp.erreur(500, e.getMessage());
        }
        
    }
    
    @GetMapping("/statut/{statut}")
    public ResponseEntity<?> getPersonnelStatut(@PathVariable String statut) {
        List<PersonnelDTO> personnelList = personnelService.getPersonnelStatut(statut);
        return resp.success("PErsonnel recuperé", personnelList);
    }
}
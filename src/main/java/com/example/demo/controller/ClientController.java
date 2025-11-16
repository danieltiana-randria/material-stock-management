package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

import com.example.demo.controller.config.ApiResp;
import com.example.demo.dto.ClientDTO;
import com.example.demo.services.implementation.ClientServicesImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientServicesImpl services;

    @Autowired private ApiResp reponse;

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
     List<ClientDTO> clients = services.getAll();
return reponse.success("Clients recuperés", clients);
    } catch (RuntimeException e) {
            return reponse.erreur(500, e.getMessage());
        }
    }



    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody ClientDTO ClientDTO, @RequestHeader("personnel-Id") Long PersonnelId) {
        try {
ClientDTO createdClient = services.createClient(ClientDTO, ClientDTO.getArticleId(), PersonnelId);
return reponse.success("Client crée", createdClient);
        } catch (IllegalArgumentException e) {
     log.warn("Erreur de creation: ", e.getMessage());
     return reponse.erreur(409, e.getMessage());
        } catch(NoSuchElementException e){
            log.warn (e.getMessage());
            return reponse.erreur(404, e.getMessage());

        } catch (Exception e) {
            log.error(e.getMessage());
            return reponse.erreur(500, e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateClient( @RequestBody ClientDTO ClientDTO) {
        try {
            ClientDTO updatedClient = services.updateClient( ClientDTO);
            return ResponseEntity.ok(updatedClient);
        }catch(NoSuchElementException e){
            log.warn(e.getMessage());
            return reponse.erreur(404, e.getMessage());
        } catch (RuntimeException e) {
            log.warn("Erreur lors de mise a jour: ", e.getMessage());
            return reponse.erreur(500, e.getMessage());
        } 
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        try {
            services.deleteClient(id);
            return reponse.success("Client supprimé", null);
        }catch(NoSuchElementException e){
            log.warn(e.getMessage());
            return reponse.erreur(404, e.getMessage());
        } catch (RuntimeException e) {
            log.warn("Erreur de suppression", e.getMessage());
            return reponse.erreur(500, e.getMessage());
        }
    }

        @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
    log.info("cliet recuperé");
ClientDTO client = services.findById(id).orElseThrow(() -> new RuntimeException("Client non trouvé "));
            return reponse.success("Client recuperé", client);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return reponse.erreur(500, e.getMessage());}
        
    }
}
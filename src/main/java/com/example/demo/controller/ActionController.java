package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

import com.example.demo.controller.config.ApiResp;
import com.example.demo.model.Actions;
import com.example.demo.services.implementation.ActionsServicesImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/actions")
public class ActionController {
    
    @Autowired
    private ActionsServicesImpl services;
    @Autowired private ApiResp response;
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<Actions> actions = services.findAll();
            // reponse.setMessage("Utilisateur recuperé");
            // reponse.setReponse(actions);
            // reponse.setStatus(200);
         return response.success("Actions recuperés", actions);
        } catch (Exception e) {
     log.error("Erreur de recuperation", e);
    return response.erreur(500, e.getMessage());
        }
    }

        @PostMapping
    public ResponseEntity<?> createAction(@RequestBody Actions action) {
        try {
            Actions createdAction = services.create(action);
            return response.success("Action crée", createdAction);
        }catch(NoSuchElementException e){
            log.warn(e.getMessage());
            return response.erreur(404,e.getMessage());
        } 
        catch (Exception e) {
            log.error("Erreur de creation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            Optional<Actions> action = services.findById(id);
            return response.success("Données recuperés", action);
        } catch (RuntimeException e) {
            log.warn("erreur"+e.getMessage());
          return response.erreur(500, e.getMessage());
        }
    }

    @GetMapping("/types/{types}")
    public ResponseEntity<?> getByTypesContaining(@PathVariable String types) {
        try {
            List<Actions> actions = services.findByTypes(types);
            return response.success("Donné recuperés", actions);
        } catch (Exception e) {
            log.error("Il y a une erreur", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAction(@PathVariable Long id, @RequestBody Actions action) {
        try {
          Actions updatedAction = services.update(id, action);
      return response.success("Article modifié", updatedAction);
        } catch (RuntimeException e) {
    log.warn("Erreur de mise à jour", e.getMessage());
 return  response.erreur(500, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAction(@PathVariable Long id) {
        try {
            services.delete(id);
            return ResponseEntity.ok(createSuccessResponse("Action supprimé"));
        } catch (RuntimeException e) {
            log.warn("Erreur de suppressino", id, e.getMessage());
           return response.erreur(500, e.getMessage());
        } 
    }

    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        response.put("status", "ERROR");
        return response;
    }

    private Map<String, String> createSuccessResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        response.put("status", "SUCCESS");
        return response;
    }
}
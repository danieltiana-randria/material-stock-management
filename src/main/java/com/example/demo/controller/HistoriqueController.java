package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

import com.example.demo.controller.config.ApiResp;
import com.example.demo.model.Historique;
import com.example.demo.services.implementation.HistoriqueServicesImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RestController
@RequestMapping("/api/historique")
public class HistoriqueController {
    
    @Autowired 
    private HistoriqueServicesImpl services;

    @Autowired ApiResp apiResp;

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<Historique> historiques = services.findAll();
            return ResponseEntity.ok(historiques);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération de l'historique", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/type/{types}")
    public ResponseEntity<?>getByTypes(@PathVariable String types) {
        try {
            List<Historique> histo= services.findByTypeHistoriques(types);
            log.info("Donneés recuperés");
           return apiResp.success("Données recuperés", histo);
        } catch (RuntimeException e) {
            log.warn(e.getMessage());
           return apiResp.erreur(500, e.getMessage());
        }
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            Historique historique = services.findById(id)
                    .orElseThrow(() -> new RuntimeException("Historique non trouvé avec l'ID: " + id));
            return ResponseEntity.ok(historique);
        } catch (RuntimeException e) {
            log.warn("Historique non trouvé: "+ e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Erreur de recherche"+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Erreur lors de la recherche de l'historique"));
        }
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<?> getByClient(@PathVariable Long id) {
        try {
            List<Historique> historiques = services.findByClient(id);
            if (historiques.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(createErrorResponse("Aucun historique trouvé pour ce client"));
            }
            return ResponseEntity.ok(historiques);
        } catch (Exception e) {
            log.error("Erreur recherche de client", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Erreur lors de la recherche de l'historique du client"));
        }
    }

    @GetMapping("/fournisseur/{id}")
    public ResponseEntity<?> getByFournisseur(@PathVariable Long id) {
        try {
            List<Historique> historiques = services.findByFournisseur(id);
            if (historiques.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse("Aucun historique trouvé pour ce fournisseur"));
            }
            return ResponseEntity.ok(historiques);
        } catch (Exception e) {
            log.error("Erreur lors de historique fournisseur", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Erreur lors de la recherche de l'historique du fournisseur"));
        }
    }

    @GetMapping("/depot/{id}")
    public ResponseEntity<?> getByDepot(@PathVariable Long id) {
        try {
            List<Historique> historiques = services.findByDepot(id);
            if (historiques.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Aucun historique trouvé pour ce dépôt"));
            }
            return ResponseEntity.ok(historiques);
        } catch (Exception e) {
            log.error("Erreur lors de la recherche de l'historique depot", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse("Erreur lors de la recherche de l'historique du dépôt"));
        }
    }

    @GetMapping("/dates")
    public ResponseEntity<?> getByDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        try {
            List<Historique> historiques = services.findBetweenDates(debut, fin);
            if (historiques.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Aucun historique trouvé pour cette période"));
            }
            return ResponseEntity.ok(historiques);
        } catch (Exception e) {
            log.error("Erreur lors de la recherche de l'historique entre dates", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/client")
    public ResponseEntity<?> createClientHistory(@RequestBody Historique historique) {
        try {
            services.createClientHistorique(historique);
            return ResponseEntity.status(HttpStatus.CREATED).body(createSuccessResponse("Historique client créé "));
        } catch (Exception e) {
            log.error("Erreur lors de la création de l'historique client", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/fournisseur")
    public ResponseEntity<?> createFournisseurHistory(@RequestBody Historique historique) {
        try {
            services.createFournisseurHistorique(historique);
            return ResponseEntity.status(HttpStatus.CREATED).body(createSuccessResponse("Historique fournisseur créé avec succès"));
        } catch (Exception e) {
            log.error("Erreur lors de la création de l'historique fournisseur", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHistorique(@PathVariable Long id) {
        try {
            services.deleteHistorique(id);
            return ResponseEntity.ok(createSuccessResponse("Historique supprimé avec succès"));
        } catch (RuntimeException e) {
            log.warn("Erreur lors de la suppression de l'historique ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Erreur lors de la suppression de l'historique ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse(e.getMessage()));
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
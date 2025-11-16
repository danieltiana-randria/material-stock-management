package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HistoriqueDTO {
    private Long id;
    private String typeAction;
    private String description;
    private LocalDateTime dateAction;
    private Long utilisateurId;
    private Long clientId;
    private Long fournisseurId;
    private Long depotId;
    private Long articleId;
    private String ancienneValeur;
    private String nouvelleValeur;
    private Long actionId;
}
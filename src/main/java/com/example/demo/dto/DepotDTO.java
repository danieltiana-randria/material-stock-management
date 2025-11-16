package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class DepotDTO {
    private Long id;
    private String nom;
    private String emplacement;
    private Integer capacite;
    private String description;
    private Integer totalArticlesDisponibles;
     @JsonFormat(pattern = "MM-dd-yyyy 'à' HH:mm")
    private LocalDateTime dateCreation;
     @JsonFormat(pattern = "MM-dd-yyyy 'à' HH:mm")
    private LocalDateTime dateModification;
}
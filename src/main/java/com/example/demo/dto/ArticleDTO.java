package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class ArticleDTO {
    private Long id;
    private String couleur;
    private String model;
    private String numSerie;
    private String etat;
    private String marque;
    private String status;
    private String description;
    private Long materielId;
    private List<CaracteristiqueSuplDTO> caracteristiques;
    @JsonFormat(pattern = "MM-dd-yyyy 'à' HH:mm")
    private LocalDateTime dateCreation;
     @JsonFormat(pattern = "MM-dd-yyyy 'à' HH:mm")
    private LocalDateTime dateModification;
}
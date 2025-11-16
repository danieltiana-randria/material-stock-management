package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class PersonnelDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String poste;
    private String departement;
    private String email;
    private String telephone;
    private String adresse;
    @JsonFormat(pattern = "MM-dd-yyyy")
    private LocalDateTime dateEmbauche;
    private String statut;
     @JsonFormat(pattern = "MM-dd-yyyy 'à' HH:mm")
    private LocalDateTime dateCreation;
}
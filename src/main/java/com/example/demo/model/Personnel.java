package com.example.demo.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity

@Table(name = "personnel")
public class Personnel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "article_seq")
    @SequenceGenerator(name = "article_seq", sequenceName = "article_seq", allocationSize = 1)
    private Long id;
    private String nom;
    private String prenom;
    private String poste;
    private String departement;
    private String email;
    private String telephone;
    private String adresse;
    private LocalDateTime dateEmbauche;
    private String statut;
    private LocalDateTime dateCreation;
}
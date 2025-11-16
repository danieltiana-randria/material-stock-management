package com.example.demo.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "fournisseurs")
public class Fournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fournisseur_seq")
    @SequenceGenerator(name = "fournisseur_seq", sequenceName = "fournisseur_seq", allocationSize = 1)
    private Long id;
    private String nom;
    private String adresse;
    private String telephone;
    private String email;
    private String contactPerson;
    private LocalDateTime dateCreation;
}
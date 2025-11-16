package com.example.demo.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_seq")
    @SequenceGenerator(name = "client_seq", sequenceName = "client_seq", allocationSize = 1)
    private Long id;
    
    private String nom;
    private String prenom;
    private String adresse;
    private String telephone;
    private String email;
    
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
}
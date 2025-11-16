package com.example.demo.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "depots")
public class Depot {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "depot_seq")
    @SequenceGenerator(name = "depot_seq", sequenceName = "depot_seq", allocationSize = 1)
    private Long id;
    
    private String nom;
    private String emplacement;
    private Integer capacite;
    private String description;
    
    @Column(name = "total_articles_disponibles")
    private Integer totalArticlesDisponibles;
    
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    
    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
        if (totalArticlesDisponibles == null) {
            totalArticlesDisponibles = 0;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        dateModification = LocalDateTime.now();
    }
}
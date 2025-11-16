package com.example.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "materiels")
public class Materiel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "materiel_seq")
    @SequenceGenerator(name = "materiel_seq", sequenceName = "materiel_seq", allocationSize = 1)
    private Long id;
    
    private String nom;
    private Integer quteStock;
    private String etatStock;
    
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "materiel_categories",
        joinColumns = @JoinColumn(name = "materiel_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;

    @JsonIgnore
    @OneToMany(mappedBy = "materiel")
    private List<Articles> articles;
    
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;

        @Override
    public String toString() {
        return String.format("Materiel{id=%d, nom='%s', quteStock=%d, etatStock='%s'}", 
            id, nom, quteStock, etatStock);
    }
}
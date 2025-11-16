package com.example.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "caracteristique_supl")
public class CaracteristiqueSupl {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "caracteristique_supl_seq")
    @SequenceGenerator(name = "article_seq", sequenceName = "article_seq", allocationSize = 1)
    @JsonIgnore
    private Long id;
    
    private String nom;
    private String valeur;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Articles articles;

        
    @Override
    public String toString() {
        return "CaracteristiqueSupl{" +
            "id=" + id +
            ", nom='" + nom + '\'' +
            ", valeur='" + valeur + '\'' +
            '}'; 
    }
}
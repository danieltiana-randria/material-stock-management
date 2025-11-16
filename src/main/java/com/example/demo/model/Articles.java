package com.example.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "articles")
public class Articles {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "article_seq")
    @SequenceGenerator(name = "article_seq", sequenceName = "article_seq", allocationSize = 1)
    private Long id;
    
    private String couleur;
    private String model;
    private String numSerie;
    private String etat;
    private String marque;
    private String status;
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "materiel_id")
    @JsonIgnore
    private Materiel materiel;
    
    @OneToMany(mappedBy = "articles", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CaracteristiqueSupl> caracteristiques;
    
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;

        public void addCaracteristique(CaracteristiqueSupl caracteristique) {
        caracteristiques.add(caracteristique);
        caracteristique.setArticles(this);
    }
    
    public void removeCaracteristique(CaracteristiqueSupl caracteristique) {
        caracteristiques.remove(caracteristique);
        caracteristique.setArticles(null);
    }

        @Override
    public String toString() {
        return "Articles{" +
            "id=" + id +
            ", marque='" + marque + '\'' +
            ", model='" + model + '\'' +
            ", numSerie='" + numSerie + '\'' +
            '}'; 

            
    }
}
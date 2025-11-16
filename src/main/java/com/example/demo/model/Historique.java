package com.example.demo.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "historiques")
public class Historique {
    @Id
      @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "article_seq")
    @SequenceGenerator(name = "article_seq", sequenceName = "article_seq", allocationSize = 1)
    private Long id;
    
    private String typeAction;
    private String description;
    private LocalDateTime dateAction;
    
    private Long utilisateurId;
    private Long clientId;
    private Long fournisseurId;
    private Long depotId;
    private Long articleId;
    
    @Column(length = 1000)
    private String ancienneValeur;
    @Column(length = 1000)
    private String nouvelleValeur;
    
    @ManyToOne
    @JoinColumn(name = "action_id")
    private Actions action;
}
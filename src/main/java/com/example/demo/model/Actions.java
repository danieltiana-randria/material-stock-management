package com.example.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "actions")
public class Actions {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "action_seq")
    @SequenceGenerator(name = "action_seq", sequenceName = "action_seq", allocationSize = 1)
    private Long id;
    
    private String types;
    
    @OneToMany(mappedBy = "action")
    private List<Historique> historiques;
    
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;

}
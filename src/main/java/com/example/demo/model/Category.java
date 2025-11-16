package com.example.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "CATEGORIES")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categories_seq")
    @SequenceGenerator(name = "categories_seq", sequenceName = "categories_seq", allocationSize = 1)
    private Long id;
    
    @Column(name = "TYPE", length = 100, unique = true)
    private String type;
    
    @Column(name = "DESCRIPTION", length = 500)
    private String description;
    
    @Column(name = "DATE_CREATION")
    private LocalDateTime dateCreation;
    
    @JsonIgnoreProperties("categories")
    @ManyToMany(mappedBy = "categories", fetch = FetchType.EAGER)
    private Set<Materiel> materiels;
    
    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
    }
    

    @Override
    public String toString() {
        return String.format("Category{id=%d, type='%s', description='%s'}", 
            id, type, description != null ? description : "");
    }
}
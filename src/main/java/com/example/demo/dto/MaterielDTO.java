package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class MaterielDTO {
    private Long id;
    private String nom;
    private Integer quantiteStock;
    private String etatStock;
    private Set<Long> categoriesIds;
     @JsonFormat(pattern = "MM-dd-yyyy 'à' HH:mm")
    private LocalDateTime dateCreation;
     @JsonFormat(pattern = "MM-dd-yyyy 'à' HH:mm")
    private LocalDateTime dateModification;
}
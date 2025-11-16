package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class CaracteristiqueSuplDTO {
    private Long id;
    private String nom;
    private String valeur;
    // private Long articleId;
}
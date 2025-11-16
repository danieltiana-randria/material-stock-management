package com.example.demo.dto;

import lombok.Data;

@Data
public class FournisseurDTO {
    private Long id;
    private String nom;
    private String adresse;
    private String telephone;
    private String email;
    private String contactPerson;
}

package com.example.demo.services.interfaces;

import com.example.demo.dto.FournisseurDTO;
import com.example.demo.model.Fournisseur;
import java.util.List;
import java.util.Optional;

public interface FournisseurServices {
    List<Fournisseur> findAll();
    List<FournisseurDTO> findByAdresse(String adresse);
    Optional<Fournisseur> findById(Long id);
    FournisseurDTO createFournisseur(FournisseurDTO fournisseurDto);
    FournisseurDTO updateFournisseur(FournisseurDTO fournisseurDto);
    void deleteFournisseur(Long id);
}
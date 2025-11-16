package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Fournisseur;

import java.util.List;
import java.util.Optional;


@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, Long>{
    Fournisseur save(String fournisseur);
    List<Fournisseur> findByAdresse(String adresse);
    List<Fournisseur> findByAdresseContaining(String adresse);
    Fournisseur findByEmail(String email);
    Optional<Fournisseur> findById(Long id);
    Optional<Fournisseur> findByNomAndAdresse(String nom, String adresse);
    boolean existsByNomAndAdresseAndEmail(String nom, String adresse, String email);
    boolean existsByEmail(String email);
    void removeById(Long id);
    
}

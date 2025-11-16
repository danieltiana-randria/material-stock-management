package com.example.demo.repository;

import com.example.demo.model.Materiel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MaterielRepository extends JpaRepository<Materiel, Long> {
    Optional<Materiel> findByNom(String nom);
    boolean existsByNom(String nom);
    List<Materiel> findByQuteStockGreaterThan(Integer quantite);
    List<Materiel> findByEtatStock(String etatStock);
    
    @Query("SELECT m FROM Materiel m JOIN m.categories c WHERE c.id = :categoryId")
    List<Materiel> findByCategoryId(@Param("categoryId") Long categoryId);
    
    @Query("SELECT COUNT(a) FROM Articles a WHERE a.materiel.id = :materielId AND a.status = 'DISPONIBLE'")
    Long countArticlesDisponiblesByMaterielId(@Param("materielId") Long materielId);
}
package com.example.demo.repository;

import com.example.demo.model.Articles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Articles, Long> {
    Optional<Articles> findByNumSerie(String numSerie);
    List<Articles> findByMaterielId(Long materielId);
    List<Articles> findByStatus(String status);
    List<Articles> findByMaterielIdAndStatus(Long materielId, String status);
    Optional<Articles> findById(Long id) ;
    @Query("SELECT COUNT(a) FROM Articles a WHERE a.materiel.id = :materielId AND a.status = 'DISPONIBLE'")
    Integer countByMaterielIdAndStatusDisponible(@Param("materielId") Long materielId);
    @Query("SELECT a FROM Articles a WHERE a.materiel.id = :materielId")
    List<Articles> findAllByMaterielId(@Param("materielId") Long materielId);
    boolean existsByNumSerie(String numSerie);
}
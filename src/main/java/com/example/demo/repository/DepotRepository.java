package com.example.demo.repository;

import com.example.demo.model.Depot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DepotRepository extends JpaRepository<Depot, Long> {
    Optional<Depot> findByNom(String nom);
    
    @Query("SELECT COUNT(a) FROM Articles a WHERE a.status = 'DISPONIBLE'")
    Integer countTotalArticlesDisponibles();
}
package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Historique;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Repository
public interface HistoriqueRepository extends JpaRepository<Historique, Long>{
    Historique save(String historique);

    List <Historique> findByActionId(Long actionsId);

    List<Historique> findAll();

    List<Historique> findByClientId(Long clientId);

    List<Historique> findByDepotId(Long Depotid);

    List<Historique> findByFournisseurId(Long id);

    List<Historique> findByDateActionBetween(LocalDateTime start, LocalDateTime end);

  List<Historique> findByTypeActionContainingIgnoreCase(String typeAction);


    

}

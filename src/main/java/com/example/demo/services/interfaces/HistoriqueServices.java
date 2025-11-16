package com.example.demo.services.interfaces;

import com.example.demo.model.Historique;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HistoriqueServices {
    List<Historique> findAll();
    List<Historique> findByClient(Long clientId);
    List<Historique> findByFournisseur(Long fournisseurId);
    List<Historique> findByDepot(Long depotId);
    List<Historique> findBetweenDates(LocalDate debut, LocalDate fin);
    void createClientHistorique(Historique historique);
    void createFournisseurHistorique(Historique historique);
    void createActionHistorique(String typeAction, String description, Long utilisateurId,Long clientId, Long fournisseurId, Long depotId, Long articleId,String ancienneValeur, String nouvelleValeur);
    Optional<Historique> findById(Long id);
    void deleteHistorique(Long id);
    List<Historique> findByTypeHistoriques(String type);
}
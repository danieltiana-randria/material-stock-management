package com.example.demo.services.implementation;

import com.example.demo.model.Historique;
import com.example.demo.repository.HistoriqueRepository;
import com.example.demo.services.interfaces.HistoriqueServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class HistoriqueServicesImpl implements HistoriqueServices {

    @Autowired
    private HistoriqueRepository historiqueRepository;

    @Override
    public List<Historique> findAll() {
        try {
            return historiqueRepository.findAll();
        } catch (RuntimeException e) {
            log.error("Erreur lors de la récupération de l'historique", e.getMessage());
            throw new RuntimeException("Erreur la récupération historique");
        }
    }

    @Override
    public List<Historique> findByClient(Long clientId) {
        try {
            return historiqueRepository.findByClientId(clientId);
        } catch (Exception e) {
            log.error("erreur recherche client", e.getMessage());
            throw new RuntimeException("Erreur lors de la recherche de l'historique du client");
        }
    }

    @Override
    public List<Historique> findByFournisseur(Long fournisseurId) {
        try {
            return historiqueRepository.findByFournisseurId(fournisseurId);
        } catch (Exception e) {
            log.error("Erreur lors de la recherche de l'historique du fournisseur ID: {}", fournisseurId, e);
            throw new RuntimeException("Erreur lors de la recherche de l'historique du fournisseur");
        }
    }

    @Override
    public List<Historique> findByDepot(Long depotId) {
        try {
            return historiqueRepository.findByDepotId(depotId);
        } catch (Exception e) {
            log.error("Erreur lors de la recherche de l'historique du dépôt ID: {}", depotId, e);
            throw new RuntimeException("Erreur lors de la recherche de l'historique du dépôt");
        }
    }

    @Override
    public List<Historique> findBetweenDates(LocalDate debut, LocalDate fin) {
        try {
            LocalDateTime start = debut.atStartOfDay();
            LocalDateTime end = fin.atTime(23, 59, 59);
            return historiqueRepository.findByDateActionBetween(start, end);
        } catch (Exception e) {
            log.error("Erreur lors de la recherche de l'historique entre dates", e);
            throw new RuntimeException("Erreur lors de la recherche de l'historique entre dates");
        }
    }

    @Override
    @Transactional
    public void createClientHistorique(Historique historique) {
        try {
            historique.setDateAction(LocalDateTime.now());
            historiqueRepository.save(historique);
            log.info("Historique client créé avec succès");
        } catch (Exception e) {
            log.error("Erreur lors de la création de l'historique client", e);
            throw new RuntimeException("Erreur lors de la création de l'historique client");
        }
    }

    @Override
    @Transactional
    public void createFournisseurHistorique(Historique historique) {
        try {
            historique.setDateAction(LocalDateTime.now());
            historiqueRepository.save(historique);
            log.info("Historique fournisseur créé avec succès");
        } catch (Exception e) {
            log.error("Erreur lors de la création de l'historique fournisseur", e);
            throw new RuntimeException("Erreur lors de la création de l'historique fournisseur");
        }
    }

    @Override
    @Transactional
    public void createActionHistorique(String typeAction, String description, Long utilisateurId, Long clientId, Long fournisseurId, Long depotId, Long articleId,String ancienneValeur, String nouvelleValeur) 
        {
        try {
            Historique historique = new Historique();
            historique.setTypeAction(typeAction);
            historique.setDescription(description);
            historique.setDateAction(LocalDateTime.now());
            historique.setUtilisateurId(utilisateurId);
            historique.setClientId(clientId);
            historique.setFournisseurId(fournisseurId);
            historique.setDepotId(depotId);
            historique.setArticleId(articleId);
            historique.setAncienneValeur(ancienneValeur);
            historique.setNouvelleValeur(nouvelleValeur);
            historiqueRepository.save(historique);
            log.info("Action historique créée: {}", typeAction);
        } catch (Exception e) {
            log.error("Erreur lors de la création de l'action historique", e);
            throw new RuntimeException("Erreur lors de la création de l'action historique");
        }
    }

    @Override
    public Optional<Historique> findById(Long id) {
        try {
            return historiqueRepository.findById(id);
        } catch (Exception e) {
            log.error("Erreur lors de la recherche de l'historique ID: {}", id, e);
            throw new RuntimeException("Erreur lors de la recherche de l'historique");
        }
    }

    @Override
    @Transactional
    public void deleteHistorique(Long id) {
        try {
            if (!historiqueRepository.existsById(id)) {
                throw new RuntimeException("Historique non trouvé avec l'ID: " + id);
            }
            historiqueRepository.deleteById(id);
            log.info("Historique supprimé avec succès ID: {}", id);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erreur lors de la suppression de l'historique ID: {}", id, e);
            throw new RuntimeException("Erreur lors de la suppression de l'historique");
        }
    }

    @Override
    public List<Historique> findByTypeHistoriques(String type) {
        try {
            return historiqueRepository.findByTypeActionContainingIgnoreCase(type);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
package com.example.demo.services.implementation;

import com.example.demo.dto.DepotDTO;
import com.example.demo.mapper.DepotMapper;
import com.example.demo.model.Depot;
import com.example.demo.model.Historique;
import com.example.demo.model.Personnel;
import com.example.demo.repository.DepotRepository;
import com.example.demo.repository.HistoriqueRepository;
import com.example.demo.repository.PersonnelRepository;
import com.example.demo.services.interfaces.DepotService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class DepotServiceImpl implements DepotService {
    
    @Autowired private  DepotRepository depotRepository;
    @Autowired private  PersonnelRepository personnelRepository;
    @Autowired private  HistoriqueRepository historiqueRepository;
    @Autowired private  DepotMapper depotMapper;
    

    
    @Override
    public DepotDTO creerDepot(DepotDTO depotDTO, Long personnelId) {
        Personnel personnel = personnelRepository.findById(personnelId).orElseThrow(() -> new NoSuchElementException("Id de personnel inexistant;"));
        
   if (depotDTO.getNom() == null || depotDTO.getNom().trim().isEmpty()) {
 throw new IllegalArgumentException(" nom du dépôt est requis");
        }
        
 depotRepository.findByNom(depotDTO.getNom())  .ifPresent(existing -> {throw new DataIntegrityViolationException("nom depot existe déjà"); });
    depotDTO.setDateCreation(LocalDateTime.now());
    Depot depot = depotMapper.toEntity(depotDTO);
     depot.setDateCreation(LocalDateTime.now());
depot.setDateModification(LocalDateTime.now());
Integer dispo = depotRepository.countTotalArticlesDisponibles();
    depot.setTotalArticlesDisponibles(dispo);
          Depot savedDepot = depotRepository.save(depot);
   updateTotalArticlesDisponibles();
enregistrerHistorique("CREATION_DEPOT",  "Création du dépôt: " + savedDepot.getNom(), personnelId, null, null, savedDepot.getId(), null, null, savedDepot.toString());
        
        return depotMapper.toDTO(savedDepot);
    }
    
    @Override
    public DepotDTO obtenirDepotParId(Long id) {
     Depot depot = depotRepository.findById(id).orElseThrow(() -> new RuntimeException("Dépôt non trouvé avec cette " )); return depotMapper.toDTO(depot);
    }
    
    @Override
    public List<DepotDTO> obtenirTousDepots() {
        try {
            return depotRepository.findAll().stream().map(depotMapper::toDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("erreur : "+ e.getMessage());
        }
    
    }
    
    @Override
    public DepotDTO updateDepot( DepotDTO depotDTO, Long personnelId) {

        if(depotDTO.getId() == null){
            throw new RuntimeException("Id de depot requis");
        }
        try {
            
    
  Personnel personnel = personnelRepository.findById(personnelId).orElseThrow(() -> new RuntimeException("Personne inexiste avec l'iD"));
  Depot existingDepot = depotRepository.findById(depotDTO.getId()) .orElseThrow(() -> new RuntimeException("Dépôt non trouvé avec cet id " ));   
  String ancienneValeur = existingDepot.toString();
 if (depotDTO.getNom() != null && !depotDTO.getNom().equals(existingDepot.getNom())) {
depotRepository.findByNom(depotDTO.getNom()).ifPresent(existing -> {if (!existing.getId().equals(depotDTO.getId())) {throw new DataIntegrityViolationException("dépôt avec le nom  existe");}
});existingDepot.setNom(depotDTO.getNom());
        }
                if (depotDTO.getEmplacement() != null) {
     existingDepot.setEmplacement(depotDTO.getEmplacement());
        }
        
    if (depotDTO.getCapacite() != null) {
 existingDepot.setCapacite(depotDTO.getCapacite());
        }
        
        if (depotDTO.getDescription() != null) {
            existingDepot.setDescription(depotDTO.getDescription());
        }
Integer dispo = depotRepository.countTotalArticlesDisponibles();
existingDepot.setTotalArticlesDisponibles(dispo);
depotDTO.setDateModification(LocalDateTime.now());
existingDepot.setDateModification(LocalDateTime.now());
    
        Depot updatedDepot = depotRepository.save(existingDepot);       

 enregistrerHistorique("MODIFICATION_DEPOT", 
            "Modification du dépôt: " + updatedDepot.getNom(),
personnelId, null, null, updatedDepot.getId(), null,
    ancienneValeur, updatedDepot.toString());
    return depotMapper.toDTO(updatedDepot);
            } 
catch (Exception e) {
            throw new RuntimeException("Erreur de mise a jour"+e.getMessage());
        }
    }
    
    @Override
    public void supprimerDepot(Long id, Long personnelId) {
        Personnel personnel = personnelRepository.findById(personnelId).orElseThrow(() -> new NoSuchElementException("Personne inexiste avec cet iD"));
        
        Depot depot = depotRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Dépôt non trouvé avec ce id"));
        
        String ancienneValeur = depot.toString();
        
        depotRepository.delete(depot);
     
        updateTotalArticlesDisponibles();

        enregistrerHistorique("SUPPRESSION_DEPOT", 
            "Suppression du dépôt: " + depot.getNom(),
            personnelId, null, null, depot.getId(), null,
            ancienneValeur, null);
    }
    
    @Override
    public void updateTotalArticlesDisponibles() {
        Integer total = depotRepository.countTotalArticlesDisponibles();
        System.out.println("Total des articles disponibles mis à jour: " + total);
    }
    
    @Override
    public Integer obtenirTotalArticlesDisponibles() {
        return depotRepository.countTotalArticlesDisponibles();
    }

    public Double calculerTauxOccupation(Long depotId) {
        Depot depot = depotRepository.findById(depotId)
            .orElseThrow(() -> new RuntimeException("Dépôt non trouvé avec cet id"));
        
        if (depot.getCapacite() == null || depot.getCapacite() == 0) {
            return 0.0;
        }
    
        Integer totalArticles = obtenirTotalArticlesDisponibles();
        return (double) totalArticles / depot.getCapacite();
    }
    
    public boolean verifierCapaciteDepot(Long depotId, Integer quantiteAjouter) {
        Depot depot = depotRepository.findById(depotId)
            .orElseThrow(() -> new RuntimeException("Dépôt non trouvé avec cet id"));
        
        if (depot.getCapacite() == null) {
            return true;
        }
        
        Integer articlesActuels = obtenirTotalArticlesDisponibles(); 
        return (articlesActuels + quantiteAjouter) <= depot.getCapacite();
    }
    
    private void enregistrerHistorique(String typeAction, String description, Long utilisateurId, Long clientId, Long fournisseurId, Long depotId,  Long articleId, String ancienneValeur, String nouvelleValeur) {
        
        Historique historique = new Historique();
 historique.setTypeAction(typeAction);
 historique.setDescription(description);
historique.setDateAction(LocalDateTime.now());
historique.setUtilisateurId(utilisateurId);
historique.setClientId(clientId); historique.setFournisseurId(fournisseurId);
    historique.setDepotId(depotId);
     historique.setArticleId(articleId);
historique.setAncienneValeur(ancienneValeur);
        historique.setNouvelleValeur(nouvelleValeur);
        
        historiqueRepository.save(historique);
    }
}
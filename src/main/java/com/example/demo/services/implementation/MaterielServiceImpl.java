package com.example.demo.services.implementation;

import com.example.demo.dto.MaterielDTO;
import com.example.demo.mapper.MaterielMapper;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.services.interfaces.MaterielService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MaterielServiceImpl implements MaterielService {
    
@Autowired 
private  MaterielRepository materielRepository;
@Autowired 
private CategoryRepository categoryRepository;
@Autowired private  ArticleRepository articleRepository;
@Autowired HistoriqueRepository historiqueRepository;
@Autowired
private  PersonnelRepository personnelRepository;
@Autowired 
private  MaterielMapper materielMapper;
@Override

@Transactional
public MaterielDTO creerMateriel(MaterielDTO materielDTO, Long personnelId) {
    
    if (!personnelRepository.existsById(personnelId)) { 
        throw new NoSuchElementException("Personnel introuvable");
    }
 
    if (materielDTO.getNom() == null || materielDTO.getNom().trim().isEmpty()) {
        throw new IllegalArgumentException("Nom du matériel requis");
    }

    if (materielRepository.existsByNom(materielDTO.getNom())) { 
        log.warn("Matériel existant");
        throw new DataIntegrityViolationException("Matériel avec ce nom existe déjà");
    }
    

    if (materielDTO.getCategoriesIds() == null || materielDTO.getCategoriesIds().isEmpty()) {
        throw new IllegalArgumentException("Au moins une catégorie est requise");
    }

    List<Category> categoriesList = categoryRepository.findAllById(materielDTO.getCategoriesIds());
    
    if (categoriesList.size() != materielDTO.getCategoriesIds().size()) {
Set<Long> existeIds = categoriesList.stream().map(Category::getId) .collect(Collectors.toSet());        
List<Long> missingIds = materielDTO.getCategoriesIds().stream().filter(id -> !existeIds.contains(id)).collect(Collectors.toList());
            
        throw new NoSuchElementException("Catégories non trouvées: " + missingIds);
    }

     

    Materiel materiel = new Materiel(); 
    materiel.setNom(materielDTO.getNom());
    materiel.setDateCreation(LocalDateTime.now());
    materiel.setDateModification(LocalDateTime.now());
    materiel.setQuteStock(0);
    materiel.setEtatStock("VIDE");
   materiel.setCategories(new HashSet<>(categoriesList));

    try {
        Materiel savedMateriel = materielRepository.save(materiel); 
        
        enregistrerHistorique("CREATION_MATERIEL", 
            "Création du matériel: " + savedMateriel.getNom(),
            personnelId, null, null, null, savedMateriel.getId(),  
            null, savedMateriel.getNom()
        );
        
        return materielMapper.toDTO(savedMateriel);

    } catch (DataIntegrityViolationException e) {
        throw new DataIntegrityViolationException("Erreur: matériel déjà présent");
    } catch (RuntimeException e) {
        throw new RuntimeException("Erreur de création: " + e.getMessage());
    }
}

    
    @Override
    public MaterielDTO getMaterielParId(Long id) {
        Materiel materiel = materielRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Matériel non trouvé "));
        return materielMapper.toDTO(materiel);
    }
    
    @Override
    public List<MaterielDTO> getAllMateriels() {
    List<MaterielDTO> mat = materielRepository.findAll() .stream() .map(materielMapper::toDTO) .collect(Collectors.toList());
            if(mat.isEmpty()){
    throw new NoSuchElementException("Aucun materiel");
            }
        return mat;
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public MaterielDTO updateMateriel(MaterielDTO materielDTO, Long personnelId) {

        if(materielDTO.getId() == null){
            throw new RuntimeException("L'id requis");
        }
        
        Personnel personnel = personnelRepository.findById(personnelId).orElseThrow(() -> new RuntimeException("Personnel non troubé "));

       Materiel existingMateriel = materielRepository.findById(materielDTO.getId()).orElseThrow(() -> new RuntimeException("Matériel non trouvé "));
        
        String ancienneValeur = existingMateriel.toString();

        if (materielDTO.getNom() != null) {
     existingMateriel.setNom(materielDTO.getNom());
        }
        
        if (materielDTO.getEtatStock() != null) {
            existingMateriel.setEtatStock(materielDTO.getEtatStock());
        }

if(materielDTO.getCategoriesIds() != null){
                List<Category> categoriesList = categoryRepository.findAllById(materielDTO.getCategoriesIds());
    
    if (categoriesList.size() != materielDTO.getCategoriesIds().size()) {
Set<Long> existeIds = categoriesList.stream().map(Category::getId) .collect(Collectors.toSet());        
List<Long> missingIds = materielDTO.getCategoriesIds().stream().filter(id -> !existeIds.contains(id)).collect(Collectors.toList());
            
        throw new NoSuchElementException("Catégories non trouvées: " + missingIds);
    }
    existingMateriel.setCategories(new HashSet<>(categoriesList));
        }



         existingMateriel.setDateModification(LocalDateTime.now());

         try {
            Materiel updatedMateriel = materielRepository.save(existingMateriel);
        
 enregistrerHistorique("MODIFICATION_MATERIEL",  "Modification du matériel: " + updatedMateriel.getNom(),
 personnelId, null, null, null, updatedMateriel.getId(),
    ancienneValeur, updatedMateriel.toString());
        
        return materielMapper.toDTO(updatedMateriel);
         } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
         }
        

    }


    public void supCateg(MaterielDTO materiel){
        Materiel mat = new Materiel();
       List <Category> verif = categoryRepository.findAllById(materiel.getCategoriesIds());
         mat.setCategories(new HashSet<>(verif));
        // if(materiel.getCategoriesIds().size() )
    }

    
    @Override
    public void supprimerMateriel(Long id, Long personnelId) {
        Personnel personnel = personnelRepository.findById(personnelId) .orElseThrow(() -> new RuntimeException("Personnel non trouvé"));
        
        Materiel materiel = materielRepository.findById(id) .orElseThrow(() -> new RuntimeException("Matériel non trouvé "));
        
  
        List<Articles> articles = articleRepository.findByMaterielId(id);
        if (!articles.isEmpty()) {
            throw new RuntimeException("Erreur supperssion: raison articles present dans ce materiel");
        }
        
        String ancienneValeur = materiel.toString();
        materielRepository.delete(materiel);
        
    
        enregistrerHistorique("SUPPRESSION_MATERIEL", 
            "Suppression du matériel: " + materiel.getNom(),
            personnelId, null, null, null, null,
            ancienneValeur, null);
    }
    
    @Override
    public void updateStockMateriel(Long materielId) {
        Materiel materiel = materielRepository.findById(materielId)
            .orElseThrow(() -> new RuntimeException("Matériel non trouvé avec l'ID: " + materielId));
        
        Integer quantiteDisponible = articleRepository.countByMaterielIdAndStatusDisponible(materielId);
        materiel.setQuteStock(quantiteDisponible);
         
        if (quantiteDisponible == 0) {
            materiel.setEtatStock("VIDE");
        } else if (quantiteDisponible < 5) {
            materiel.setEtatStock("FAIBLE");
        } else if (quantiteDisponible < 10) {
            materiel.setEtatStock("MOYEN");
        } else {
            materiel.setEtatStock("BON");
        }
        
        materiel.setDateModification(LocalDateTime.now());
        materielRepository.save(materiel);
    }
    
    @Override
    public List<MaterielDTO> getMaterielsAvecStockFaible(Integer seuil) {
        return materielRepository.findByQuteStockGreaterThan(seuil).stream().map(materielMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<MaterielDTO> getMaterielsParCategory(Long categoryId) {
   
        List<MaterielDTO> materiel = materielRepository.findByCategoryId(categoryId).stream().map(materielMapper::toDTO).collect(Collectors.toList());
if(materiel.isEmpty()){
   throw new RuntimeException("Aucun materiel disponible"); 
}
            return materiel;
    }
    
    
    private void enregistrerHistorique(String typeAction, String description, Long utilisateurId, Long clientId, Long fournisseurId, Long depotId, Long articleId, String ancienneValeur,  String nouvelleValeur) {
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
    } catch (Exception e) {
        throw new RuntimeException(e.getMessage());
    }

    }
}
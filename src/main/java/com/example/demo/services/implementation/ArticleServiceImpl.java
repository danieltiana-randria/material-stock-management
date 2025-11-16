package com.example.demo.services.implementation;


import com.example.demo.dto.ArticleDTO;
import com.example.demo.mapper.ArticleMapper;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.services.interfaces.ArticleService;
import com.example.demo.services.interfaces.DepotService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {
    
    @Autowired
     private  ArticleRepository articleRepository;
 @Autowired
  private  MaterielRepository materielRepository;
@Autowired
 private  PersonnelRepository personnelRepository;
    @Autowired
     private  HistoriqueRepository historiqueRepository;
    @Autowired
     private  ArticleMapper articleMapper;
    @Autowired 
    private DepotService DepotService;

    @Autowired CaracteristiquesRepository caractRepository;

    @Autowired ClientRepository clientRepository;

    

@Transactional(rollbackFor = Exception.class)
    @Override
    public ArticleDTO creerArticle(ArticleDTO articleDTO, Long personnelId) {
        Personnel personnel = personnelRepository.findById(personnelId).orElseThrow(() -> new IllegalArgumentException("Personnel non trouvé " ));
        
        if (articleDTO.getMaterielId() == null) {
            throw new IllegalArgumentException("L'ID du matériel requis");
        }
        
        Materiel materiel = materielRepository.findById(articleDTO.getMaterielId()).orElseThrow(() -> new NoSuchElementException("Matériel non trouvé avec id "));
        
        if (articleDTO.getNumSerie() != null) {
            articleRepository.findByNumSerie(articleDTO.getNumSerie()).ifPresent(existing -> {
 throw new DataIntegrityViolationException("Un article avec le numéro de série existe"); });
        }
        
        Articles article = articleMapper.toEntity(articleDTO);
        article.setMateriel(materiel);
        article.setDateCreation(LocalDateTime.now());
        article.setDateModification(LocalDateTime.now());
        
        if (article.getStatus() == null) {
            article.setStatus("DISPONIBLE");
        }

        if (articleDTO.getCaracteristiques() != null) {
            var caracteristiques = articleDTO.getCaracteristiques().stream().map(dto -> {
 CaracteristiqueSupl caracteristique = new CaracteristiqueSupl();
 
      if(dto.getNom() == null && dto.getValeur()==null){
             throw new IllegalArgumentException("Le nom categorie et valeur obligatoires");
                    }
            caracteristique.setNom(dto.getNom());
            caracteristique.setValeur(dto.getValeur());
            caracteristique.setArticles(article);
                    return caracteristique;
                }).collect(Collectors.toList());
            article.setCaracteristiques(caracteristiques);
        }
        
        Articles savedArticle = articleRepository.save(article);       
  
    updateStockMateriel(materiel.getId());

        enregistrerHistorique("CREATION_ARTICLE", 
            "Création de l'article: " + savedArticle.getMarque() + " " + savedArticle.getModel(),
            personnelId, null, null, null, savedArticle.getId(),
            null, savedArticle.toString());
        
        return articleMapper.toDTO(savedArticle);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ArticleDTO getArticleParId(Long id) {
        Articles article = articleRepository.findById(id).orElseThrow(() -> new RuntimeException("Article non trouvé  "));
        return articleMapper.toDTO(article);
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<ArticleDTO> getAllArticles() {
        try {
              return articleRepository.findAll().stream().map(articleMapper::toDTO).collect(Collectors.toList());
        } catch (Exception e) {
           throw new RuntimeException(e.getMessage());
        }
      
    }
    
  @Transactional(rollbackFor = Exception.class)
    @Override
    public ArticleDTO updateArticle(ArticleDTO articleDTO, Long personnelId) {      
        if(articleDTO.getId() == null){
            throw new IllegalArgumentException("L'id article requis");
        }  
        Personnel personnel = personnelRepository.findById(personnelId).orElseThrow(() ->new NoSuchElementException("Personnel non trouvé  "));
        
    Articles existingArticle = articleRepository.findById(articleDTO.getId()).orElseThrow(() -> new NoSuchElementException("Article non trouvé"));
    
        
if (articleDTO.getCouleur() != null) {
            existingArticle.setCouleur(articleDTO.getCouleur());
        }
if (articleDTO.getModel() != null) {
            existingArticle.setModel(articleDTO.getModel());
        }
     if (articleDTO.getNumSerie() != null && !articleDTO.getNumSerie().equals(existingArticle.getNumSerie())) {
        if(articleRepository.existsByNumSerie(articleDTO.getNumSerie())){
 throw new DataIntegrityViolationException("Un article avec le numéro de séri  existe déjà");
        }
      
            existingArticle.setNumSerie(articleDTO.getNumSerie());
        }

//         if(articleRepository.existsByNumSerie (articleDTO.getNumSerie())){
// throw new DataIntegrityViolationException("Article avec ce numero de serie existe ");
//         }
  if (articleDTO.getEtat() != null) {
        existingArticle.setEtat(articleDTO.getEtat());
        }
  if (articleDTO.getMarque() != null) {
            existingArticle.setMarque(articleDTO.getMarque());
        }    if (articleDTO.getStatus() != null) {
            existingArticle.setStatus(articleDTO.getStatus());    }
    if (articleDTO.getDescription() != null) {
          existingArticle.setDescription(articleDTO.getDescription());
    }

        if (articleDTO.getCaracteristiques() != null) {
            var caracteristiques = articleDTO.getCaracteristiques().stream().map(dto -> {
 CaracteristiqueSupl caracteristique = new CaracteristiqueSupl();
      if(dto.getNom() == null && dto.getValeur()==null){
             throw new IllegalArgumentException("Le nom categorie et valeur obligatoires");
                    }
            caracteristique.setNom(dto.getNom());
            caracteristique.setValeur(dto.getValeur());
            caracteristique.setArticles(existingArticle);
                    return caracteristique;
                }).collect(Collectors.toList());
            existingArticle.setCaracteristiques(caracteristiques);
        }

try {
         String ancienneValeur = existingArticle.toString();
        
        existingArticle.setDateModification(LocalDateTime.now());
        
        Articles updatedArticle = articleRepository.save(existingArticle);
        

        if (articleDTO.getStatus() != null && !articleDTO.getStatus().equals(ancienneValeur)) {
            updateStockMateriel(existingArticle.getMateriel().getId());
        }

        enregistrerHistorique("MODIFICATION_ARTICLE", 
            "Modification de l'article: " + updatedArticle.getMarque() + " " + updatedArticle.getModel(),
            personnelId, null, null, null, updatedArticle.getId(),
            ancienneValeur, updatedArticle.toString());
        
        return articleMapper.toDTO(updatedArticle);
        } catch (RuntimeException e) {
         throw new RuntimeException("Erreur de modification: "+e.getMessage());
        }
    }
    
      @Transactional(rollbackFor = Exception.class)
    @Override
    public void supprimerArticle(Long id, Long personnelId) {
        try {
        Personnel personnel = personnelRepository.findById(personnelId).orElseThrow(() -> new RuntimeException("Personnel non trouvé "));
        
        Articles article = articleRepository.findById(id).orElseThrow(() -> new RuntimeException("Article non trouvé " ));

        Long materielId = article.getMateriel().getId();
        String ancienneValeur = article.toString();
        
        articleRepository.delete(article);  
      updateStockMateriel(materielId);
       
        enregistrerHistorique("SUPPRESSION_ARTICLE", "Suppression de l'article: " + article.getMarque() + " " + article.getModel(),personnelId, null, null, null, null, ancienneValeur, null);
        } catch (RuntimeException e) {
            log.error("Erreru de suppression.  ", e);
            throw new RuntimeException("erreur de suppression: "+e.getMessage());
        }

    }
    
    @Override
    public List<ArticleDTO> getArticlesParMateriel(Long materielId) {
       List< Articles> article = articleRepository.findByMaterielId(materielId);
       if(article.isEmpty()){
        throw new RuntimeException("Article vide");
       } 
        return article.stream().map(articleMapper::toDTO).collect(Collectors.toList());
    }
    
    @Override
    public List<ArticleDTO> getArticlesParStatus(String status) {
        return articleRepository.findByStatus(status).stream().map(articleMapper::toDTO).collect(Collectors.toList());
    }

      @Transactional(rollbackFor = Exception.class)
    @Override
    public ArticleDTO vendu(Long personnelId, Long id, Long clientId){
        try {

            if(personnelId == null || id == null|| clientId == null){
throw new RuntimeException("L'id client, article et personnel requis");
            }

         Personnel personnel = personnelRepository.findById(personnelId).orElseThrow(() -> new NoSuchElementException("Personnel non trouvé  " ));
        
        Articles article = articleRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Article non trouvé avec cet ID.." ));

        Client client = clientRepository.findById(clientId).orElseThrow(() -> new NoSuchElementException("Client n'existe pas"));

        if(article.getStatus() == "vendu"){
            throw new DataIntegrityViolationException("L'artice est deja vendu");
        } 

        article.setStatus("vendu");
        articleRepository.save(article);
        enregistrerHistorique("Vente article", "Vente de article au client", personnelId, clientId, null, null, id, null, client.toString());
        return articleMapper.toDTO(article);
        } catch (RuntimeException e) {
           throw new RuntimeException("Erreur: "+e.getMessage());
        }
               
    }
    
    @Override
    public ArticleDTO changerStatusArticle(Long id, String nouveauStatus, Long personnelId) {
        Personnel personnel = personnelRepository.findById(personnelId).orElseThrow(() -> new RuntimeException("Personnel non trouvé  " ));
        
        Articles article = articleRepository.findById(id).orElseThrow(() -> new RuntimeException("Article non trouvé avec cet ID" ));
        
        String ancienStatus = article.getStatus();
        String ancienneValeur = article.toString();
        
        article.setStatus(nouveauStatus);
 article.setDateModification(LocalDateTime.now());
        
        Articles updatedArticle = articleRepository.save(article);
        
        updateStockMateriel(article.getMateriel().getId());
      
        enregistrerHistorique("CHANGEMENT_STATUS_ARTICLE", 
            "Changement de statut de l'article: " + ancienStatus + " -> " + nouveauStatus, personnelId, null, null, null, updatedArticle.getId(),
            ancienneValeur, updatedArticle.toString());
        
        return articleMapper.toDTO(updatedArticle);
    }
    
    private void updateStockMateriel(Long materielId) {
        Integer quantiteDisponible = articleRepository.countByMaterielIdAndStatusDisponible(materielId);
        
        Materiel materiel = materielRepository.findById(materielId).orElseThrow(() -> new RuntimeException("Matériel non trouvé avec l'ID: " + materielId));
        
        materiel.setQuteStock(quantiteDisponible);
        
    if (quantiteDisponible == 0) {
     materiel.setEtatStock("vide");
     } else if (quantiteDisponible < 5) {
  materiel.setEtatStock("faible");
  } else if (quantiteDisponible < 10) {
      materiel.setEtatStock("moyen");
 } else {
            materiel.setEtatStock("bon");
        }
        
        materiel.setDateModification(LocalDateTime.now());
        DepotService.updateTotalArticlesDisponibles();
        materielRepository.save(materiel);
    }
    
    private void enregistrerHistorique(String typeAction, String description,   Long utilisateurId, Long clientId, Long fournisseurId, Long depotId, 
     Long articleId, String ancienneValeur, String nouvelleValeur) {
        Historique historique = new Historique();
historique.setTypeAction(typeAction);
historique.setDescription(description);
 historique.setDateAction(LocalDateTime.now()); historique.setUtilisateurId(utilisateurId);
        historique.setClientId(clientId);
 historique.setFournisseurId(fournisseurId);
        historique.setDepotId(depotId);
        historique.setArticleId(articleId);
        historique.setAncienneValeur(ancienneValeur);
        historique.setNouvelleValeur(nouvelleValeur);
        
        historiqueRepository.save(historique);
    }

    @Override
    public ArticleDTO supprimerCaracteristique(Long articleId, Long caracteristiqueId) {
    Articles articles = articleRepository.findById(articleId).orElseThrow(() -> new RuntimeException("Article introuvable"));
    CaracteristiqueSupl carct = caractRepository.findById(caracteristiqueId).orElseThrow(() -> new RuntimeException("Caracteristique non touvé"));


    if(carct.getArticles().getId() != articleId){
        throw new RuntimeException("La catégorie n'appartient pas à cette article");
    }

    try {
        enregistrerHistorique("SUPPRESSION_CARACTERISTIQUE", "Suppression de categorie: " + carct.toString(), null, null, null, null, articleId, null, null );
        articles.getCaracteristiques().remove(carct);
        caractRepository.delete(carct);
     
        return articleMapper.toDTO(articles);
    } catch (Exception e) {
        throw new RuntimeException(e.getMessage());
    }
    }
}
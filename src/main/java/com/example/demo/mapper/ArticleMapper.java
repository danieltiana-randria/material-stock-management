package com.example.demo.mapper;

import com.example.demo.dto.ArticleDTO;
import com.example.demo.dto.CaracteristiqueSuplDTO;
import com.example.demo.model.Articles;
import com.example.demo.model.CaracteristiqueSupl;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArticleMapper {
    
    public ArticleDTO toDTO(Articles article) {
        if (article == null) {
            return null;
        }
        
        ArticleDTO dto = new ArticleDTO();
        dto.setId(article.getId());
        dto.setCouleur(article.getCouleur());
        dto.setModel(article.getModel());
        dto.setNumSerie(article.getNumSerie());
        dto.setEtat(article.getEtat());
        dto.setMarque(article.getMarque());
        dto.setStatus(article.getStatus());
        dto.setDescription(article.getDescription());
        dto.setDateCreation(article.getDateCreation());
        dto.setDateModification(article.getDateModification());
        
        if (article.getMateriel() != null) {
            dto.setMaterielId(article.getMateriel().getId());
        }
        
        if (article.getCaracteristiques() != null) {
            List<CaracteristiqueSuplDTO> caracteristiquesDTO = article.getCaracteristiques().stream().map(this::caracteristiqueToDTO).collect(Collectors.toList());
            dto.setCaracteristiques(caracteristiquesDTO);
        }
        
        return dto;
    }
    
    public Articles toEntity(ArticleDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Articles article = new Articles();
        article.setId(dto.getId());
        article.setCouleur(dto.getCouleur());
        article.setModel(dto.getModel());
        article.setNumSerie(dto.getNumSerie());
        article.setEtat(dto.getEtat());
        article.setMarque(dto.getMarque());
        article.setStatus(dto.getStatus());
        article.setDescription(dto.getDescription());
        article.setDateCreation(dto.getDateCreation());
        article.setDateModification(dto.getDateModification());
        
        return article;
    }
    
    private CaracteristiqueSuplDTO caracteristiqueToDTO(CaracteristiqueSupl caracteristique) {
        if (caracteristique == null) {
            return null;
        }
        
        CaracteristiqueSuplDTO dto = new CaracteristiqueSuplDTO();
        dto.setId(caracteristique.getId());
        dto.setNom(caracteristique.getNom());
        dto.setValeur(caracteristique.getValeur());
        
        // if (caracteristique.getArticles() != null) {
        //     dto.setArticleId(caracteristique.getArticles().getId());
        // }
        
        return dto;
    }
}
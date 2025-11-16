package com.example.demo.services.interfaces;

import com.example.demo.dto.ArticleDTO;
import java.util.List;

public interface ArticleService {
    ArticleDTO creerArticle(ArticleDTO articleDTO, Long personnelId);
    ArticleDTO getArticleParId(Long id);
    List<ArticleDTO> getAllArticles();
    ArticleDTO updateArticle(ArticleDTO articleDTO, Long personnelId);
    void supprimerArticle(Long id, Long personnelId);
    List<ArticleDTO> getArticlesParMateriel(Long materielId);
    List<ArticleDTO> getArticlesParStatus(String status);
     ArticleDTO vendu(Long personnelId, Long id, Long clientId);
    ArticleDTO changerStatusArticle(Long id, String nouveauStatus, Long personnelId);
    ArticleDTO supprimerCaracteristique(Long articleId, Long caracteristiqueId);
}
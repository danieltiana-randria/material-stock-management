package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.config.ApiResp;
import com.example.demo.dto.ArticleDTO;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.services.implementation.ArticleServiceImpl;
import com.example.demo.services.interfaces.ArticleService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private  ArticleRepository articleRepository;
    @Autowired
    private ArticleService articleService;
    @Autowired private ApiResp reponse;
    @Autowired private ArticleServiceImpl serviceImpl;



    private Map<String, String>reponse(String message){
        Map<String, String>resp = new HashMap<>();
        resp.put("message", message);
        return resp;
    }
    
    @PostMapping()
    public ResponseEntity<?> creerArticle(@RequestBody ArticleDTO articleDTO,@RequestHeader("personnel-id") Long personnelId) {

        
        try {
            ArticleDTO createdArticle = articleService.creerArticle(articleDTO, personnelId);
            return ResponseEntity.ok(createdArticle);
        }catch(IllegalArgumentException e){
            log.warn(e.getMessage());
            return reponse.erreur(400, e.getMessage());}
         catch(NoSuchElementException e){
            log.warn(e.getMessage());
           return reponse.erreur(400, e.getMessage());
         } catch(DataIntegrityViolationException e){
            return reponse.erreur(409, e.getMessage());
         }
            catch (RuntimeException e) {
                log.error(e.getMessage());
            return reponse.erreur(500, e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getArticle(@PathVariable Long id) {
try {
     ArticleDTO article = articleService.getArticleParId(id);
 return reponse.success("Recuperé", article);
} catch (RuntimeException e) {
            return reponse.erreur(404, e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<?> getAllArticles() {
        try {
             List<ArticleDTO> articles = articleService.getAllArticles();
                          return reponse.success("Articles recuperés", articles);
    } catch (Exception e) {
        return reponse.erreur(404, e.getMessage());
 }
       
    }
    
    @PutMapping
    public ResponseEntity<?> Article( @RequestBody ArticleDTO articleDTO, @RequestHeader("personnel-id") Long personnelId) {
    try {
ArticleDTO art = articleService.updateArticle(articleDTO, personnelId);
return reponse.success("Article modifé", art);
    }catch(NoSuchElementException e){
        log.warn(e.getMessage());
        return reponse.erreur(404, e.getMessage());
    }catch(DataIntegrityViolationException e){
        log.warn(e.getMessage());
        return reponse.erreur(409, e.getMessage());

    }  catch (RuntimeException e) {
        log.error(e.getMessage());
     return reponse.erreur(500, e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimerArticle(@PathVariable Long id,@RequestHeader("personnel-id") Long personnelId) {
        try {
            articleService.supprimerArticle(id, personnelId);
            return reponse.success("Article supprimé", null);
        } catch (RuntimeException e) {
            log.warn("Erreur de suppression"+e.getMessage());
            return reponse.erreur(404, e.getMessage());
        }
    }
    
    @GetMapping("/materiel/{materielId}")
    public ResponseEntity<?> getArticlesParMateriel(@PathVariable Long materielId) {
        try {
            log.info("Article recuperés");
            List<ArticleDTO> articles = articleService.getArticlesParMateriel(materielId);
            return reponse.success("Articles recuperés", articles);
        } catch (RuntimeException e) {
            log.warn("erreur de recuperation", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(reponse(e.getMessage()));
        }
    }

    @Transactional
    @DeleteMapping("/del/{id}/{caractId}")
    public ResponseEntity<?> deleteCaract(@PathVariable Long id, @PathVariable Long caractId){
        try {
           ArticleDTO art = articleService.supprimerCaracteristique(id, caractId);
           return reponse.success("Caracteristique supprimé", art);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return reponse.erreur(404, e.getMessage());
        }
    }
    
    
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getArticlesParStatus(@PathVariable String status) {
        try {
   List<ArticleDTO> articles = articleService.getArticlesParStatus(status);
        return ResponseEntity.ok(articles);
        } catch (Exception e) {
            log.warn("Erreur de recuperation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(reponse(e.getMessage()));
        }
     
    }

    @PutMapping("vendu/{id}/client/{clientId}")
    public ResponseEntity<?> vendu(@PathVariable Long id, @RequestHeader("personnel-Id") Long personnelId) {
        try {
            ArticleDTO article = serviceImpl.vendu(personnelId, personnelId, id);
         return   reponse.success("Article vendu", article);
        }catch(NoSuchElementException e){
            log.info(e.getMessage());
            return reponse.erreur(404, e.getMessage());
        }catch(DataIntegrityViolationException e){
            return reponse.erreur(409,e.getMessage());
        }  catch (RuntimeException e) {
            log.error(e.getMessage()    );
        return reponse.erreur(500, e.getMessage());
        }
      
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<?> changerStatusArticle(@PathVariable Long id,@RequestParam String nouveauStatus,@RequestHeader("personnel-id") Long personnelId) {
        try {
            ArticleDTO updatedArticle = articleService.changerStatusArticle(id, nouveauStatus, personnelId);
            return reponse.success("Article modifié", updatedArticle);
        } catch (RuntimeException e) {
            log.warn( e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(reponse(e.getMessage()));
        }
    }
}
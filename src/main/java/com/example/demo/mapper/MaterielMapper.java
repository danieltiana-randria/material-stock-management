package com.example.demo.mapper;

import com.example.demo.dto.MaterielDTO;
import com.example.demo.model.Category;
import com.example.demo.model.Materiel;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class MaterielMapper {
    
    public MaterielDTO toDTO(Materiel materiel) {
        if (materiel == null) {
            return null;
        }        
        MaterielDTO dto = new MaterielDTO();
        dto.setId(materiel.getId());
        dto.setNom(materiel.getNom());
        dto.setQuantiteStock(materiel.getQuteStock());
        dto.setEtatStock(materiel.getEtatStock());
        dto.setDateCreation(materiel.getDateCreation());
        dto.setDateModification(materiel.getDateModification());
          if (materiel.getCategories() != null) {
            Set<Long> ids = new HashSet<>();
            for (Category category : materiel.getCategories()) {
                ids.add(category.getId());
            }
            dto.setCategoriesIds(ids);
        }
        return dto;
    }
    
    public Materiel toEntity(MaterielDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Materiel materiel = new Materiel();
        materiel.setId(dto.getId());
        materiel.setNom(dto.getNom());
        materiel.setQuteStock(dto.getQuantiteStock());
        materiel.setEtatStock(dto.getEtatStock());
        materiel.setDateCreation(dto.getDateCreation());
        materiel.setDateModification(dto.getDateModification());
        return materiel;
    }
}
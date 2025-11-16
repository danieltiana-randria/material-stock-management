package com.example.demo.services.interfaces;

import com.example.demo.dto.MaterielDTO;
import java.util.List;

public interface MaterielService {
    MaterielDTO creerMateriel(MaterielDTO materielDTO, Long personnelId);
    MaterielDTO getMaterielParId(Long id);
    List<MaterielDTO> getAllMateriels();
    MaterielDTO updateMateriel(MaterielDTO materielDTO, Long personnelId);
    void supprimerMateriel(Long id, Long personnelId);
    void updateStockMateriel(Long materielId);
    List<MaterielDTO> getMaterielsAvecStockFaible(Integer seuil);
    List<MaterielDTO> getMaterielsParCategory(Long categoryId);
}
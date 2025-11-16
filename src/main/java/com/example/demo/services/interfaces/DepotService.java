package com.example.demo.services.interfaces;

import com.example.demo.dto.DepotDTO;
import java.util.List;

public interface DepotService {
    DepotDTO creerDepot(DepotDTO depotDTO, Long personnelId);
    DepotDTO obtenirDepotParId(Long id);
    List<DepotDTO> obtenirTousDepots();
    DepotDTO updateDepot(DepotDTO depotDTO, Long personnelId);
    void supprimerDepot(Long id, Long personnelId);
    void updateTotalArticlesDisponibles();
    Integer obtenirTotalArticlesDisponibles();
}
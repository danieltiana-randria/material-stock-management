package com.example.demo.mapper;
import com.example.demo.dto.DepotDTO;
import com.example.demo.model.Depot;
import com.example.demo.repository.DepotRepository;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class DepotMapper {

    private final DepotRepository depotRepository;

    DepotMapper(DepotRepository depotRepository) {
        this.depotRepository = depotRepository;
    }
    public  DepotDTO toDTO(Depot depot) {
        if (depot == null) return null;
        DepotDTO dto = new DepotDTO();
        dto.setId(depot.getId());
        dto.setCapacite(depot.getCapacite());
        dto.setDescription(depot.getDescription());
        dto.setEmplacement(depot.getEmplacement());
        dto.setNom(depot.getNom());
        dto.setTotalArticlesDisponibles(depot.getTotalArticlesDisponibles());
        dto.setEmplacement(depot.getEmplacement());
        return dto;
    }
    public Depot toEntity(DepotDTO dto) {
        if (dto == null) return null;
        Depot depot = new Depot();
        depot.setId(dto.getId());
        depot.setEmplacement(dto.getEmplacement());
        depot.setCapacite(dto.getCapacite());
        depot.setDescription(dto.getDescription());
        depot.setEmplacement(dto.getEmplacement());
        depot.setNom(dto.getNom());
        return depot;
    }
}

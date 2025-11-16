package com.example.demo.mapper;

import com.example.demo.dto.PersonnelDTO;
import com.example.demo.model.Personnel;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class PersonnelMapper {
    public  PersonnelDTO toDTO(Personnel personnel) {
        if (personnel == null) return null;
        PersonnelDTO dto = new PersonnelDTO();
        dto.setId(personnel.getId());
        dto.setNom(personnel.getNom());
        dto.setEmail(personnel.getEmail());
        dto.setAdresse(personnel.getAdresse());
        dto.setTelephone(personnel.getTelephone());
        dto.setPrenom(personnel.getPrenom());
        dto.setDepartement(personnel.getDepartement());
        dto.setDateEmbauche(personnel.getDateEmbauche());
        dto.setPoste(personnel.getPoste());
        dto.setStatut(personnel.getStatut());
        
        return dto;
    }

    public  Personnel toEntity(PersonnelDTO dto) {
        if (dto == null) return null;
        Personnel personnel = new Personnel();
        personnel.setId(dto.getId());
        personnel.setNom(dto.getNom());
        personnel.setPrenom(dto.getPrenom());
        personnel.setStatut(dto.getStatut());
        personnel.setDepartement(dto.getDepartement());
        personnel.setDateEmbauche(dto.getDateEmbauche());
        personnel.setEmail(dto.getEmail());
        personnel.setAdresse(dto.getAdresse());
        personnel.setTelephone(dto.getTelephone());
        return personnel;
    }
}

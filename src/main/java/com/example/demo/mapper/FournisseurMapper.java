package com.example.demo.mapper;
import com.example.demo.dto.FournisseurDTO;
import com.example.demo.model.Fournisseur;
import java.util.stream.Collectors;
public class FournisseurMapper {
    public static FournisseurDTO toDTO(Fournisseur fournisseur) {
        if (fournisseur == null) return null;
        FournisseurDTO dto = new FournisseurDTO();
        dto.setId(fournisseur.getId());
        dto.setNom(fournisseur.getNom());
        dto.setAdresse(fournisseur.getAdresse());
        dto.setTelephone(fournisseur.getTelephone());
        dto.setEmail(fournisseur.getEmail());

        return dto;
    }
    public static Fournisseur toEntity(FournisseurDTO dto) {
        if (dto == null) return null;
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setId(dto.getId());
        fournisseur.setNom(dto.getNom());
        fournisseur.setAdresse(dto.getAdresse());
        fournisseur.setTelephone(dto.getTelephone());
        fournisseur.setEmail(dto.getEmail());

        return fournisseur;
    }
}

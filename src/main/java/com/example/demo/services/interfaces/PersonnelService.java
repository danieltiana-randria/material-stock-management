package com.example.demo.services.interfaces;

import com.example.demo.dto.PersonnelDTO;
import java.util.List;

public interface PersonnelService {
    PersonnelDTO creerPersonnel(PersonnelDTO personnelDTO);
    PersonnelDTO getPersonnelId(Long id);
    List<PersonnelDTO> getTousPersonnel();
    PersonnelDTO updatePersonnel( PersonnelDTO personnelDTO);
    void supprimerPersonnel(Long id);
    PersonnelDTO getPersonnelEmail(String email);
    List<PersonnelDTO> getPersonnelDepartement(String Departement);
    List<PersonnelDTO> getPersonnelStatut(String statut);
}
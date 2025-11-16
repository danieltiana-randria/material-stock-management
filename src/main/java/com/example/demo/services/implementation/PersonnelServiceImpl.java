package com.example.demo.services.implementation;

import com.example.demo.dto.PersonnelDTO;
import com.example.demo.mapper.PersonnelMapper;
import com.example.demo.model.Personnel;
import com.example.demo.repository.PersonnelRepository;
import com.example.demo.services.interfaces.PersonnelService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

@Slf4j
@Service
public class PersonnelServiceImpl implements PersonnelService {
    
    @Autowired private PersonnelRepository personnelRepository;
    @Autowired private PersonnelMapper personnelMapper;
    

    
    @Override
    public PersonnelDTO creerPersonnel(PersonnelDTO personnelDTO) {
     
        if (personnelDTO.getNom() == null || personnelDTO.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("nom requis");
        }
        
        if (personnelDTO.getPrenom() == null || personnelDTO.getPrenom().trim().isEmpty()) {
            throw new IllegalArgumentException(" prénom requis");
        }
        
        if (personnelDTO.getEmail() == null || personnelDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("L'email requis");
        }
        
        if (personnelRepository.existsByEmail(personnelDTO.getEmail())) {
            throw new DataIntegrityViolationException ("Un personnel avec email existe déjà");
        }
        
        Personnel personnel = personnelMapper.toEntity(personnelDTO);
        personnel.setDateCreation(LocalDateTime.now());

     
            personnel.setDateCreation(LocalDateTime.now());
        
        
        if (personnel.getStatut() == null) {
            personnel.setStatut("ACTIF");
        }
        
        Personnel savedPersonnel = personnelRepository.save(personnel);
        return personnelMapper.toDTO(savedPersonnel);
    }
    
    @Override
    public PersonnelDTO getPersonnelId(Long id) {
        Personnel personnel = personnelRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Aucun personnel avec id"));
        return personnelMapper.toDTO(personnel);
    }
    
    @Override
    public List<PersonnelDTO> getTousPersonnel() {
        try {
            List<Personnel> perso = personnelRepository.findAll();
        return perso.stream()
            .map(personnelMapper::toDTO)
            .collect(Collectors.toList()); 
        } catch (RuntimeException e) {
            throw new RuntimeException("ereur de recuperatio"+e.getMessage());
        }
       
            
    }
    
    @Override
    public PersonnelDTO updatePersonnel( PersonnelDTO personnelDTO) {

        if(personnelDTO.getId() == null){
throw new RuntimeException("Id de personnel requis");
        }
        Personnel existingPersonnel = personnelRepository.findById(personnelDTO.getId()).orElseThrow(() -> new NoSuchElementException("Personnel non trouvé avec ce id"));
        
        if (personnelDTO.getEmail() != null && 
            !personnelDTO.getEmail().equals(existingPersonnel.getEmail())) {
            if (personnelRepository.existsByEmail(personnelDTO.getEmail())) {
                throw new DataIntegrityViolationException("Un personnel email  existe déjà");
            }
            existingPersonnel.setEmail(personnelDTO.getEmail());
        }
        
        if (personnelDTO.getNom() != null) {
            existingPersonnel.setNom(personnelDTO.getNom());
        }
        if (personnelDTO.getPrenom() != null) {
            existingPersonnel.setPrenom(personnelDTO.getPrenom());
        }
        if (personnelDTO.getPoste() != null) {
            existingPersonnel.setPoste(personnelDTO.getPoste());
        }
        if (personnelDTO.getDepartement() != null) {
            existingPersonnel.setDepartement(personnelDTO.getDepartement());
        }
        if (personnelDTO.getTelephone() != null) {
            existingPersonnel.setTelephone(personnelDTO.getTelephone());
        }
        if (personnelDTO.getAdresse() != null) {
            existingPersonnel.setAdresse(personnelDTO.getAdresse());
        }
        if (personnelDTO.getDateEmbauche() != null) {
            existingPersonnel.setDateEmbauche(personnelDTO.getDateEmbauche());
        }
        if (personnelDTO.getStatut() != null) {
            existingPersonnel.setStatut(personnelDTO.getStatut());
        }
        
        Personnel updatedPersonnel = personnelRepository.save(existingPersonnel);
        return personnelMapper.toDTO(updatedPersonnel);
    }
    
    @Override
    public void supprimerPersonnel(Long id) {
        try {
            Personnel personnel = personnelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Personnel  introuvable avec ce id"));
        
        personnelRepository.delete(personnel);
        } catch (Exception e) {
            log.error("Erreur de suppression: ", e.getMessage());
            throw new RuntimeException("Erreur de recuperation"+e.getMessage());
        }
        
    }
    
    @Override
    public PersonnelDTO getPersonnelEmail(String email) {
        try {
                    Personnel personnel = personnelRepository.findByEmail(email);
        return personnelMapper.toDTO(personnel);
        } catch (Exception e) {
            log.error( e.getMessage());
            throw new RuntimeException("Erreur de recuperation");
        }

    }
    
    @Override
    public List<PersonnelDTO> getPersonnelDepartement(String Departement) {
        try {
             return personnelRepository.findAll().stream().filter(p -> Departement.equals(p.getDepartement())).map(personnelMapper::toDTO).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Erreur de recu^peration", e);
            throw new RuntimeException("Erreu recuperatio"+e.getMessage());
        }
       
    }

    @Override
    public List<PersonnelDTO> getPersonnelStatut(String statut) {
        return personnelRepository.findAll()
            .stream()
            .filter(p -> statut.equals(p.getStatut()))
            .map(personnelMapper::toDTO)
            .collect(Collectors.toList());
    }
}
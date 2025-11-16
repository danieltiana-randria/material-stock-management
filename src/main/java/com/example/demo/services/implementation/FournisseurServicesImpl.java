package com.example.demo.services.implementation;

import com.example.demo.dto.FournisseurDTO;
import com.example.demo.mapper.FournisseurMapper;
import com.example.demo.model.Fournisseur;
import com.example.demo.repository.FournisseurRepository;
import com.example.demo.services.interfaces.FournisseurServices;
import com.example.demo.services.interfaces.HistoriqueServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FournisseurServicesImpl implements FournisseurServices {

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Autowired
    private HistoriqueServices historiqueServices;

    @Override
    public List<Fournisseur> findAll() {
        try {
    return fournisseurRepository.findAll();
        } catch (Exception e) {
log.error("Erreur lors de la récupération des fournisseurs", e);
 throw new RuntimeException("Erreur lors de la récupération des fournisseurs");
        }
    }

    @Override
    public List<FournisseurDTO> findByAdresse(String adresse) {
        try {
return fournisseurRepository.findByAdresseContaining(adresse).stream().map(FournisseurMapper::toDTO).collect(Collectors.toList());
        } catch (Exception e) {
    log.error("Erreur de recgerche par adrese: ", e.getMessage());
 throw new RuntimeException("Erreur rcherche: "+e.getMessage());
        }
    }

    @Override
    public Optional<Fournisseur> findById(Long id) {
        try {return fournisseurRepository.findById(id);
        } catch (Exception e) {
  log.error("Ereur de recuperatn par id: ", e.getMessage());
    throw new RuntimeException("Erreur de recuperation: "+e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FournisseurDTO createFournisseur(FournisseurDTO fournisseurDto) {
        try {

if(fournisseurDto.getNom() == null || fournisseurDto.getNom().isEmpty()){
    throw new RuntimeException("Le nom doit pas null");
}

if(fournisseurRepository.existsByNomAndAdresseAndEmail(fournisseurDto.getNom(), fournisseurDto.getAdresse(), fournisseurDto.getEmail())){
    throw new DataIntegrityViolationException("Fournisseur deja present");
}

if(fournisseurRepository.existsByEmail(fournisseurDto.getEmail())){
    throw new RuntimeException("Email deja utilsie");
}

Fournisseur fournisseur = FournisseurMapper.toEntity(fournisseurDto);
Fournisseur savedFournisseur = fournisseurRepository.save(fournisseur);

            historiqueServices.createActionHistorique(
  "CREATION_FOURNISSEUR","Création d'un nouveau fournisseur: " + savedFournisseur.getNom(),
null, null, savedFournisseur.getId(), null, null, null, savedFournisseur.toString() );

         log.info("Fournisseur créé ");
            return FournisseurMapper.toDTO(savedFournisseur);
        } catch (RuntimeException e) {
    log.warn("Erreur de création du fournisseur: "+ e.getMessage());
    throw new RuntimeException("Erreur de création: "+ e.getMessage());
        }
    }

    @Override
    @Transactional
    public FournisseurDTO updateFournisseur(FournisseurDTO fournisseurDto) {
        try {

            if(fournisseurDto.getId() == null){
    throw new RuntimeException("L'id de fournissuer à modifier requis");
}
Fournisseur existingFournisseur = fournisseurRepository.findById(fournisseurDto.getId()).orElseThrow(() -> new RuntimeException("Fournisseur non trouvé avec  cet id"));



if(!fournisseurRepository.existsById(fournisseurDto.getId())){
    throw new RuntimeException("LE fournissuer n'existe pas");
}

if(fournisseurDto.getNom() != null || fournisseurDto.getNom().isEmpty()){
 existingFournisseur.setNom(fournisseurDto.getNom());
}

if(fournisseurDto.getAdresse() != null){
    existingFournisseur.setAdresse(fournisseurDto.getAdresse());
}

if(fournisseurRepository.existsByNomAndAdresseAndEmail(fournisseurDto.getNom(), fournisseurDto.getAdresse(), fournisseurDto.getEmail())){
throw new RuntimeException("Fournissuer deja present");
}

if(fournisseurDto.getEmail() != null){
existingFournisseur.setEmail(fournisseurDto.getEmail());
}

if(fournisseurRepository.existsByEmail(fournisseurDto.getEmail())){
    throw new RuntimeException("Email deja utilsie");}

String ancienneValeur = existingFournisseur.toString();

     Fournisseur updatedFournisseur = fournisseurRepository.save(existingFournisseur);  historiqueServices.createActionHistorique(
"MODIFICATION_FOURNISSEUR", "Modification du fournisseur: " + updatedFournisseur.getNom(),null, null, updatedFournisseur.getId(), null, null,ancienneValeur,
                updatedFournisseur.toString()
            );

            log.info("Fournisseur mis à jour avec succès");
            return FournisseurMapper.toDTO(updatedFournisseur);
        } catch (Exception e) {
            throw new RuntimeException("Erreur de msia joyur : "+e.getMessage());
        } 
    }

    @Override
    @Transactional
    public void deleteFournisseur(Long id) {
        try {
            Fournisseur fournisseur = fournisseurRepository.findById(id).orElseThrow(() -> new NoSuchElementException("L'id n'rxiste pas"
            
));

     historiqueServices.createActionHistorique(   "SUPPRESSION_FOURNISSEUR",  "Suppression du fournisseur: " + fournisseur.getNom(),
null, null, fournisseur.getId(), null, null,   fournisseur.toString(),
          null     );

            fournisseurRepository.deleteById(id);
            log.info("Fournisseur supprimé ");
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erreur lors de la suppression du fournisseur ", e.getMessage());
            throw new RuntimeException("Erreur lors de la suppression du fournisseur"+e.getMessage());
        }
    }
}
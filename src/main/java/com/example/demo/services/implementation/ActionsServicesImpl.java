package com.example.demo.services.implementation;

import com.example.demo.model.Actions;
import com.example.demo.repository.ActionRepository;
import com.example.demo.services.interfaces.ActionsServices;
import com.example.demo.services.interfaces.HistoriqueServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
public class ActionsServicesImpl implements ActionsServices {

    @Autowired
    private ActionRepository actionRepository;

    @Autowired
    private HistoriqueServices historiqueServices;

    @Override
    public List<Actions> findAll() {
        try {
       return actionRepository.findAll();
        } catch (Exception e) {
   log.error("Erreur en recuperation", e.getMessage());    throw new RuntimeException("Erreur de recuperation");
        }
    }

    @Override
    public Optional<Actions> findById(Long id) {
        try {
  return actionRepository.findById(id);
        } catch (Exception e) {
  log.error("Erreur de recherche", e.getMessage());
   throw new RuntimeException("Erreur lor de recherche"+e.getMessage());
        }
    }

    @Override
    public List<Actions> findByTypes(String types) {
        try {
  return actionRepository.findByTypesContaining(types); } catch (Exception e) {
   log.error("Erreur lors de recherhce", e);
   throw new RuntimeException("Errer de recherche "+ e.getMessage());
        }
    }

    @Override
    @Transactional
    public Actions create(Actions action) {
        try {  action.setDateCreation(LocalDateTime.now());
     action.setDateModification(LocalDateTime.now());
            
   Actions savedAction = actionRepository.save(action);
                    historiqueServices.createActionHistorique(
    "CREATION_ACTION","Création d'une nouvelle action: " + action.getTypes(),
 null, null, null, null, null,
                null,
                savedAction.toString()
            );
            
 log.info("Action créée avec succès");
 return savedAction;
        } catch (Exception e) {
            log.error("Erreur lors de la création de l'action", e.getMessage());
            throw new RuntimeException("Erreur de création de l'action");
        }
    }

    @Override
    @Transactional
    public Actions update(Long id, Actions action) {
        
        if(action.getId() == null){
            throw new RuntimeException("Id de depot requis");
        }
        try {Actions existingAction = actionRepository.findById(id) .orElseThrow(() -> new NoSuchElementException("Action nno trouvé "));
    String ancienneValeur = existingAction.toString();
            
 existingAction.setTypes(action.getTypes());
   existingAction.setDateModification(LocalDateTime.now());
            
 Actions updatedAction = actionRepository.save(existingAction);
            
 historiqueServices.createActionHistorique(
 "MODIFICATION_ACTION", "Modification de l'action: " + action.getTypes(), null, null, null, null, null, ancienneValeur,
                updatedAction.toString()
            );

            return updatedAction;
        } catch (RuntimeException e) {
          log.error( e.getMessage());
     throw new RuntimeException("Erreur de mise à jour"+e.getMessage());
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
 Actions action = actionRepository.findById(id).orElseThrow(() -> new RuntimeException("Action non trouvé " ));
       
     historiqueServices.createActionHistorique("SUPPRESSION_ACTION",
    "Suppression de l'action: " + action.getTypes(), null, null, null, null, null,
 action.toString(), null
            );
            
     actionRepository.deleteById(id);
        } catch (RuntimeException e) {
 throw new RuntimeException(e.getMessage());
        } 
    }
}
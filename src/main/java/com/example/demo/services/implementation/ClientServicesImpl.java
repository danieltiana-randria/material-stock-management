package com.example.demo.services.implementation;

import com.example.demo.dto.ArticleDTO;
import com.example.demo.dto.ClientDTO;
import com.example.demo.mapper.ArticleMapper;
import com.example.demo.mapper.ClientMapper;
import com.example.demo.model.Articles;
import com.example.demo.model.Client;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.ClientRepository;
import com.example.demo.services.interfaces.ClientServices;
import com.example.demo.services.interfaces.HistoriqueServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ClientServicesImpl implements ClientServices {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private HistoriqueServices historiqueServices;

    @Autowired private ArticleRepository articleRepository;

    @Autowired private ArticleServiceImpl articleService;

    @Override
    public List<ClientDTO> getAll() {
        try {
            return clientRepository.findAll().stream().map(ClientMapper::toDTO).collect(Collectors.toList());
        } catch (RuntimeException e) {
            log.error("Erreur lors de la récupération des clients", e);
            throw new RuntimeException("Erreur lors de la récupération des clients");
        }
    }

    @Override
    public Optional<ClientDTO> findById(Long id) {
        try {
            return clientRepository.findById(id).map(ClientMapper::toDTO);
        } catch (RuntimeException e) {
            log.error("Erreur Recherhce", e);
            throw new RuntimeException("Erreur  lors rechsrheu");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClientDTO createClient(ClientDTO ClientDTO,Long articleId, Long PersonnelId) {
       
        if(PersonnelId == null){
throw new IllegalArgumentException("Le personnel rsponsable est requis");
            }

    if(ClientDTO.getNom() == null && ClientDTO.getPrenom()== null){
                throw new IllegalArgumentException("Au moins le nom est fournis");
            }

 if(ClientDTO.getNom() == null){
    throw new IllegalArgumentException("Nom est requis");
            }

            if(ClientDTO.getArticleId() == null){
    throw new IllegalArgumentException("L'article concernant requis");
            }

    if(!articleRepository.existsById(ClientDTO.getArticleId())){
    log.error("L'article inexistan");
     throw new NoSuchElementException("L'article concernant inexistant");
            }

        try {



 articleRepository.findById(ClientDTO.getArticleId()).filter(article->"DISPONIBLE".equals(article.getStatus())).orElseThrow(()-> new NoSuchElementException(" Article non disponible ou vendu"));
 articleService.changerStatusArticle(ClientDTO.getArticleId(), "VENDU", PersonnelId);

            Client client = ClientMapper.toEntity(ClientDTO);

            if (!clientRepository.existsByNomAndPrenomAndAdresse(
ClientDTO.getNom(), ClientDTO.getPrenom(), ClientDTO.getAdresse())) {
clientRepository.save(client);
            
            }
historiqueServices.createActionHistorique("Achat materiel ","Cleint: " + ClientDTO.getNom() + " " + ClientDTO.getPrenom(),PersonnelId, client.getId(), null, null, ClientDTO.getArticleId(),null,client.toString()
            );

            log.info("Client créé ");
            return ClientMapper.toDTO(client);
        } catch (RuntimeException e) {
            log.error("Erreur de creaion: ", e);
            throw new RuntimeException("Erreur de creation: "+e.getMessage());
        } 
    }


      @Override
    @Transactional
    public void deleteClient(Long id) {
        try {
Client client = clientRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Client non trouvé avec l'ID: " + id));
historiqueServices.createActionHistorique("SUPPRESSION_CLIENT","Suppression du client: " + client.getNom() + " " + client.getPrenom(),null, client.getId(), null, null, null,client.toString(),null);

clientRepository.deleteById(id);log.info("Client supprimé ");
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erreur lors de la suppression ",  e);
            throw new RuntimeException("Erreur de suppression du client");
        }
    }
    @Override
@Transactional
    public ClientDTO updateClient(ClientDTO ClientDTO) {
        
        if(ClientDTO.getId() == null){
            throw new RuntimeException("Id de depot requis");
        }
      
            Client existingClient = clientRepository.findById(ClientDTO.getId()).orElseThrow(() -> new NoSuchElementException("Client inexistant"));

         

            if(ClientDTO.getNom() != null){
  existingClient.setNom(ClientDTO.getNom());
            }
            if(ClientDTO.getPrenom() != null){
existingClient.setPrenom(ClientDTO.getPrenom());
            }
      
            if(ClientDTO.getAdresse() !=null){
existingClient.setAdresse(ClientDTO.getAdresse());
            }

            if(ClientDTO.getTelephone() != null){
existingClient.setTelephone(ClientDTO.getTelephone());
            }

     
            if(ClientDTO.getEmail() != null){
    existingClient.setEmail(ClientDTO.getEmail());
            }

                 try {
            String ancienneValeur = existingClient.toString();

 existingClient.setDateModification(java.time.LocalDateTime.now());

            Client updatedClient = clientRepository.save(existingClient);
            historiqueServices.createActionHistorique("MODIFICATION_CLIENT","Modification du client: " + ClientDTO.getNom() + " " + ClientDTO.getPrenom(),null, updatedClient.getId(), null, null, null,ancienneValeur,
updatedClient.toString());
log.info("Client mis à jour");
return ClientMapper.toDTO(updatedClient);
        } catch (RuntimeException e) {
log.error("Erreur de mise à jour: ", e.getMessage());
throw new RuntimeException("Erreur de mise à jour: "+e.getMessage());
        } 
    }

  
    @Override
    public boolean existsByNomAndPrenomAndAdresse(String nom, String prenom, String adresse) {
        try {
return clientRepository.existsByNomAndPrenomAndAdresse(nom, prenom, adresse);
        } catch (Exception e) {
log.error("Erreur de  vérification du client", e);
     throw new RuntimeException("Erreur de vérification  client");
        }
    }
}
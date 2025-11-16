package com.example.demo.services.interfaces;

import com.example.demo.dto.ClientDTO;
import java.util.List;
import java.util.Optional;

public interface ClientServices {
    List<ClientDTO> getAll();
    Optional<ClientDTO> findById(Long id);
    ClientDTO createClient(ClientDTO ClientDTO, Long ArticleId, Long PersonnelId);
    ClientDTO updateClient( ClientDTO ClientDTO);
    void deleteClient(Long id);
    boolean existsByNomAndPrenomAndAdresse(String nom, String prenom, String adresse);
}
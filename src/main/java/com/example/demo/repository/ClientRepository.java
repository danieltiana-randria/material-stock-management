package com.example.demo.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.Client;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{
    Client save(String client);
    Optional<Client> findById(Long id);
    List<Client> findAll();
    boolean existsByNomAndPrenomAndAdresse(String nom, String prenom, String adresse);
}

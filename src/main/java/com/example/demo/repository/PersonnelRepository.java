package com.example.demo.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.PersonnelDTO;
import com.example.demo.model.Personnel;


@Repository
public interface PersonnelRepository extends JpaRepository<Personnel, Long> {

    Personnel save(String personnel);

    Personnel findByEmail(String email);

    List<Personnel> findByNom(String nom);

    List<Personnel> findByAdresse(String andresse);

    List<Personnel> findAll();

    boolean existsByEmail(String email);

    boolean existsByNom(String nom);

    Collection<PersonnelDTO> findByNomContaining(String nom);

}

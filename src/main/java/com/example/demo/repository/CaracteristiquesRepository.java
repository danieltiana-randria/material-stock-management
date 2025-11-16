package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.CaracteristiqueSupl;
import com.example.demo.model.Articles;


public interface CaracteristiquesRepository extends JpaRepository<CaracteristiqueSupl, Long>{
    boolean existsByNomAndValeurAndArticles(String nom, String valeur, Articles articles);
}

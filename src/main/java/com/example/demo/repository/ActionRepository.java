package com.example.demo.repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Actions;


public interface ActionRepository extends JpaRepository<Actions, Long>{
    Actions save(String action);
    Optional <Actions> findById(Long id);
     List <Actions> findByTypesContaining(String types);
     Actions findByTypes(String types);
    List<Actions> findByTypesContainingIgnoreCase(String types);
}

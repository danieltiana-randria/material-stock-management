package com.example.demo.services.interfaces;

import com.example.demo.model.Actions;
import java.util.List;
import java.util.Optional;

public interface ActionsServices {
    List<Actions> findAll();
    Optional<Actions> findById(Long id);
    List<Actions> findByTypes(String types);
    Actions create(Actions action);
    Actions update(Long id, Actions action);
    void delete(Long id);
}
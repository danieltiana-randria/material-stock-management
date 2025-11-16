package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class CategoryDTO {
    private Long id;
    private String type;
    private String description;
     @JsonFormat(pattern = "MM-dd-yyyy 'à' HH:mm")
    private LocalDateTime dateCreation;
}
package com.example.demo.mapper;
import com.example.demo.dto.ClientDTO;
import com.example.demo.model.Client;
public class ClientMapper {
    public static ClientDTO toDTO(Client client) {
        if (client == null) return null;
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setNom(client.getNom());
        dto.setPrenom(client.getPrenom());
        dto.setAdresse(client.getAdresse());
        return dto;
    }
    public static Client toEntity(ClientDTO dto) {
        if (dto == null) return null;
        Client client = new Client();
        client.setNom(dto.getNom());
        client.setPrenom(dto.getPrenom());
        client.setAdresse(dto.getAdresse());
        return client;
    }
}

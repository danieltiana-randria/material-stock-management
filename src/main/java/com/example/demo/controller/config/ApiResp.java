package com.example.demo.controller.config;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Component
public   class  ApiResp {
private Map<String, Object> reponse(Integer status, String message, Object data, LocalDateTime date){
    Map<String, Object> reponse = new HashMap<>();
    reponse.put("message", message);
    reponse.put("status", status);
    reponse.put("data", data);
    reponse.put("date", date);
    return reponse;
};
public ResponseEntity<?> success(String message, Object data){
return ResponseEntity.status(200).body(reponse(200, message, data, LocalDateTime.now()));
}

public ResponseEntity<?>erreur(Integer status, String message){
    return ResponseEntity.status(status).body(reponse(status, message, null, LocalDateTime.now()));
}

}

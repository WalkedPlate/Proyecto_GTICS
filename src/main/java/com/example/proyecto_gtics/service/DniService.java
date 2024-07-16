package com.example.proyecto_gtics.service;

import com.example.proyecto_gtics.dto.ResultDni;
import com.example.proyecto_gtics.entity.Usuarios;
import lombok.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DniService {

    String token = "sk-C4aCs9xgdLjEhaVy5wOIaCQz5aFyAVl4ZgknUuyMu+w==";

    public ResultDni obtenerDatosPorDni(String dni) {



        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        /*
        restTemplate.getInterceptors().add((outReq, bytes, clientHttpReqExec) -> {
            outReq.getHeaders().set(
                    HttpHeaders.AUTHORIZATION, "Bearer" + token
            );
            return clientHttpReqExec.execute(outReq, bytes);
        }); */

        String url = "https://api.verifica.id/v2/consulta/personas?dni=" + dni;
        try {
            ResponseEntity<ResultDni> responseMap = restTemplate.exchange(url, HttpMethod.POST, entity, ResultDni.class);
            return responseMap.getBody();

        } catch (Exception e) {
            return null;
        }
    }

    public String validarDni(String dni) {
        if (dni == null || dni.length() != 8 || !dni.matches("\\d+")) {
            return "El campo dni debe tener 8 dígitos.";
        }
        return null;
    }

}

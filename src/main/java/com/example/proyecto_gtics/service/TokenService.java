package com.example.proyecto_gtics.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TokenService {
    private final Map<String, String> tokenStorage = new HashMap<>();
    private final Map<String, Long> tokenExpiration = new HashMap<>();

    public String generateToken(String email) {
        String token = UUID.randomUUID().toString();
        tokenStorage.put(token, email);
        tokenExpiration.put(token, System.currentTimeMillis() + 5 * 60 * 1000); // 5 minutos
        return token;
    }

    public String getEmailFromToken(String token) {
        if (!tokenStorage.containsKey(token)) {
            return null;
        }
        if (tokenExpiration.get(token) < System.currentTimeMillis()) {
            tokenStorage.remove(token);
            tokenExpiration.remove(token);
            return "expiration";
        }
        return tokenStorage.get(token);
    }

    public void removeToken(String token) {
        tokenStorage.remove(token);
        tokenExpiration.remove(token);
    }
}
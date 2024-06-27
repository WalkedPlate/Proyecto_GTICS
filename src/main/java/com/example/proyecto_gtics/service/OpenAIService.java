package com.example.proyecto_gtics.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAIService {


    private String apiKey = "sk-proj-cGNzYqsVd431dZwrw6QpT3BlbkFJNdALNRvOHl07PY89zLRt";

    private static final String OPENAI_API_URL  = "https://api.openai.com/v1/chat/completions";

    public String getChatResponse(String userMessage) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + apiKey);

        JSONObject messageJson = new JSONObject();
        messageJson.put("role", "user");
        messageJson.put("content", userMessage);

        JSONArray messagesJsonArray = new JSONArray();
        messagesJsonArray.put(messageJson);

        JSONObject requestJson = new JSONObject();
        requestJson.put("model", "gpt-3.5-turbo");
        requestJson.put("messages", messagesJsonArray);

        HttpEntity<String> request = new HttpEntity<>(requestJson.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(OPENAI_API_URL, HttpMethod.POST, request, String.class);

        JSONObject responseJson = new JSONObject(response.getBody());
        return responseJson.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
    }
}

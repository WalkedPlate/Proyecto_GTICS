package com.example.proyecto_gtics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatGPTService {


    private String apiKey = "";

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public String getChatResponse(String userMessage) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(API_URL);

        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization", "Bearer " + apiKey);

        JSONObject json = new JSONObject();
        json.put("model", "gpt-3.5-turbo");
        json.put("messages", new JSONObject[]{
                new JSONObject().put("role", "user").put("content", userMessage)
        });

        StringEntity entity = new StringEntity(json.toString());
        httpPost.setEntity(entity);

        /*
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            JSONObject responseJson = new JSONObject(responseBody);
            return responseJson.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
        }

         */
        return null;
    }
}

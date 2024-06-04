package com.example.java_final_project_javafx;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ChatBot {
    private static final String API_URL = "https://api.chatanywhere.tech/v1/chat/completions";
    private static final String API_KEY = System.getenv("GPT_API_KEY");
    // private static final String prompt = "You will now play the role of an assistant. I will give you a to-do list, and you need to help me provide some suggestions on how to best arrange these tasks. ";
    private static final String prompt = "你現在要扮演一個助手，我會給你一個to do list，你要幫我給一些這些事情該怎麼安排比較好的建議: ";
    
    public ChatBot() {}

    public String getMessage(String message) {
        HttpClient client = HttpClient.newHttpClient();

        System.out.println("Getting response from GPT-3.5 Turbo...");

        System.out.println(prompt + message);

        String json = "{"
            + "\"model\": \"gpt-3.5-turbo\","
            + "\"messages\": [{\"role\": \"assistant\", \"content\": \"" + prompt + message + "\"}],"
            + "\"temperature\": 0.7"
            + "}";

        /*
         String json = "{"
            + "\"model\": \"gpt-3.5-turbo\","
            + "\"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}],"
            + "\"temperature\": 0.7"
            + "}"; 
         */

        HttpRequest request = null;

        try {
                request = HttpRequest.newBuilder()
                .uri(new URI(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

            
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
        HttpResponse<String> response = null;
        
        try {
            response = client.send(request, BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }

        System.out.println("Response received from GPT-3.5 Turbo");

        try {
            String jsonStr = response.body();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonStr);
            JsonNode contentNode = rootNode.path("choices").get(0).path("message").path("content");
            return contentNode.asText();
        } catch (Exception e) {
            System.out.println(response.body());
            e.printStackTrace();
        }

        return "Error";
    }
}
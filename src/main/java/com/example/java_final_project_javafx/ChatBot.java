package com.example.java_final_project_javafx;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URI;

public class ChatBot {
    private static final String API_URL = "https://api.chatanywhere.tech/v1/chat/completions";
    private static final String API_KEY = System.getenv("GPT_API_KEY");

    public static void main(String[] args) {
        ChatBot chatBot = new ChatBot();
        System.out.println(API_KEY);
        // System.out.println(chatBot.getMessageString("你好呀"));
    }

    public String getMessageString(String message) {
        HttpClient client = HttpClient.newHttpClient();

        String json = "{"
            + "\"model\": \"gpt-3.5-turbo\","
            + "\"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}],"
            + "\"temperature\": 0.7"
            + "}";

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

        System.out.println(response.body());

        return response.body();
    }
}
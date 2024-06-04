package com.example.java_final_project_javafx;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.text.StringEscapeUtils;

public class ChatBot {
    private static final String API_URL = "https://api.chatanywhere.tech/v1/chat/completions";
    private static final String API_KEY = System.getenv("GPT_API_KEY");
    // private static final String prompt = "You will now play the role of an assistant. I will give you a to-do list, and you need to help me provide some suggestions on how to best arrange these tasks. ";
    private String prompt;
    
    public ChatBot() {
        setInitPrompt();
    }

    public void setInitPrompt() {
        prompt = "你將化身為一個助手。我將給你一張待辦事項列表，格式以\"作業名稱,開始日期,結束日期,完成狀態,備註\"的方式呈現。以\"Algorithm,2024-06-02,2024-06-06,85,Prepare exam\"為例，其中包含了作業名稱、開始時間、結束時間、完成度和一些備註。在這個基礎上根據工作表，你需要建議我如何安排這些任務。";
    }

    public void setAdvicePrompt() {
        System.out.println(LocalDate.now().toString());
        prompt = "今天的日期是" + LocalDate.now().toString() + " ";
        prompt = "現在我將給你一個目標，你要幫我拆分任務並且安排，格式以\"作業名稱,開始日期,結束日期,完成狀態,備註\"的方式呈現。以\"Algorithm,2024-06-02,2024-06-06,85,Prepare exam\"為例，其中包含了作業名稱、開始時間、結束時間、完成度和一些備註。幫我把目標拆分成多項，此外不要作其他回應，每一行格式固定，不要加其他標號或數字標。";
    }
    
    public String processStr(String str) {
        // 將所有的換行符替換為空格
        String ret = str.replace("\n", " ");
        // 轉義 JSON 特殊字符
        ret = StringEscapeUtils.escapeJson(ret);
        return ret;
    }

    public String getMessage(String message) {
        HttpClient client = HttpClient.newHttpClient();

        System.out.println("Getting response from GPT-3.5 Turbo...");

        prompt = processStr(prompt);
        message = processStr(message);

        String json = "{"
            + "\"model\": \"gpt-3.5-turbo\","
            + "\"messages\": [{\"role\": \"assistant\", \"content\": \"" + prompt + message + "\"}],"
            + "\"temperature\": 0.7"
            + "}";
        
            JsonValidator validator = new JsonValidator();
            if (!validator.isValidJson(json)) {
                System.out.println("Invalid JSON");
                System.out.println(json);
                return "Error";
            }
        
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
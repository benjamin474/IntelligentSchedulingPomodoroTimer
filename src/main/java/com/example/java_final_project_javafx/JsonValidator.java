package com.example.java_final_project_javafx;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonValidator {
    public static boolean isValidJson(String json) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
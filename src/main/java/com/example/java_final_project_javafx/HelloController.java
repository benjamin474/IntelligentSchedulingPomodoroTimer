package com.example.java_final_project_javafx;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;


    @FXML
    private CheckBox checkBox;
    @FXML
    protected void onHelloClick(){
        welcomeText.setText("Welcome to Here");
    }
    @FXML
    protected void onHelloClick2(){
        welcomeText.setText("Welcome to Here2");
    }
    @FXML
    protected void onHelloClick3(){
        welcomeText.setText("Welcome to Here3");
    }
    @FXML
    protected void onCheckButtonClick(){
        if(checkBox.isSelected()){
            welcomeText.setText("Term accepted!!");
        }else{
            welcomeText.setText("Term not accepted!!");
        }
    }
}
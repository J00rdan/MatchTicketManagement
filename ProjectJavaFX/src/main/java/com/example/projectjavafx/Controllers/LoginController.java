package com.example.projectjavafx.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class LoginController extends Controller{
    @FXML
    public Button loginButton;
    @FXML
    public Button adminLoginButton;
    @FXML
    public TextField usernameField;
    @FXML
    public TextField passwordField;
    @FXML
    public Label wrongLogin;

    public void adminLogin(ActionEvent actionEvent) {
        String pass = passwordField.getText().toString();

        if(srv.loginAdmin(pass)) {
            try {
                gui.changeSceneToAdminMenu();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            wrongLogin.setMaxWidth(Double.MAX_VALUE);
            AnchorPane.setLeftAnchor(wrongLogin, 0.0);
            AnchorPane.setRightAnchor(wrongLogin, 0.0);
            wrongLogin.setAlignment(Pos.CENTER);
            wrongLogin.setText("Wrong Login Credentials");

        }
    }

    public void login(ActionEvent actionEvent){
        String username = usernameField.getText().toString();
        String pass = passwordField.getText().toString();
        if(srv.login(username, pass)) {
            try {
                gui.changeSceneToUserMenu();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            wrongLogin.setMaxWidth(Double.MAX_VALUE);
            AnchorPane.setLeftAnchor(wrongLogin, 0.0);
            AnchorPane.setRightAnchor(wrongLogin, 0.0);
            wrongLogin.setAlignment(Pos.CENTER);
            wrongLogin.setText("Wrong Login Credentials");

        }

    }
}

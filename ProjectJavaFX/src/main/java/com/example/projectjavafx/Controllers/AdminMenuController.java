package com.example.projectjavafx.Controllers;

import com.example.projectjavafx.Validators.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AdminMenuController extends Controller{
    @FXML
    public Button addEmployeeButton;

    @FXML
    public Button addMatchButton;

    @FXML
    public Button addTeamButton;

    @FXML
    public Button logoutButton;

    @FXML
    public TextField firstNameField;

    @FXML
    public TextField lastNameField;

    @FXML
    public TextField usernameField;

    @FXML
    public TextField numberField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public TextField statusField;

    @FXML
    public TextField teamName1Field;

    @FXML
    public TextField teamName2Field;

    @FXML
    public TextField teamNameField;

    @FXML
    public TextField ticketPriceField;

    @FXML
    public Label messageLabel;

    public void addTeam(ActionEvent actionEvent){
        messageLabel.setText("");
        try{
            String teamName = teamNameField.getText().toString();
            srv.addTeam(teamName);
            messageLabel.setText("Team added successfully");
        }
        catch (IllegalArgumentException | ValidationException ex){
            messageLabel.setText(ex.getMessage());
        }
        teamNameField.clear();

    }

    public void addMatch(ActionEvent actionEvent){
        messageLabel.setText("");
        try{
            String teamName1 = teamName1Field.getText().toString();
            String teamName2 = teamName2Field.getText().toString();
            int ticketPrice = Integer.parseInt(ticketPriceField.getText());
            int numberOfSeats = Integer.parseInt(numberField.getText());
            String status = statusField.getText().toString();

            srv.addMatch(teamName1, teamName2, ticketPrice, numberOfSeats, status);
            messageLabel.setText("Match added successfully");
        }catch (IllegalArgumentException ex){
            messageLabel.setText("Input is null");
        }
        catch (ValidationException ex){
            messageLabel.setText(ex.getMessage());
        }
        teamName1Field.clear();
        teamName2Field.clear();
        ticketPriceField.clear();
        numberField.clear();
        statusField.clear();
    }

    public void addEmployee(ActionEvent actionEvent){
        messageLabel.setText("");
        try{
            String firstName = firstNameField.getText().toString();
            String lastName = lastNameField.getText().toString();
            String username = usernameField.getText().toString();
            String pass = passwordField.getText().toString();

            srv.addEmployee(firstName, lastName, username, pass);
            messageLabel.setText("Employee added successfully");
        }catch (IllegalArgumentException ex){
            messageLabel.setText("Input is null");
        }
        catch (ValidationException ex){
            messageLabel.setText(ex.getMessage());
        }
        firstNameField.clear();
        lastNameField.clear();
        usernameField.clear();
        passwordField.clear();
    }

    public void logout(ActionEvent actionEvent){
        try{
            gui.changeScene("Login.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

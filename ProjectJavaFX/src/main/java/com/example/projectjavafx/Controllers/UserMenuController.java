package com.example.projectjavafx.Controllers;

import com.example.projectjavafx.Model.Match;
import com.example.projectjavafx.Validators.ValidationException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserMenuController extends Controller{
    ObservableList<Match> model = FXCollections.observableArrayList();

    @FXML
    public TableColumn<Match, String> Status;

    @FXML
    public TableColumn<Match, String> tableColumnPrice;

    @FXML
    public TableColumn<Match, String> tableColumnSeats;

    @FXML
    public TableColumn<Match, String> tableColumnTeam1;

    @FXML
    public TableColumn<Match, String> tableColumnTeam2;

    @FXML
    public TableView<Match> tableView;

    @FXML
    public TextField matchIdField;

    @FXML
    public TextField firstNameField;

    @FXML
    public TextField lastNameField;

    @FXML
    public TextField ticketsField;

    @FXML
    public Button sellButton;

    @FXML
    public Button logoutButton;

    @FXML
    public Button sortButton;

    @FXML
    public Label messageLabel;

    @FXML
    public Label soldOutLabel;

    public void init(){
        initModel();
        initialize();
    }

    @FXML
    public void initialize() {
        // TODO
        tableColumnTeam1.setCellValueFactory(new PropertyValueFactory<Match, String>("Team1"));
        tableColumnTeam2.setCellValueFactory(new PropertyValueFactory<Match, String>("Team2"));
        tableColumnPrice.setCellValueFactory(new PropertyValueFactory<Match, String>("ticketPrice"));
        tableColumnSeats.setCellValueFactory(new PropertyValueFactory<Match, String>("seatsAvailable"));
        Status.setCellValueFactory(new PropertyValueFactory<Match, String>("status"));
        tableView.setItems(model);
    }

    private void initModel() {
        Iterable<Match> messages = srv.getAllMatches();
        List<Match> messageTaskList = StreamSupport.stream(messages.spliterator(), false)
                .collect(Collectors.toList());

        model.setAll(messageTaskList);
    }

    public void sellTickets(){
        messageLabel.setText("");

        try{
            int matchId = Integer.parseInt(matchIdField.getText().toString());
            String firstName = firstNameField.getText().toString();
            String lastName = lastNameField.getText().toString();
            int numberOfTickets = Integer.parseInt(ticketsField.getText());

            srv.sellTickets(firstName, lastName, numberOfTickets, matchId);

            messageLabel.setText("Tickets sold successfully");

            init();
        }catch (IllegalArgumentException | ValidationException ex){
            messageLabel.setText(ex.getMessage());
        }
        matchIdField.clear();
        firstNameField.clear();
        lastNameField.clear();
        ticketsField.clear();

    }

    public void sortMatches(){
        Iterable<Match> matches = srv.sortMatches();
        List<Match> matchesList = StreamSupport.stream(matches.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(matchesList);
        initialize();
    }

    public void onSelectingItem(javafx.scene.input.MouseEvent mouseEvent) {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            Match match = tableView.getSelectionModel().getSelectedItem();
            matchIdField.setText(String.valueOf(match.getId()));

            if(match.getSeatsAvailable() == 0)
                soldOutLabel.setText("Sold Out");
            else soldOutLabel.setText("");
        }
    }

    public void logout(){
        try{
            gui.changeScene("Login.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

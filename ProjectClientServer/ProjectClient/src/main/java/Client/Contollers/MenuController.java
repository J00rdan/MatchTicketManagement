package Client.Contollers;

import Model.Customer;
import Model.Employee;
import Model.Match;
import Model.Validators.ValidationException;
import Services.Observer;
import Services.Service;
import Services.ServiceException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import rpcprotocol.Response;
import rpcprotocol.ResponseType;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MenuController implements Observer {
    private Service server;

    private Employee employee;

    ObservableList<Match> model = FXCollections.observableArrayList();
    ObservableList<Match> model2 = FXCollections.observableArrayList();

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
    public TableColumn<Match, String> Status1;

    @FXML
    public TableColumn<Match, String> tableColumnPrice1;

    @FXML
    public TableColumn<Match, String> tableColumnSeats1;

    @FXML
    public TableColumn<Match, String> tableColumnTeam11;

    @FXML
    public TableColumn<Match, String> tableColumnTeam21;

    @FXML
    public TableView<Match> tableView1;

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

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public void ticketsSold(Customer customer) throws ServiceException {
        Platform.runLater(()->{
            init();
        });
    }

    public void init(){
        initModel();
        initialize();
        initModel2();
        initialize2();
    }

    private void initModel() {
        Iterable<Match> messages = null;
        try {
            messages = server.getAllMatches();
            List<Match> messageTaskList = StreamSupport.stream(messages.spliterator(), false)
                    .collect(Collectors.toList());

            model.setAll(messageTaskList);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    private void initModel2() {
        Iterable<Match> messages = null;
        try {
            messages = server.sortMatches();
            List<Match> messageTaskList = StreamSupport.stream(messages.spliterator(), false)
                    .collect(Collectors.toList());

            model2.setAll(messageTaskList);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
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

    @FXML
    public void initialize2() {
        // TODO
        tableColumnTeam11.setCellValueFactory(new PropertyValueFactory<Match, String>("Team1"));
        tableColumnTeam21.setCellValueFactory(new PropertyValueFactory<Match, String>("Team2"));
        tableColumnPrice1.setCellValueFactory(new PropertyValueFactory<Match, String>("ticketPrice"));
        tableColumnSeats1.setCellValueFactory(new PropertyValueFactory<Match, String>("seatsAvailable"));
        Status1.setCellValueFactory(new PropertyValueFactory<Match, String>("status"));
        tableView1.setItems(model2);
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

    public void setServer(Service s) {
        server = s;
    }

    public void sellTickets(){
        messageLabel.setText("");

        try{
            int matchId = Integer.parseInt(matchIdField.getText().toString());
            String firstName = firstNameField.getText().toString();
            String lastName = lastNameField.getText().toString();
            int numberOfTickets = Integer.parseInt(ticketsField.getText());


            Match match = new Match();
            match.setId(matchId);
            Customer customer = new Customer(firstName, lastName, match, numberOfTickets);

            server.sellTickets(customer);

            messageLabel.setText("Tickets sold successfully");

            init();
        }catch (Exception ex){
            messageLabel.setText(ex.getMessage());
        }
        matchIdField.clear();
        firstNameField.clear();
        lastNameField.clear();
        ticketsField.clear();
    }

    public void handleLogout(ActionEvent actionEvent) {
        logout();
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }


    @FXML
    void logout() {
        try {
            server.logout(employee, this);
        } catch (ServiceException e) {
            System.out.println("Logout error " + e);
        }
    }
}

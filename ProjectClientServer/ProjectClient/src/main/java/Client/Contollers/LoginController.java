package Client.Contollers;

import Model.Employee;
import Services.Service;
import Services.ServiceException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class LoginController {
    private Service server;

    private MenuController menuCtrl;

    Parent mainMenuParent;

    @FXML
    public Button loginButton;
    @FXML
    public TextField usernameField;
    @FXML
    public TextField passwordField;
    @FXML
    public Label wrongLogin;

    public void setServer(Service s){
        server=s;
    }

    public void setParent(Parent p){
        mainMenuParent=p;
    }

    public void pressCancel(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void setMenuCtrl(MenuController menuCtrl) {
        this.menuCtrl = menuCtrl;
    }

    public void login(ActionEvent actionEvent){
        String username = usernameField.getText().toString();
        String pass = passwordField.getText().toString();

        Employee employee = new Employee("A", "A", username, pass);
        try {
            if(server.login(employee, menuCtrl)) {
                menuCtrl.init();
                menuCtrl.setEmployee(employee);
                Stage stage=new Stage();
                stage.setTitle("Menu Window");
                stage.setScene(new Scene(mainMenuParent));

                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        menuCtrl.logout();
                        System.exit(0);
                    }
                });
                stage.show();
                ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
            }

        } catch (ServiceException serviceException) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MPP chat");
            alert.setHeaderText("Authentication failure");
            alert.setContentText("Wrong username or password");
            alert.showAndWait();
        }
    }


}

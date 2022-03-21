package com.example.projectjavafx;

import com.example.projectjavafx.Controllers.AdminMenuController;
import com.example.projectjavafx.Controllers.Controller;
import com.example.projectjavafx.Controllers.UserMenuController;
import com.example.projectjavafx.Repository.CustomerDBRepository;
import com.example.projectjavafx.Repository.EmployeeDBRepository;
import com.example.projectjavafx.Repository.MatchDBRepository;
import com.example.projectjavafx.Repository.TeamDBRepository;
import com.example.projectjavafx.Service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class GUI extends Application {
    private Service srv;
    private static Stage stage;
    //private MenuController menuController;
    private AdminMenuController adminMenuController;
    private UserMenuController userMenuController;

    public GUI(){
        setService();
    }

    public void setService(){
        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }

        TeamDBRepository teamDBRepository=new TeamDBRepository(props);
        MatchDBRepository matchDBRepository = new MatchDBRepository(props, teamDBRepository);
        CustomerDBRepository customerDBRepository = new CustomerDBRepository(props, matchDBRepository);
        EmployeeDBRepository employeeDBRepository = new EmployeeDBRepository(props);

        srv = new Service(teamDBRepository, matchDBRepository, customerDBRepository, employeeDBRepository);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1050, 600);

        Controller loginController = fxmlLoader.getController();
        loginController.setService(srv, this);

        primaryStage.setTitle("Social Network");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void changeScene(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
        Parent pane = fxmlLoader.load();

        Controller loginController = fxmlLoader.getController();
        loginController.setService(srv, this);

        stage.getScene().setRoot(pane);
    }

    public void changeSceneToAdminMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AdminMenu.fxml"));
        Parent pane = fxmlLoader.load();

        adminMenuController = fxmlLoader.getController();
        adminMenuController.setService(srv, this);

        stage.getScene().setRoot(pane);
    }

    public void changeSceneToUserMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UserMenu.fxml"));
        Parent pane = fxmlLoader.load();

        userMenuController = fxmlLoader.getController();
        userMenuController.setService(srv, this);
        userMenuController.init();

        stage.getScene().setRoot(pane);
    }

    public void run(){
        launch();
    }
}

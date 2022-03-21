package com.example.projectjavafx.Controllers;


import com.example.projectjavafx.GUI;
import com.example.projectjavafx.Service.Service;

public class Controller {
    protected Service srv;
    protected GUI gui;

    public void setService(Service service, GUI gui){
        this.srv = service;
        this.gui = gui;
    }
}
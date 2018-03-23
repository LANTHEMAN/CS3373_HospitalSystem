package com.github.CS3733_D18_Team_F_Project_0;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.HashMap;

public class PaneSwitcher {
    private HashMap<String, Pane> panes;
    private Scene scene;

    public PaneSwitcher(Scene scene){
        this.scene = scene;
        panes = new HashMap<>();
    }

    public void addPane(String name, String fxmlFile, Class<? extends SwitchableController> ControllerClass) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Pane pane = loader.load();

            panes.put(name, pane);

            Object controller = loader.getController();
            if(!ControllerClass.isInstance(controller)){
                throw new AssertionError("ControllerClass must extend SwitchableController");
            }
            SwitchableController switchableController = (SwitchableController) controller;
            switchableController.initialize(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchTo(String name) {
        scene.setRoot(panes.get(name));
    }
}

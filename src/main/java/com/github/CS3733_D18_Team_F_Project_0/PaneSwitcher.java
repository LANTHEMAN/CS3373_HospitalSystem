package com.github.CS3733_D18_Team_F_Project_0;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.HashMap;

public class PaneSwitcher {
    private HashMap<String, Pane> panes;
    private Scene scene;

    public PaneSwitcher(Scene scene) {
        this.scene = scene;
        panes = new HashMap<>();
    }

    public void addPane(Screens.Screen screen) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(screen.fxmlFile));
            Pane pane = loader.load();

            panes.put(screen.fxmlFile, pane);

            Object controller = loader.getController();

            SwitchableController switchableController = (SwitchableController) controller;
            switchableController.initialize(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchTo(Screens.Screen screen) {
        scene.setRoot(panes.get(screen.fxmlFile));
    }
}

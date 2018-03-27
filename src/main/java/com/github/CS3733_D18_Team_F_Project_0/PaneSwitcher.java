package com.github.CS3733_D18_Team_F_Project_0;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

public class PaneSwitcher {
    private HashMap<String, Pane> panes;
    private Scene scene;
    public ObservableResourceFactory resFac = new ObservableResourceFactory();

    public PaneSwitcher(Scene scene) {
        this.scene = scene;
        panes = new HashMap<>();
        resFac.setResources(ResourceBundle.getBundle("LanguageBundle", new Locale("en", "US")));
    }

    // loads the screen into memory from the file
    public void load(Screens.Screen screen) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(screen.fxmlFile), resFac.getResources());
            Pane pane = loader.load();

            panes.put(screen.fxmlFile, pane);

            Object controller = loader.getController();

            SwitchableController switchableController = (SwitchableController) controller;
            switchableController.initialize(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // removes a screen entry from memory
    public void remove(Screens.Screen screen) {
        panes.remove(screen.fxmlFile);
    }

    // assumes the screen is already loaded
    public void setActive(Screens.Screen screen) {
        scene.setRoot(panes.get(screen.fxmlFile));
    }

    // clears all screens and load the given screen
    public void switchTo(Screens.Screen screen) {
        panes.clear();
        load(screen);
        scene.setRoot(panes.get(screen.fxmlFile));
    }

    public void switchResource(ResourceBundle resource, Screens.Screen screen){
        // reset the resource package
        resFac.setResources(resource);
        // reload the given screen
        panes.clear();
        load(screen);
        scene.setRoot(panes.get(screen.fxmlFile));
    }
}

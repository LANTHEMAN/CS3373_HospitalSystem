package edu.wpi.cs3733d18.teamF.controller.page.element.screensaver;

import edu.wpi.cs3733d18.teamF.controller.page.PageElement;
import edu.wpi.cs3733d18.teamF.controller.page.element.mapView.MapMementoSingleton;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;


public class Screensaver extends PageElement{

    private final ArrayList<String> askMe = new ArrayList<>(Arrays.asList("Ask me to find the nearest bathroom"
            , "Ask me to get religous services"
            , "Ask me to find the nearest elevator"
            , "Ask me to Find the Nearest Stairway"
            , "Ask me to find the parking garage"
            , "Ask me to bring you to Paitent Services"
            , "Ask me to get you food"
            , "Ask me to bring you to"
            , "Ask me to bring you to the nearest ATM"
            , "Ask me to bring you to the Shapiro Cardiovascular Building"
            , "Ask me to bring you to the international paitent Center"
            , "Ask me to bring you to Spiritual Care Office"));
    @FXML
    AnchorPane root;
    IdleMonitor idleMonitor;
    Scene scene;

    public void initialize(AnchorPane sourcePane, Scene scene) {
        initElement(sourcePane, root);
        sourcePane.setOnMouseClicked(this::wakeUp);
        sourcePane.setOnMouseMoved(this::wakeUp);

        this.scene = scene;

        idleMonitor = new IdleMonitor(new Duration(30000), this::start);
        idleMonitor.startMonitoring();
        idleMonitor.register(scene, Event.ANY);
    }

    public void changeTimeout(int millis){
        finalize();

        idleMonitor = new IdleMonitor(new Duration(millis), this::start);
        idleMonitor.startMonitoring();
        idleMonitor.register(scene, Event.ANY);
    }

    public void start(){
        MapMementoSingleton.getInstance().saveState();
        showElement();
    }

    public void wakeUp(Event e) {
        hideElement();
        MapMementoSingleton.getInstance().returnToLastState();
    }

    @Override
    public void finalize() {
        idleMonitor.stopMonitoring();
    }


}
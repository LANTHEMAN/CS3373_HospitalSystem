package edu.wpi.cs3733d18.teamF.controller.page.element.screensaver;

import edu.wpi.cs3733d18.teamF.controller.page.PageElement;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;


public class Screensaver extends PageElement{

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
        showElement();
        root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
    }

    public void wakeUp(Event e){
        hideElement();
    }

    @Override
    public void finalize() {
        idleMonitor.stopMonitoring();
    }

}
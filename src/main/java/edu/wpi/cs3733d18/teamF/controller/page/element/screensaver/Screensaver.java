package edu.wpi.cs3733d18.teamF.controller.page.element.screensaver;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733d18.teamF.controller.page.PageElement;
import edu.wpi.cs3733d18.teamF.controller.page.element.mapView.MapMementoSingleton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static java.lang.StrictMath.abs;


public class Screensaver extends PageElement{

    private  final ArrayList<String> welcome = new ArrayList<>(Arrays.asList("Welcome!"
    ,"Bienvenue!"
    ,"benvinguda!"
    ,"Bienvenido!"
    ,"Herzlich Willkommen!"
    ,"welkom!"));



    private final ArrayList<String> askMe = new ArrayList<>(Arrays.asList("Ask me to find the nearest bathroom"
            , "Ask me to find the nearest elevator"
            , "Ask me to find the earest stairway"
            , "Ask me to find the parking garage"
            , "Ask me to bring you to paitent services"
            , "Ask me to do a food request"
            , "Ask me to bring you to look for places nearby"
            , "Ask to call for help in an emergency"
            , "Ask me to play where's Packman"
            , "Ask me to find the closet elevator"
            , "Ask me to bring you to the nearest ATM"
            , "Say Hello Kiosk if you want to use voice commands"
            , "Ask me to go to the tower"
            , "Ask me to go to the dialysis"
            , "Ask me to bring you to the Shapiro Cardiovascular Building"
            , "Ask me to bring you to the international paitent Center"
            , "Ask me to navigate  to Spiritual Care Office"));
        @FXML
    AnchorPane root;
    IdleMonitor idleMonitor;
    Scene scene;

    @FXML
    JFXTextField askMeField;

    @FXML
    JFXTextField welcomeField;

    Random rand = new Random();


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
        welcomeField.setText(welcome.get(abs(rand.nextInt()) % welcome.size()));
        askMeField.setText(askMe.get(abs(rand.nextInt()) % askMe.size()));

        Timeline clock1 = new Timeline(new KeyFrame(Duration.seconds(rand.nextInt()), e ->
                welcomeField.setText(welcome.get(abs(rand.nextInt()) % askMe.size()))));
        clock1.setCycleCount(Animation.INDEFINITE);
        clock1.play();

        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(5), e ->
                askMeField.setText(askMe.get(abs(rand.nextInt()) % askMe.size()))));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public void wakeUp(Event e) {
        MapMementoSingleton.getInstance().reset();
        hideElement();
    }

    @Override
    public void finalize() {
        idleMonitor.stopMonitoring();
    }


}

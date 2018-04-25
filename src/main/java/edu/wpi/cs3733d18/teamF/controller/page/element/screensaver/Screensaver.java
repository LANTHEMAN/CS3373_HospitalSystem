package edu.wpi.cs3733d18.teamF.controller.page.element.screensaver;

import edu.wpi.cs3733d18.teamF.controller.page.PageElement;
import edu.wpi.cs3733d18.teamF.controller.page.element.mapView.MapMementoSingleton;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import javax.swing.text.html.ImageView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

import static java.lang.StrictMath.abs;


public class Screensaver extends PageElement {

    private final ArrayList<String> welcome = new ArrayList<>(Arrays.asList(
            "WELCOME!"
            , "BIENVENUE!"
            , "BENVINGUDA!"
            , "BIENVENIDO!"
            , "HERZLICH WILLKOMMEN!"
            , "WELKOM!"
            , "GHINI VINISHI!"));

    private final ArrayList<String> askMe = new ArrayList<>(Arrays.asList(
            "Ask me to find the nearest bathroom"
            , "Ask me the Weather"
            , "Ask me where the dentist is"
            , "Ask me to find the parking garage"
            , "Ask me where the Cafe is located"
            , "Ask me to find the Ear Nose and Throat Doctor"
            , "Ask me to find the closest exit"
            , "Ask me to find the closest elevator"
            , "Say Hello Kiosk to use voice commands"
            , "Ask me to Neuroscience"
            , "Ask me to Rap"
            , "Ask me to go to Orthopedics and Rhemutology"
            , "Ask me to go to Plastic Surgery"
            , "Ask directions to Radiology"
            , "Ask me directions to Nuclear Medicine"
            , "Ask me where the Duncan Reid Conference Room is"
            , "Ask me how to get to the Lee Bell Breast Center"
            , "Click anywhere on the map to get directions"
            , "Play a game of Where's Pacman if you are bored"));
    @FXML
    AnchorPane root;

    IdleMonitor idleMonitor;
    Scene scene;

    @FXML
    Label askMeField;

    @FXML
    Label time;

    @FXML
    Label date;

    @FXML
    Label welcomeField;

    @FXML
    ImageView Background;

    Random rand = new Random();


    Timeline welcomeTime = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), welcomeField);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.play();

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), welcomeField);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        fadeOut.setOnFinished(event -> {
            welcomeField.setText(welcome.get(abs(rand.nextInt()) % welcome.size()));
            fadeIn.play();
        });
    }));

    Timeline askMeTime = new Timeline(new KeyFrame(Duration.seconds(4), e -> {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), askMeField);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.play();

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), askMeField);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        fadeOut.setOnFinished(event -> {
            askMeField.setText(askMe.get(abs(rand.nextInt()) % askMe.size()));
            fadeIn.play();
        });
    }));

    private String start;

    Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
        Calendar cal = Calendar.getInstance();
        int second = cal.get(Calendar.SECOND);
        int minute = cal.get(Calendar.MINUTE);
        int hour = cal.get(Calendar.HOUR) % 12 + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) % 12 + 1;
        int year = cal.get(Calendar.YEAR);
        time.setText(hour + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second));
        date.setText(month + "/" + day + "/" + year);
    }));


    public void initialize(AnchorPane sourcePane, Scene scene) {
        initElement(sourcePane, root);
        sourcePane.setOnMouseClicked(this::wakeUp);
        sourcePane.setOnMouseMoved(this::wakeUp);

        this.scene = scene;

        idleMonitor = new IdleMonitor(new Duration(30000), this::start);
        idleMonitor.startMonitoring();
        idleMonitor.register(scene, Event.ANY);

        System.out.println("Init");

        welcomeField.setText(welcome.get(abs(rand.nextInt()) % welcome.size()));
        askMeField.setText(askMe.get(abs(rand.nextInt()) % askMe.size()));

        welcomeTime.setCycleCount(Animation.INDEFINITE);
        welcomeTime.play();
        askMeTime.setCycleCount(Animation.INDEFINITE);
        askMeTime.play();
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

    }

    public void changeTimeout(int millis) {
        finalize();

        idleMonitor = new IdleMonitor(new Duration(millis), this::start);
        idleMonitor.startMonitoring();
        idleMonitor.register(scene, Event.ANY);
    }

    public void start() {
        MapMementoSingleton.getInstance().saveState();
        showElement();
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

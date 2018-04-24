package edu.wpi.cs3733d18.teamF.controller.page.element.screensaver;

import edu.wpi.cs3733d18.teamF.controller.page.PageElement;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class Screensaver extends PageElement {

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
    @FXML
    private Label time;
    @FXML
    private Label date;

    public void initialize(AnchorPane sourcePane, Scene scene) {
        initElement(sourcePane, root);
        sourcePane.setOnMouseClicked(this::wakeUp);
        sourcePane.setOnMouseMoved(this::wakeUp);

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            Calendar cal = Calendar.getInstance();
            int second = cal.get(Calendar.SECOND);
            int minute = cal.get(Calendar.MINUTE);
            int hour = cal.get(Calendar.HOUR) % 12 + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH) % 12 + 1;
            int year = cal.get(Calendar.YEAR);
            time.setText(hour + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second));
            date.setText(month + "/" + day + "/" + year);
        }),
                new KeyFrame(Duration.seconds(1))
        );

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }


    /*
            idleMonitor = new IdleMonitor(new Duration(30000), this::start);
            idleMonitor.startMonitoring();
            idleMonitor.register(scene, Event.ANY);
        }
    */
    public void start() {
        showElement();
        root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
    }

    public void wakeUp(Event e) {
        hideElement();
    }

    @Override
    public void finalize() {
        idleMonitor.stopMonitoring();
    }


}
package edu.wpi.cs3733d18.teamF.gfx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class PaneVoiceController extends PaneController {
    public PaneVoiceController(Pane root) {
        super(root);

        addDrawable(pane -> {
            Circle circle = new Circle(10, Color.DEEPSKYBLUE);
            circle.setCenterX(pane.getWidth() / 2);
            circle.setCenterY(pane.getHeight() / 1.3);
            final Boolean[] isUp = {false};
            final Integer[] timer = {0};

            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), event -> {
                timer[0]++;
                if (isUp[0]) {
                    circle.setRadius(circle.getRadius() + 1);
                    if (circle.getRadius() > 30) {
                        isUp[0] = false;
                    }
                }
                if (!isUp[0]) {
                    circle.setRadius(circle.getRadius() - 1);
                    if (circle.getRadius() < 10) {
                        isUp[0] = true;
                    }
                }
                if (timer[0] == 500 ) {
                    circle.setRadius(0);
                }
            }));
            timeline.setCycleCount(500);
            timeline.play();
            pane.getChildren().add(circle);
        });
    }


}

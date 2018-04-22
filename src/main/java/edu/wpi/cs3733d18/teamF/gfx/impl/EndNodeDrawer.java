package edu.wpi.cs3733d18.teamF.gfx.impl;

import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import edu.wpi.cs3733d18.teamF.Main;
import edu.wpi.cs3733d18.teamF.MapSingleton;
import edu.wpi.cs3733d18.teamF.gfx.NodeDrawable;
import edu.wpi.cs3733d18.teamF.graph.Node;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class EndNodeDrawer extends NodeDrawable {

    private boolean isSelected = false;

    public EndNodeDrawer(Node node) {
        super(node);
    }

    public EndNodeDrawer() {
        super();
    }

    @Override
    public void selectNode() {
        isSelected = true;
    }

    @Override
    public void unselectNode() {
        isSelected = false;
    }

    @Override
    public void draw(Pane pane) {
        boolean is2D = MapSingleton.getInstance().getMap().is2D();
        double imageWidth = 5000;
        double imageHeight = is2D ? 3400 : 2772;
        double posX = is2D ? node.getPosition().getX() : node.getWireframePosition().getX();
        double posY = is2D ? node.getPosition().getY() : node.getWireframePosition().getY();
        if (!node.getFloor().equals(MapSingleton.getInstance().getMap().getFloor()) && is2D) {
            return;
        }
        ImageView end  = new ImageView(Main.class.getResource("end-icon.png").toExternalForm());
        end.setTranslateX((posX * pane.getMaxWidth() / imageWidth) - 85);
        end.setTranslateY((posY * pane.getMaxHeight() / imageHeight) - 131);

        end.setScaleX(0.03);
        end.setScaleY(0.03);
        end.setVisible(true);
        pane.getChildren().add(end);

        TranslateTransition dropEnd = new TranslateTransition(Duration.seconds(0.15), end);
        dropEnd.setFromY(end.getTranslateY() - 10);
        dropEnd.setToY(end.getTranslateY());
        dropEnd.play();

        if (!node.getNodeType().equals("HALL")) {
            JFXTextField endText = new JFXTextField();

            endText.setPrefWidth(-1);
            endText.setPrefHeight(-1);

            endText.setTranslateX((posX * pane.getMaxWidth() / imageWidth));
            endText.setTranslateY((posY * pane.getMaxHeight() / imageHeight) - 10);

            endText.setText(node.getShortName());
            endText.setFocusColor(Color.WHITE);
            endText.setPrefColumnCount((node.getShortName().length() / 4) * 3);
            endText.setAlignment(Pos.CENTER);
            endText.setStyle("-fx-background-color: red; " +
                    "-fx-border-color: black; " +
                    "-fx-border-width: .2; " +
                    "-fx-border-radius: 2; " +
                    "-fx-background-radius: 2;" +
                    "-fx-text-fill: white;" +
                    "-fx-font: 2 Ariel");
            pane.getChildren().add(endText);
            endText.setVisible(true);
        }
    }
}

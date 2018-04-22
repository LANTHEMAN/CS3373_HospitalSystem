package edu.wpi.cs3733d18.teamF.gfx.impl;

import edu.wpi.cs3733d18.teamF.Main;
import edu.wpi.cs3733d18.teamF.MapSingleton;
import edu.wpi.cs3733d18.teamF.gfx.NodeDrawable;
import edu.wpi.cs3733d18.teamF.graph.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import javax.swing.*;
import javax.swing.text.Element;
import javax.swing.text.html.ImageView;
import java.net.URL;

public class PacmanNodeTest extends NodeDrawable {

    private boolean isSelected = false;
    public PacmanNodeTest(Node node) {
        super(node);
    }

    public PacmanNodeTest() {
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

        Image icon =  new Image(Main.class.getResource("pacman.gif").toExternalForm());
        javafx.scene.image.ImageView iconView = new javafx.scene.image.ImageView(icon);
        boolean is2D = MapSingleton.getInstance().getMap().is2D();
        double imageWidth = 5000;
        double imageHeight = is2D ? 3400 : 2772;
        double posX = is2D ? node.getPosition().getX() : node.getWireframePosition().getX();
        double posY = is2D ? node.getPosition().getY() : node.getWireframePosition().getY();
        if(!node.getFloor().equals(MapSingleton.getInstance().getMap().getFloor())){
            return;
        }
        iconView.setTranslateX((posX * pane.getMaxWidth() / imageWidth) - 85);
        iconView.setTranslateY((posY * pane.getMaxHeight() / imageHeight) - 131);
        iconView.setScaleX(0.03);
        iconView.setScaleY(0.03);
        iconView.setVisible(true);
        pane.getChildren().add(iconView);
    }
}

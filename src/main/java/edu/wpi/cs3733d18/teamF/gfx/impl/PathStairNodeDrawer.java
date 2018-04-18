package edu.wpi.cs3733d18.teamF.gfx.impl;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.octicons.OctIcon;
import de.jensd.fx.glyphs.octicons.OctIconView;
import edu.wpi.cs3733d18.teamF.Main;
import edu.wpi.cs3733d18.teamF.MapSingleton;
import edu.wpi.cs3733d18.teamF.gfx.NodeDrawable;
import edu.wpi.cs3733d18.teamF.graph.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PathStairNodeDrawer extends NodeDrawable {

    private boolean isSelected = false;
    private boolean isHovered = false;
    private javafx.scene.image.Image upStair = new javafx.scene.image.Image(Main.class.getResource("up-stairs.png").toExternalForm());
    private javafx.scene.image.Image downStair = new javafx.scene.image.Image(Main.class.getResource("down-stairs.png").toExternalForm());
    private boolean direction = true;

    public PathStairNodeDrawer(Node node) {
        super(node);
    }

    public PathStairNodeDrawer() {
        super();
    }

    @Override
    public void setDirection(boolean direction) {
        this.direction = direction;
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
    public void hoverNode() {
        isHovered = true;
    }

    @Override
    public void unhoverNode() {
        isHovered = false;
    }

    @Override
    public void draw(Pane pane) {
        boolean is2D = MapSingleton.getInstance().getMap().is2D();
        double imageWidth = 5000;
        double imageHeight = is2D ? 3400 : 2772;
        double posX = is2D ? node.getPosition().getX() : node.getWireframePosition().getX();
        double posY = is2D ? node.getPosition().getY() : node.getWireframePosition().getY();
        if(!node.getFloor().equals(MapSingleton.getInstance().getMap().getFloor())){
            return;
        }
        Image currIcon;
        if(direction) {
            currIcon = upStair;
        }
        else {
            currIcon = downStair;
        }
        ImageView icon = new ImageView(currIcon);

        if (isHovered) {
            icon.setTranslateX((posX * pane.getMaxWidth() / imageWidth)-112.5);
            icon.setTranslateY((posY * pane.getMaxHeight() / imageHeight)-114);

            icon.setScaleX(0.02);
            icon.setScaleY(0.02);
            icon.setVisible(true);
            pane.getChildren().add(icon);
        } else {
            icon.setTranslateX((posX * pane.getMaxWidth() / imageWidth)-112.5);
            icon.setTranslateY((posY * pane.getMaxHeight() / imageHeight)-114);

            icon.setScaleX(0.015);
            icon.setScaleY(0.015);
            icon.setVisible(true);
            pane.getChildren().add(icon);
        }
    }
}

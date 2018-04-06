package com.github.CS3733_D18_Team_F_Project_0.gfx.impl;

import com.github.CS3733_D18_Team_F_Project_0.MapSingleton;
import com.github.CS3733_D18_Team_F_Project_0.gfx.NodeDrawable;
import com.github.CS3733_D18_Team_F_Project_0.graph.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CircleNodeDrawer extends NodeDrawable {

    private boolean isSelected = false;

    protected CircleNodeDrawer(Node node) {
        super(node);
    }

    public CircleNodeDrawer() {
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

        if(!isSelected){
            Circle circle = new Circle(1.5, Color.RED);
            circle.setCenterX(posX * pane.getMaxWidth() / imageWidth);
            circle.setCenterY(posY * pane.getMaxHeight() / imageHeight);
            pane.getChildren().add(circle);
        }
        else{
            Circle circle = new Circle(2.2, Color.CORNFLOWERBLUE);
            circle.setCenterX(posX * pane.getMaxWidth() / imageWidth);
            circle.setCenterY(posY * pane.getMaxHeight() / imageHeight);
            pane.getChildren().add(circle);
        }

    }
}

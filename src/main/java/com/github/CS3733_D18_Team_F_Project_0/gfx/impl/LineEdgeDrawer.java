package com.github.CS3733_D18_Team_F_Project_0.gfx.impl;

import com.github.CS3733_D18_Team_F_Project_0.MapSingleton;
import com.github.CS3733_D18_Team_F_Project_0.gfx.EdgeDrawable;
import com.github.CS3733_D18_Team_F_Project_0.graph.Edge;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

public class LineEdgeDrawer extends EdgeDrawable {

    Paint color = Color.SILVER;

    public LineEdgeDrawer(Edge edge) {
        super(edge);
    }

    public LineEdgeDrawer() {
        super();
    }
    public LineEdgeDrawer(Paint color) {
        super();
        this.color = color;
    }

    @Override
    public void draw(Pane pane) {
        boolean is2D = MapSingleton.getInstance().getMap().is2D();
        double imageWidth = 5000;
        double imageHeight = is2D ? 3400 : 2772;
        double posX1 = is2D ? edge.getNode1().getPosition().getX() : edge.getNode1().getWireframePosition().getX();
        double posY1 = is2D ? edge.getNode1().getPosition().getY() : edge.getNode1().getWireframePosition().getY();
        double posX2 = is2D ? edge.getNode2().getPosition().getX() : edge.getNode2().getWireframePosition().getX();
        double posY2 = is2D ? edge.getNode2().getPosition().getY() : edge.getNode2().getWireframePosition().getY();

        Line line = new Line();
        line.setStartX(posX1 * pane.getMaxWidth() / imageWidth);
        line.setStartY(posY1 * pane.getMaxHeight() / imageHeight);
        line.setEndX(posX2 * pane.getMaxWidth() / imageWidth);
        line.setEndY(posY2 * pane.getMaxHeight() / imageHeight);
        line.setStroke(color);
        pane.getChildren().add(line);
    }
}

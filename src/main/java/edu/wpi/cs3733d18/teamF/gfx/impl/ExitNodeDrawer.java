package edu.wpi.cs3733d18.teamF.gfx.impl;

import edu.wpi.cs3733d18.teamF.MapSingleton;
import edu.wpi.cs3733d18.teamF.gfx.NodeDrawable;
import edu.wpi.cs3733d18.teamF.graph.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ExitNodeDrawer extends NodeDrawable {

    private boolean isSelected = false;

    public ExitNodeDrawer(Node node) {
        super(node);
    }

    public ExitNodeDrawer() {
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
            Rectangle rectangle = new Rectangle(2,2,Color.DARKRED);
            rectangle.setX((posX * pane.getMaxWidth() / imageWidth)-(rectangle.getWidth()/2.f));
            rectangle.setY((posY * pane.getMaxHeight() / imageHeight)-(rectangle.getHeight()/2.f));
            pane.getChildren().add(rectangle);
        }
        else{
            Rectangle rectangle = new Rectangle(2.8,2.8,Color.RED);
            rectangle.setX((posX * pane.getMaxWidth() / imageWidth)-(rectangle.getWidth()/2.f));
            rectangle.setY((posY * pane.getMaxHeight() / imageHeight)-(rectangle.getHeight()/2.f));
            pane.getChildren().add(rectangle);
        }
    }
}

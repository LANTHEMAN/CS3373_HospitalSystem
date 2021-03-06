package edu.wpi.cs3733d18.teamF.gfx.impl.node;

import edu.wpi.cs3733d18.teamF.graph.MapSingleton;
import edu.wpi.cs3733d18.teamF.gfx.NodeDrawable;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ElevatorNodeDrawer extends NodeDrawable {

    private boolean isSelected = false;
    private boolean isHovered = false;

    public ElevatorNodeDrawer() {
        super();
    }

    @Override
    public void draw(Pane pane) {
        boolean is2D = MapSingleton.getInstance().getMap().is2D();
        double imageWidth = 5000;
        double imageHeight = is2D ? 3400 : 2772;
        double posX = is2D ? node.getPosition().getX() : node.getWireframePosition().getX();
        double posY = is2D ? node.getPosition().getY() : node.getWireframePosition().getY();

        if (isSelected) {
            Rectangle rectangle = new Rectangle(4, 4, Color.LIGHTGREEN);
            rectangle.setX((posX * pane.getMaxWidth() / imageWidth) - (rectangle.getWidth() / 2.f));
            rectangle.setY((posY * pane.getMaxHeight() / imageHeight) - (rectangle.getHeight() / 2.f));
            pane.getChildren().add(rectangle);
        } else if (isHovered) {
            Rectangle rectangle = new Rectangle(5, 5, Color.GREENYELLOW);
            rectangle.setX((posX * pane.getMaxWidth() / imageWidth) - (rectangle.getWidth() / 2.f));
            rectangle.setY((posY * pane.getMaxHeight() / imageHeight) - (rectangle.getHeight() / 2.f));
            pane.getChildren().add(rectangle);
        } else {
            Rectangle rectangle = new Rectangle(3, 3, Color.GREEN);
            rectangle.setX((posX * pane.getMaxWidth() / imageWidth) - (rectangle.getWidth() / 2.f));
            rectangle.setY((posY * pane.getMaxHeight() / imageHeight) - (rectangle.getHeight() / 2.f));
            pane.getChildren().add(rectangle);
        }
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
}

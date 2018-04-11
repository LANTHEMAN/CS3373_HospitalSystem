package edu.wpi.cs3733d18.teamF.gfx.impl;

import com.sun.javafx.font.Glyph;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import edu.wpi.cs3733d18.teamF.MapSingleton;
import edu.wpi.cs3733d18.teamF.gfx.NodeDrawable;
import edu.wpi.cs3733d18.teamF.graph.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class StartNodeDrawer extends NodeDrawable {
    private boolean isSelected = false;

    public StartNodeDrawer(Node node) {
        super(node);
    }

    public StartNodeDrawer() {
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
        if(!node.getFloor().equals(MapSingleton.getInstance().getMap().getFloor())){
            return;
        }

        if(!isSelected){
            Circle circle = new Circle(2, Color.GREEN);
            circle.setCenterX(posX * pane.getMaxWidth() / imageWidth);
            circle.setCenterY(posY * pane.getMaxHeight() / imageHeight);
            pane.getChildren().add(circle);
        }
        else{
            Circle circle = new Circle(2, Color.GREEN);
            circle.setCenterX(posX * pane.getMaxWidth() / imageWidth);
            circle.setCenterY(posY * pane.getMaxHeight() / imageHeight);
            pane.getChildren().add(circle);
        }
    }
}

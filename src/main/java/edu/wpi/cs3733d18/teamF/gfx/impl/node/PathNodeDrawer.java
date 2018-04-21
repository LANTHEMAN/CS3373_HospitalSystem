package edu.wpi.cs3733d18.teamF.gfx.impl.node;

import edu.wpi.cs3733d18.teamF.Main;
import edu.wpi.cs3733d18.teamF.graph.MapSingleton;
import edu.wpi.cs3733d18.teamF.gfx.NodeDrawable;
import edu.wpi.cs3733d18.teamF.graph.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class PathNodeDrawer extends NodeDrawable {

    private boolean isSelected = false;
    private boolean isHovered = false;
    private javafx.scene.image.Image upStair = new javafx.scene.image.Image(Main.class.getResource("up-stairs.png").toExternalForm());
    private javafx.scene.image.Image downStair = new javafx.scene.image.Image(Main.class.getResource("down-stairs.png").toExternalForm());
    private javafx.scene.image.Image upElevator = new javafx.scene.image.Image(Main.class.getResource("up-elevator.png").toExternalForm());
    private javafx.scene.image.Image downElevator = new javafx.scene.image.Image(Main.class.getResource("down-elevator.png").toExternalForm());
    private boolean direction = true;
    private boolean type = true;

    public PathNodeDrawer(Node node) {
        super(node);
    }

    public PathNodeDrawer() {
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

    public void setType(boolean type){
        this.type = type;
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
        if(type) {
            if (direction) {
                currIcon = upStair;
            } else {
                currIcon = downStair;
            }
        }
        else{
            if (direction) {
                currIcon = upElevator;
            } else {
                currIcon = downElevator;
            }
        }
        ImageView icon = new ImageView(currIcon);

        if (isHovered) {
            if(type) {
                icon.setTranslateX((posX * pane.getMaxWidth() / imageWidth) - 112.5);
                icon.setTranslateY((posY * pane.getMaxHeight() / imageHeight) - 114);
                icon.setScaleX(0.03);
                icon.setScaleY(0.03);
            }
            else
            {
                icon.setTranslateX((posX * pane.getMaxWidth() / imageWidth) - 87.5);
                icon.setTranslateY((posY * pane.getMaxHeight() / imageHeight) - 128);
                icon.setScaleX(0.04);
                icon.setScaleY(0.04);
            }
            icon.setVisible(true);
            pane.getChildren().add(icon);
        } else {
            if(type) {
                icon.setTranslateX((posX * pane.getMaxWidth() / imageWidth) - 112.5);
                icon.setTranslateY((posY * pane.getMaxHeight() / imageHeight) - 114);
                icon.setScaleX(0.02);
                icon.setScaleY(0.02);
            }
            else{
                icon.setTranslateX((posX * pane.getMaxWidth() / imageWidth) - 87.5);
                icon.setTranslateY((posY * pane.getMaxHeight() / imageHeight) - 128);
                icon.setScaleX(0.03);
                icon.setScaleY(0.03);
            }
            icon.setVisible(true);
            pane.getChildren().add(icon);
        }
    }
}

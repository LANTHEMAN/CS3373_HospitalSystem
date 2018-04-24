package edu.wpi.cs3733d18.teamF.controller.page.element.pacman;

import edu.wpi.cs3733d18.teamF.Main;
import edu.wpi.cs3733d18.teamF.gfx.Drawable;
import edu.wpi.cs3733d18.teamF.graph.MapSingleton;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
enum Direction {UP, DOWN, LEFT, RIGHT}

public class PacmanDrawable implements Drawable {

    private double posX,posY;
    private Direction currDirection;


    public PacmanDrawable(double posX, double posY, Direction d) {
        this.posX = posX;
        this.posY = posY;
        currDirection = d;
    }

    public PacmanDrawable() {
        this.posX = 0;
        this.posY = 0;
        this.currDirection = Direction.UP;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public void setDirection(Direction currDirection) {
        this.currDirection = currDirection;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    @Override
    public void draw(Pane pane) {

        Image icon =  new Image(Main.class.getResource("pacman.gif").toExternalForm());
        javafx.scene.image.ImageView iconView = new javafx.scene.image.ImageView(icon);
        switch(currDirection){
            case UP:
                iconView.setRotate(270);
                break;
            case LEFT:
                iconView.setRotate(180);
                break;
            case DOWN:
                iconView.setRotate(90);
                break;
            case RIGHT:
            default:
                iconView.setRotate(0);
                break;
        }

        boolean is2D = MapSingleton.getInstance().getMap().is2D();
        double imageWidth = 5000;
        double imageHeight = is2D ? 3400 : 2772;

        iconView.setTranslateX((posX * pane.getMaxWidth() / imageWidth) - 100);
        iconView.setTranslateY((posY * pane.getMaxHeight() / imageHeight) - 100);
        iconView.setScaleX(0.03);
        iconView.setScaleY(0.03);
        iconView.setVisible(true);
        pane.getChildren().add(iconView);
    }
}

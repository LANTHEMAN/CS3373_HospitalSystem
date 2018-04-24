package edu.wpi.cs3733d18.teamF.gfx.pacman;

import edu.wpi.cs3733d18.teamF.Main;
import edu.wpi.cs3733d18.teamF.gfx.Drawable;
import edu.wpi.cs3733d18.teamF.gfx.MapDrawable;
import edu.wpi.cs3733d18.teamF.graph.MapSingleton;
import edu.wpi.cs3733d18.teamF.graph.Node;
import edu.wpi.cs3733d18.teamF.graph.Path;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.util.HashSet;
import java.util.Random;

enum Direction {UP, DOWN, LEFT, RIGHT}

public class PacmanDrawable extends MapDrawable implements Drawable {

    private double posX,posY;
    private String floor;
    private Direction currDirection;


    public PacmanDrawable(double posX, double posY, Direction d) {
        this.posX = posX;
        this.posY = posY;
        currDirection = d;
    }

    public PacmanDrawable() {
        this.posX = 0;
        this.posY = 0;
        this.floor = "L1";
        this.currDirection = Direction.UP;
    }

    @Override
    public void selectNode(Node node) {

    }

    @Override
    public void unselectNode() {

    }

    @Override
    public void showPath(Path path) {

    }

    @Override
    public void redrawPath(Path path) {

    }

    @Override
    public void unshowPath() {

    }

    @Override
    public void showNodes() {

    }

    @Override
    public void unshowNodes() {

    }

    @Override
    public void showEdges() {

    }

    @Override
    public void unshowEdges() {

    }

    @Override
    public void addDefaultStartNode(Node node) {

    }

    @Override
    public void hoverNode(Node node) {

    }

    @Override
    public void unhoverNode() {

    }

    @Override
    public void update3DPathDisplay(boolean showAllFloors) {

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

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setRandom(){
        Random rand = new Random();
        boolean is2D = MapSingleton.getInstance().getMap().is2D();
        HashSet<Node> nodes = MapSingleton.getInstance().getMap().getNodes();
        Node node = null;
        int i = rand.nextInt(nodes.size());
        int j = 0;
        for(Node n : nodes){
            if(i == j){
                node = n;
                break;
            }
            j++;
        }
        posX = is2D ? node.getPosition().getX() : node.getWireframePosition().getX();
        posY = is2D ? node.getPosition().getY() : node.getWireframePosition().getY();
        floor = node.getFloor();
        switch(rand.nextInt(4)){
            case 0:
                currDirection = Direction.UP;
                break;
            case 1:
                currDirection = Direction.DOWN;
                break;
            case 2:
                currDirection = Direction.LEFT;
                break;
            default:
                currDirection = Direction.RIGHT;
                break;
        }
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

package edu.wpi.cs3733d18.teamF.controller.page.element.mapView;

import edu.wpi.cs3733d18.teamF.graph.Map;
import edu.wpi.cs3733d18.teamF.gfx.MapDrawable;
import edu.wpi.cs3733d18.teamF.gfx.PaneController;
import edu.wpi.cs3733d18.teamF.graph.Node;
import edu.wpi.cs3733d18.teamF.graph.Path;
import javafx.scene.layout.Pane;

import java.awt.Rectangle;

import javafx.geometry.Point2D;

import java.util.Observable;
import java.util.Observer;

public class PaneMapController extends PaneController implements Observer {

    private MapDrawable mapDrawer;
    public Path drawnPath = null;
    Node hoveredNode = null;

    PaneMapController(Pane root, Map map, MapDrawable mapDrawer) {
        super(root);
        this.mapDrawer = mapDrawer;
        map.addObserver(this);

        mapDrawer.update(map);

        addDrawable(mapDrawer);

        draw();
    }

    public void refresh() {
        clear();
        draw();
    }

    public Rectangle getPathBoundingBox() {
        Rectangle bBox = new Rectangle();
        double upperLeftX = 5000;
        double upperLeftY = 2772;
        double bottomRightX = 0;
        double bottomRightY = 0;

        if (drawnPath != null) {
            for (Node node : drawnPath.getNodes()) {
                if (node.getPosition().getX() < upperLeftX) {
                    upperLeftX = node.getPosition().getX();
                }

                if (node.getPosition().getY() < upperLeftY) {
                    upperLeftY = node.getPosition().getY();
                }

                if (node.getPosition().getX() > bottomRightX) {
                    bottomRightX = node.getPosition().getX();
                }

                if (node.getPosition().getY() > bottomRightY) {
                    bottomRightY = node.getPosition().getY();
                }
            }
        }

        double gestureAspect = 844.0/578.0;

        bBox.x = (int) upperLeftX;
        bBox.y = (int) upperLeftY;
        bBox.height = (int) (bottomRightY - upperLeftY);
        bBox.width = (int) (bottomRightX - upperLeftX);

        double multiple = gestureAspect * (bBox.getHeight()/bBox.getWidth());
        System.out.println("multiple = " + multiple);

        if(multiple > 1){
            bBox.height *= multiple;
        }else{
            bBox.width *= multiple;
        }

        return bBox;
    }

    public void selectNode(Node node) {
        mapDrawer.selectNode(node);
        refresh();
    }

    public void refreshPath() {
        if (drawnPath == null) {
            return;
        }
        mapDrawer.redrawPath(drawnPath);
        refresh();
    }

    public void showPath(Path path) {
        mapDrawer.showPath(path);
        this.drawnPath = path;
        if (path.getNodes().size() > 0) {
            addDefaultStartNode(path.getNodes().get(0));
        }
        refresh();
    }

    public void unshowPath() {
        mapDrawer.unshowPath();
        this.drawnPath = null;
        refresh();
    }

    public Path getDrawnPath() {
        return drawnPath;
    }

    public void showNodes() {
        mapDrawer.showNodes();
        refresh();
    }

    public void showEdges() {
        mapDrawer.showEdges();
        refresh();
    }

    public void unshowEdges() {
        mapDrawer.unshowEdges();
        refresh();
    }

    public void unshowNodes() {
        mapDrawer.unshowNodes();
        refresh();
    }

    public void unselectNode() {
        mapDrawer.unselectNode();
        refresh();
    }

    void addDefaultStartNode(Node node) {
        mapDrawer.addDefaultStartNode(node);
        refresh();
    }

    void hoverNode(Node node) {
        if (hoveredNode == node) {
            return;
        }
        hoveredNode = node;
        mapDrawer.hoverNode(node);
        refresh();
    }

    void unhoverNode() {
        hoveredNode = null;
        mapDrawer.unhoverNode();
        refresh();
    }

    void update3DPathDisplay(boolean showAllFloors){
        mapDrawer.update3DPathDisplay(showAllFloors);
        refresh();
    }

    boolean isHoveringNode(Node node){
        return node == hoveredNode;
    }


    @Override
    public void update(Observable o, Object arg) {
        refresh();
    }
}

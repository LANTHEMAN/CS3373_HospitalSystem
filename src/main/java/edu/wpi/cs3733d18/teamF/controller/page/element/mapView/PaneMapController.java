package edu.wpi.cs3733d18.teamF.controller.page.element.mapView;

import edu.wpi.cs3733d18.teamF.gfx.MapDrawable;
import edu.wpi.cs3733d18.teamF.gfx.PaneController;
import edu.wpi.cs3733d18.teamF.graph.Map;
import edu.wpi.cs3733d18.teamF.graph.Node;
import edu.wpi.cs3733d18.teamF.graph.Path;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class PaneMapController extends PaneController implements Observer {

    public Path drawnPath = null;
    Node hoveredNode = null;
    private MapDrawable mapDrawer;

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

    public Rectangle getPathBoundingBox(Path path, boolean is2D) {
        Rectangle bBox = new Rectangle();
        double upperLeftX = 5000;
        double upperLeftY = 2772;
        double bottomRightX = 0;
        double bottomRightY = 0;

        Rectangle scaledRect = new Rectangle();

        if (is2D) {
            if (path != null && path.getNodes().size() > 0) {
                for (Node node : path.getNodes()) {
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

            bBox.x = (int) upperLeftX;
            bBox.y = (int) upperLeftY;
            bBox.height = (int) (bottomRightY - upperLeftY);
            bBox.width = (int) (bottomRightX - upperLeftX);

            scaledRect.width = (int) (bBox.getWidth() * 844.f / 5000.f);
            scaledRect.height = (int) (bBox.getHeight() * 578.f / 3400.f);
            scaledRect.x = (int) (bBox.getX() * 844.f / 5000.f);
            scaledRect.y = (int) (bBox.getY() * 578.f / 3400.f);
        } else {
            if (path != null && path.getNodes().size() > 0) {
                for (Node node : path.getNodes()) {
                    if (node.getPosition().getX() < upperLeftX) {
                        upperLeftX = node.getWireframePosition().getX();
                    }

                    if (node.getPosition().getY() < upperLeftY) {
                        upperLeftY = node.getWireframePosition().getY();
                    }

                    if (node.getPosition().getX() > bottomRightX) {
                        bottomRightX = node.getWireframePosition().getX();
                    }

                    if (node.getPosition().getY() > bottomRightY) {
                        bottomRightY = node.getWireframePosition().getY();
                    }
                }
            }

            bBox.x = (int) upperLeftX;
            bBox.y = (int) upperLeftY;
            bBox.height = (int) (bottomRightY - upperLeftY);
            bBox.width = (int) (bottomRightX - upperLeftX);

            scaledRect.width = (int) (bBox.getWidth() * 844.f / 5000.f);
            scaledRect.height = (int) (bBox.getHeight() * 578.f / 2774.f);
            scaledRect.x = (int) (bBox.getX() * 844.f / 5000.f);
            scaledRect.y = (int) (bBox.getY() * 578.f / 2774.f);
        }

        double aspectRatio = 844.f / 578.f;
        double multiple = aspectRatio * scaledRect.getHeight() / scaledRect.getWidth();

        if (multiple > 1) {
            double preWidth = scaledRect.getWidth();
            scaledRect.width *= multiple;
            scaledRect.x -= (scaledRect.getWidth() - preWidth) / 2;
        } else {
            double preHeight = scaledRect.getHeight();
            scaledRect.height *= (1 / multiple);
            scaledRect.y -= (scaledRect.getHeight() - preHeight) / 2;
        }

        return scaledRect;
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

    void update3DPathDisplay(boolean showAllFloors) {
        mapDrawer.update3DPathDisplay(showAllFloors);
        refresh();
    }

    boolean isHoveringNode(Node node) {
        return node == hoveredNode;
    }


    @Override
    public void update(Observable o, Object arg) {
        refresh();
    }
}

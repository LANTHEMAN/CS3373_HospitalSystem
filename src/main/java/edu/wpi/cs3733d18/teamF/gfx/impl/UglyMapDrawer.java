package edu.wpi.cs3733d18.teamF.gfx.impl;

import edu.wpi.cs3733d18.teamF.Map;
import edu.wpi.cs3733d18.teamF.gfx.EdgeDrawable;
import edu.wpi.cs3733d18.teamF.gfx.MapDrawable;
import edu.wpi.cs3733d18.teamF.gfx.NodeDrawable;
import edu.wpi.cs3733d18.teamF.gfx.PathDrawable;
import edu.wpi.cs3733d18.teamF.graph.Edge;
import edu.wpi.cs3733d18.teamF.graph.Node;
import edu.wpi.cs3733d18.teamF.graph.Path;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class UglyMapDrawer extends MapDrawable {

    private Point2D selectedNodePos = null;
    private Path path = null;
    private boolean showNodes = false;
    private boolean showEdges = false;

    private EdgeDrawable edgeDrawer = new LineEdgeDrawer();
    private NodeDrawable nodeDrawer = new CircleNodeDrawer();
    private NodeDrawable elevatorDrawer = new ElevatorNodeDrawer();
    private NodeDrawable currNodeDrawable = nodeDrawer;
    private PathDrawable pathDrawer = new StalePathDrawer(new LineEdgeDrawer(Color.DEEPPINK));

    public UglyMapDrawer(Map map) {
        super(map);
    }

    public UglyMapDrawer() {
        super();
    }

    @Override
    public void selectNode(Node node) {
        selectedNodePos = new Point2D(node.getPosition().getX(), node.getPosition().getY());
    }

    @Override
    public void unselectNode() {
        selectedNodePos = null;
    }

    @Override
    public void showPath(Path path) {
        this.path = path;
    }

    @Override
    public void unshowPath() {
        path = null;
    }

    @Override
    public void showNodes() {
        showNodes = true;
    }

    @Override
    public void unshowNodes() {
        showNodes = false;
    }

    @Override
    public void showEdges() {
        showEdges = true;
    }

    @Override
    public void unshowEdges() {
        showEdges = false;
    }

    @Override
    public void draw(Pane pane) {
        Node selectedNode = null;
        if (selectedNodePos != null) {
            selectedNode = map.findNodeClosestTo(selectedNodePos.getX(), selectedNodePos.getY(), true);
        }

        if (showEdges) {
            for (Edge edge : map.getEdges(edge -> edge.getNode2().getFloor().equals(map.getFloor()))) {
                edgeDrawer.update(edge);
                edgeDrawer.draw(pane);
            }
        }

        if (path != null) {
            pathDrawer.update(path);
            pathDrawer.draw(pane);
        }
        for (Node node : map.getNodes(node -> node.getFloor().equals(map.getFloor()))) {
            switch (node.getNodeType()) {
                case "ELEV":
                    currNodeDrawable = elevatorDrawer;
                    break;
                default:
                    currNodeDrawable = nodeDrawer;
                    break;
            }
            currNodeDrawable.update(node);
            if (selectedNode == node) {
                currNodeDrawable.selectNode();
            } else if (!showNodes) {
                continue;
            }
            currNodeDrawable.draw(pane);
            if (selectedNode == node) {
                currNodeDrawable.unselectNode();

            }
        }
    }
}

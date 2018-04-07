package com.github.CS3733_D18_Team_F_Project_0.gfx.impl;

import com.github.CS3733_D18_Team_F_Project_0.Map;
import com.github.CS3733_D18_Team_F_Project_0.gfx.EdgeDrawable;
import com.github.CS3733_D18_Team_F_Project_0.gfx.MapDrawable;
import com.github.CS3733_D18_Team_F_Project_0.gfx.NodeDrawable;
import com.github.CS3733_D18_Team_F_Project_0.gfx.PathDrawable;
import com.github.CS3733_D18_Team_F_Project_0.graph.Edge;
import com.github.CS3733_D18_Team_F_Project_0.graph.Node;
import com.github.CS3733_D18_Team_F_Project_0.graph.Path;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class UglyMapDrawer extends MapDrawable {

    private Point2D selectedNodePos = null;
    private Path path = null;

    private EdgeDrawable edgeDrawer = new LineEdgeDrawer();
    private NodeDrawable nodeDrawer = new CircleNodeDrawer();
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
    public void draw(Pane pane) {
        Node selectedNode = null;
        if (selectedNodePos != null) {
            selectedNode = map.findNodeClosestTo(selectedNodePos.getX(), selectedNodePos.getY(), true);
        }

        for (Edge edge : map.getEdges(edge -> edge.getNode2().getFloor().equals(map.getFloor()))) {
            edgeDrawer.update(edge);
            edgeDrawer.draw(pane);
        }

        if (path != null) {
            pathDrawer.update(path);
            pathDrawer.draw(pane);
        }


        for (Node node : map.getNodes(node -> node.getFloor().equals(map.getFloor()))) {
            nodeDrawer.update(node);

            if (selectedNode == node) {
                nodeDrawer.selectNode();
            }

            nodeDrawer.draw(pane);

            nodeDrawer.unselectNode();
        }

    }
}

package edu.wpi.cs3733d18.teamF.gfx.impl;

import edu.wpi.cs3733d18.teamF.gfx.EdgeDrawable;
import edu.wpi.cs3733d18.teamF.gfx.MapDrawable;
import edu.wpi.cs3733d18.teamF.gfx.NodeDrawable;
import edu.wpi.cs3733d18.teamF.gfx.PathDrawable;
import edu.wpi.cs3733d18.teamF.graph.Edge;
import edu.wpi.cs3733d18.teamF.graph.Node;
import edu.wpi.cs3733d18.teamF.graph.Path;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

public class UglyMapDrawer extends MapDrawable {

    boolean redrawPath = true;
    private Point2D selectedNodePos = null;
    private Point2D hoveredNodePos = null;
    private Path path = null;
    private boolean showNodes = false;
    private boolean showEdges = false;
    private EdgeDrawable edgeDrawer = new LineEdgeDrawer();
    private NodeDrawable nodeDrawer = new CircleNodeDrawer();
    private PathDrawable pathDrawer3 = new DynamicPathDrawer();
    private PathDrawable pathDrawer = pathDrawer3;
    private NodeDrawable elevatorDrawer = new ElevatorNodeDrawer();
    private NodeDrawable exitDrawer = new ExitNodeDrawer();
    private NodeDrawable stairDrawer = new StairNodeDrawer();
    private NodeDrawable restroomDrawer = new RestroomNodeDrawer();
    private NodeDrawable pathNodeDrawer = new PathNodeDrawer();
    private NodeDrawable currNodeDrawable = nodeDrawer;
    private NodeDrawable startNodeDefault = null;
    private Pane pathPane = new Pane();

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
        if (this.path != path) {
            redrawPath = true;
        }
        this.path = path;
    }

    @Override
    public void redrawPath(Path path) {
        this.path = path;
        redrawPath = true;
    }

    @Override
    public void unshowPath() {
        redrawPath = true;
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
    public void addDefaultStartNode(Node node) {
        if (node == null) {
            startNodeDefault = null;
        } else {
            startNodeDefault = new StartNodeDrawer(node);
        }

    }

    @Override
    public void hoverNode(Node node) {
        hoveredNodePos = new Point2D(node.getPosition().getX(), node.getPosition().getY());
    }

    @Override
    public void unhoverNode() {
        hoveredNodePos = null;
    }

    @Override
    public void draw(Pane pane) {
        boolean is2D = map.is2D();

        Node selectedNode = null;
        Node hoveredNode = null;
        if (selectedNodePos != null) {
            selectedNode = map.findNodeClosestTo(selectedNodePos.getX(), selectedNodePos.getY(), true);
        }
        if (hoveredNodePos != null) {
            hoveredNode = map.findNodeClosestTo(hoveredNodePos.getX(), hoveredNodePos.getY(), true);
        }

        if (showEdges) {
            for (Edge edge : map.getEdges(edge -> edge.getNode2().getFloor().equals(map.getFloor()))) {
                if (edge.getNode1().getNodeType().equals("ELEV") && edge.getNode2().getNodeType().equals("ELEV")) {
                    continue;
                } else if (edge.getNode1().getNodeType().equals("STAI") && edge.getNode2().getNodeType().equals("STAI")) {
                    continue;
                }
                edgeDrawer.update(edge);
                edgeDrawer.draw(pane);
            }
        }

        if (path != null && path.getNodes().size() > 0) {
            for (Edge edge : path.getEdges()) {
                    if (!(edge.getNode1().getFloor().equals(edge.getNode2().getFloor()))) {
                        if (edge.getNode1().getFloor().equals(map.getFloor()) || !is2D) {
                            Node node = edge.getNode1();
                            currNodeDrawable = getPathNodeDrawer(node.getNodeType());
                            if(node.compareFloors(edge.getNode2()) == -1){
                                currNodeDrawable.setDirection(true);
                            }
                            else{
                                currNodeDrawable.setDirection(false);
                            }
                            currNodeDrawable.update(node);
                            if (hoveredNode == node) {
                                currNodeDrawable.hoverNode();
                            }
                            currNodeDrawable.draw(pane);
                            if (hoveredNode == node) {
                                currNodeDrawable.unhoverNode();
                            }
                        }
                        if (edge.getNode2().getFloor().equals(map.getFloor())  || !is2D) {
                            Node node = edge.getNode2();
                            currNodeDrawable = getPathNodeDrawer(node.getNodeType());
                            if(node.compareFloors(edge.getNode1()) == -1){
                                currNodeDrawable.setDirection(true);
                            }
                            else{
                                currNodeDrawable.setDirection(false);
                            }
                            currNodeDrawable.update(node);
                            if (hoveredNode == node) {
                                currNodeDrawable.hoverNode();
                            }
                            currNodeDrawable.draw(pane);
                            if (hoveredNode == node) {
                                currNodeDrawable.unhoverNode();
                            }
                        }

                    }

            }

            pane.getChildren().add(pathPane);
            pathPane.maxWidthProperty().bind(pane.maxWidthProperty());
            pathPane.maxHeightProperty().bind(pane.maxHeightProperty());
            if (redrawPath) {
                pathPane.getChildren().clear();

                pathDrawer.update(path);
                pathDrawer.draw(pathPane);

                Node startNode = path.getNodes().get(0);
                NodeDrawable startIconDrawer = new StartNodeDrawer(startNode);
                startIconDrawer.draw(pathPane);

                Node endNode = path.getNodes().get(path.getNodes().size() - 1);
                NodeDrawable endIconDrawer = new EndNodeDrawer();
                endIconDrawer.update(endNode);
                endIconDrawer.draw(pathPane);
                redrawPath = false;
            }

        } else {
            if (startNodeDefault != null) {
                startNodeDefault.draw(pane);
            }
        }
        for (Node node : map.getNodes(node -> node.getFloor().equals(map.getFloor()))) {
            currNodeDrawable = getDrawer(node.getNodeType());
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


    //TODO: Implement drawing for all node types
    private NodeDrawable getDrawer(String type) {
        switch (type) {
            case "ELEV":
                return elevatorDrawer;
            case "EXIT":    //exits or entrances
                return exitDrawer;
            case "STAI":    //stairs
                return stairDrawer;
            case "REST":    //restroom
                return restroomDrawer;
            case "DEPT":    //medical departments, clinics, and waiting room areas
            case "LABS":    //labs, imaging centers, and medical testing areas
            case "INFO":    //information desks, security desks, lost and found
            case "CONF":    //conference room
            case "RETL":    //shops, food, pay phone, areas that provide non-medical services for immediate payment
            case "SERV":    //hospital non-medical services, interpreters, shuttles, spiritual, library, patient financial, etc.
            default:    //will be hallways and anything not implemented
                return nodeDrawer;
        }
    }
    private NodeDrawable getPathNodeDrawer(String type) {
        switch (type) {
            case "ELEV":
                pathNodeDrawer.setType(false);
                return pathNodeDrawer;
            case "STAI":
                pathNodeDrawer.setType(true);
                return pathNodeDrawer;
            default:
                return nodeDrawer;
        }
    }

}

package edu.wpi.cs3733d18.teamF.gfx.impl.map;

import edu.wpi.cs3733d18.teamF.gfx.*;
import edu.wpi.cs3733d18.teamF.gfx.impl.edge.LineEdgeDrawer;
import edu.wpi.cs3733d18.teamF.gfx.impl.node.*;
import edu.wpi.cs3733d18.teamF.gfx.impl.path.DynamicPathDrawer;
import edu.wpi.cs3733d18.teamF.gfx.impl.pacman.GameMapDrawer;
import edu.wpi.cs3733d18.teamF.graph.Edge;
import edu.wpi.cs3733d18.teamF.graph.Node;
import edu.wpi.cs3733d18.teamF.graph.Path;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

public class UglyMapDrawer extends MapDrawable {

    private boolean redrawPath = true;
    private boolean drawPathOnAllFloors = false;
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
    private GameMapDrawer gameMapDrawer = new GameMapDrawer();
    private NodeDrawable currNodeDrawable = nodeDrawer;
    private NodeDrawable startNodeDefault = null;
    private Pane pathPane = new Pane();


    public UglyMapDrawer() {
        super();
        gameMapDrawer.setRandom();
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
    public void update3DPathDisplay(boolean showAllFloors) {
        redrawPath = true;
        drawPathOnAllFloors = showAllFloors;
    }

    @Override
    public void draw(Pane pane) {
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
                if (edge.getNode1().getNodeType().equals(Node.Type.ELEVATOR) && edge.getNode2().getNodeType().equals(Node.Type.ELEVATOR)) {
                    continue;
                } else if (edge.getNode1().getNodeType().equals(Node.Type.STAIR) && edge.getNode2().getNodeType().equals(Node.Type.STAIR)) {
                    continue;
                }
                edgeDrawer.update(edge);
                edgeDrawer.draw(pane);
            }
        }

        // iterate over every node of the path, drawing all of the elevators
        if (path != null && path.getNodes().size() > 0) {
            for (int i = 0; i < path.getNodes().size(); i++) {
                Node node = path.getNodes().get(i);
                if (node.getNodeType().equals(Node.Type.STAIR) || node.getNodeType().equals(Node.Type.ELEVATOR)) {
                    String nodeType = node.getNodeType();

                    currNodeDrawable = pathNodeDrawer;
                    // NOTE: PathNodeDrawer is for drawing elevators or stairs!
                    // TODO change name
                    PathNodeDrawer stairElevatorDrawer = (PathNodeDrawer) pathNodeDrawer;
                    stairElevatorDrawer.setType(nodeType);

                    Node linkedNode = null;
                    if (i > 0) {
                        if (path.getNodes().get(i - 1).getNodeType().equals(nodeType)) {
                            linkedNode = path.getNodes().get(i - 1);
                            if (node.compareFloors(linkedNode) == -1) {
                                stairElevatorDrawer.setDirection(PathNodeDrawer.Direction.UP);
                            } else {
                                stairElevatorDrawer.setDirection(PathNodeDrawer.Direction.DOWN);
                            }
                        }
                    }
                    if (i < path.getNodes().size() - 1) {
                        if (path.getNodes().get(i + 1).getNodeType().equals(nodeType)) {
                            linkedNode = path.getNodes().get(i + 1);
                            if (node.compareFloors(linkedNode) == -1) {
                                stairElevatorDrawer.setDirection(PathNodeDrawer.Direction.UP);
                            } else {
                                stairElevatorDrawer.setDirection(PathNodeDrawer.Direction.DOWN);
                            }
                        }
                    }

                    // don't draw the elevator if it does not connect to another floor!
                    if (linkedNode == null || (linkedNode.getFloor().equals(node.getFloor()))) {
                        continue;
                    }

                    // only draw nodes on the current floor unless specified otherwise
                    if (!node.getFloor().equals(map.getFloor()) && (!drawPathOnAllFloors || map.is2D())) {
                        continue;
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

            pane.getChildren().add(pathPane);
            pathPane.maxWidthProperty().bind(pane.maxWidthProperty());
            pathPane.maxHeightProperty().bind(pane.maxHeightProperty());

            // dont draw the path if its empty
            if(path.getEdges().size() == 0){
                redrawPath = false;
                pathPane.getChildren().clear();
            }

            if (redrawPath) {
                pathPane.getChildren().clear();

                pathDrawer.update(path);
                if (pathDrawer instanceof DynamicPathDrawer) {
                    DynamicPathDrawer dyPath = (DynamicPathDrawer) pathDrawer;
                    dyPath.updateFloorSelection(drawPathOnAllFloors);
                }
                pathDrawer.draw(pathPane);

                Node startNode = path.getNodes().get(0);
                if (startNode.getFloor().equals(map.getFloor()) || (!map.is2D() && drawPathOnAllFloors)) {
                    NodeDrawable startIconDrawer = new StartNodeDrawer(startNode);
                    startIconDrawer.draw(pathPane);
                }

                Node endNode = path.getNodes().get(path.getNodes().size() - 1);
                if (endNode.getFloor().equals(map.getFloor()) || (!map.is2D() && drawPathOnAllFloors)) {
                    NodeDrawable endIconDrawer = new EndNodeDrawer(endNode);
                    endIconDrawer.draw(pathPane);
                }

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

}

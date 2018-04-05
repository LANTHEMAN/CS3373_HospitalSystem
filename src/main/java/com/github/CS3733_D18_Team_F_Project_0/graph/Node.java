package com.github.CS3733_D18_Team_F_Project_0.graph;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

import java.util.Observable;

public class Node extends Observable {
    // the database ID of this node
    private final String nodeID;
    // the name of the floor where this node is located
    private final String floor;
    // the type of location this node is at
    private final String nodeType;
    // actual 3d position of node
    private Point3D position;
    // position of the node on the wireframe map
    private Point2D wireframePosition;
    // extra weight to cause A* to more likely avoid this node
    private double additionalWeight = 0;
    // the name of the building this node is located in
    private String building;
    // an abbreviation of the name of this node
    private String shortName;
    // full name of this node
    private String longName;

    Node(Point3D position, Point2D wireframePosition, double additionalWeight, String nodeID, String floor, String building
            , String nodeType, String shortName, String longName) {
        this.position = position;
        this.wireframePosition = wireframePosition;
        this.additionalWeight = additionalWeight;
        this.nodeID = nodeID;
        this.floor = floor;
        this.building = building;
        this.nodeType = nodeType;
        this.shortName = shortName;
        this.longName = longName;
    }

    /**
     * @return the id string of the node
     */
    public String getNodeID() {
        return nodeID;
    }

    private void setNodeID(String newNodeID) {
        // deletes this node and creates a new one
        signalClassChanged(newNodeID);
        // this node should be 'deleted' now
    }

    /**
     * Find the cartesian distance from this node to another node
     *
     * @param node The node to find the distance to
     * @return The distance from this node to node
     */
    public double displacementTo(Node node) {
        return (this.position.distance(node.position));
    }

    /**
     * Get the additional weight of this node
     *
     * @return The additional weight
     */
    public double getAdditionalWeight() {
        return additionalWeight;
    }

    /**
     * Set an additional weight for this node to make AStar avoid it
     *
     * @param additionalWeight The additional weight to add
     */
    public void setAdditionalWeight(double additionalWeight) {
        this.additionalWeight = additionalWeight;
    }

    /**
     * @return the floor of this node
     */
    public String getFloor() {
        return floor;
    }

    /**
     * @return the position of this node
     */
    public Point3D getPosition() {
        return position;
    }

    /**
     * @param position the new position of this node
     */
    public void setPosition(Point3D position) {
        this.position = position;
        signalClassChanged();
    }

    /**
     * @return the wireframe map position of this node
     */
    public Point2D getWireframePosition() {
        return wireframePosition;
    }

    /**
     * @param wireframePosition the new wireframe map position of this node
     */
    public void setWireframePosition(Point2D wireframePosition) {
        this.wireframePosition = wireframePosition;
        signalClassChanged();
    }

    /**
     * @return the building of this node
     */
    public String getBuilding() {
        return building;
    }

    /**
     * @param building the new building of this node
     */
    public void setBuilding(String building) {
        this.building = building;
        signalClassChanged();
    }

    /**
     * @return the node type of this node
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     * @param nodeType the new node type of this node
     */
    public void setNodeType(String nodeType, int nodeTypeCount) {
        if (!(NodeBuilder.getNodeTypes().contains(nodeType))
                || nodeType.equals("ELEV")) {
            throw new AssertionError("The nodeType was invalid.");
        }
        if (nodeTypeCount > 999 || nodeTypeCount < 0) {
            throw new AssertionError("The nodeTypeCount was out of bounds.");
        }

        // notify the change in nodeID
        String newNodeID = nodeID.substring(0, 1)
                + nodeType
                + String.format("%03d", nodeTypeCount)
                + nodeID.substring(8);
        setNodeID(newNodeID);
    }

    public void setNodeType(String nodeType, char elevatorChar) {
        if (!(nodeType.equals("ELEV"))) {
            throw new AssertionError("The nodeType was invalid.");
        }
        if (!(Character.isLetter(elevatorChar))) {
            throw new AssertionError("You must assign a valid elevator character!");
        }

        // notify the change in nodeID
        String newNodeID = nodeID.substring(0, 1)
                + nodeType
                + "00" + elevatorChar
                + nodeID.substring(8);
        setNodeID(newNodeID);
    }

    /**
     * @return the short name of this node
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * @param shortName the new short name of this node
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
        signalClassChanged();
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
        signalClassChanged();
    }

    private void signalClassChanged() {
        this.setChanged();
        this.notifyObservers();
    }

    private void signalClassChanged(Object arg) {
        this.setChanged();
        this.notifyObservers(arg);
    }

}

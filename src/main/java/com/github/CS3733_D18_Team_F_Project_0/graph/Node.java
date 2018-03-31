package com.github.CS3733_D18_Team_F_Project_0.graph;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

public class Node {
    // the database ID of this node
    private final String nodeID;
    // the name of the floor where this node is located
    private final String floor;
    // actual 3d position of node
    private Point3D position;
    // position of the node on the wireframe map
    private Point2D wireframePosition;
    // extra weight to cause A* to more likely avoid this node
    private double additionalWeight = 0;
    // the name of the building this node is located in
    private String building;
    // the type of location this node is at
    private String nodeType;
    // an abbreviation of the name of this node
    private String shortName;

    Node(Point3D position, Point2D wireframePosition, double additionalWeight, String nodeID, String floor, String building
            , String nodeType, String shortName) {
        this.position = position;
        this.wireframePosition = wireframePosition;
        this.additionalWeight = additionalWeight;
        this.nodeID = nodeID;
        this.floor = floor;
        this.building = building;
        this.nodeType = nodeType;
        this.shortName = shortName;
    }

    /**
     * @return the id string of the node
     */
    public String getNodeID() {
        return nodeID;
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
    public void setNodeType(String nodeType) {
        if (!(nodeType.equals("HALL")
                || nodeType.equals("ELEV")
                || nodeType.equals("REST")
                || nodeType.equals("STAI")
                || nodeType.equals("DEPT")
                || nodeType.equals("LABS")
                || nodeType.equals("INFO")
                || nodeType.equals("CONF")
                || nodeType.equals("EXIT")
                || nodeType.equals("RETL")
                || nodeType.equals("SERV"))) {
            throw new AssertionError("The nodeType was invalid.");
        }
        this.nodeType = nodeType;
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
    }
}

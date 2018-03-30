package com.github.CS3733_D18_Team_F_Project_0.graph;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import org.junit.Assert;

public class NodeBuilder {
    // the database ID of this node
    private String nodeID = null;
    // the name of the floor where this node is located
    private String floor = null;
    // which team was assigned to create this node
    private String teamAssigned = "Team F";
    // actual 3d position of node
    private Point3D position = null;
    // position of the node on the wireframe map
    private Point2D wireframePosition = null;
    // extra weight to cause A* to more likely avoid this node
    private double additionalWeight = 0;
    // the name of the building this node is located in
    private String building = null;
    // the type of location this node is at
    private String nodeType = null;
    // the full name of the location of this node
    private String longName = null;
    // an abbreviation of the name of this node
    private String shortName = null;
    // elevator number for when nodetype is elevator
    private char elevatorChar = '\0';
    // for completely new nodes
    // keeps track of the number of nodes of this node type
    private int numNodeType = -1;

    public NodeBuilder setTeamAssigned(String teamAssigned) {
        this.teamAssigned = teamAssigned;
        return this;
    }

    public NodeBuilder setPosition(Point3D position) {
        this.position = position;
        return this;
    }

    public NodeBuilder setWireframePosition(Point2D wireframePosition) {
        this.wireframePosition = wireframePosition;
        return this;
    }

    public NodeBuilder setAdditionalWeight(double additionalWeight) {
        this.additionalWeight = additionalWeight;
        return this;
    }

    public NodeBuilder setBuilding(String building) {
        this.building = building;
        return this;
    }

    public NodeBuilder setNodeType(String nodeType) {
        if(nodeType.length() != 4){
            throw new AssertionError("A node type must be 4 characters long.");
        }
        this.nodeType = nodeType;
        return this;
    }

    public NodeBuilder setLongName(String longName) {
        this.longName = longName;
        return this;
    }

    public NodeBuilder setShortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public NodeBuilder setElevatorChar(char elevatorChar) {
        if(!(Character.isLetter(elevatorChar) | Character.isDigit(elevatorChar))){
            throw new AssertionError("You must assign a valid elevator character!");
        }

        this.elevatorChar = elevatorChar;
        return this;
    }

    public NodeBuilder setNumNodeType(int numNodeType) {
        this.numNodeType = numNodeType;
        return this;
    }

    public NodeBuilder setFloor(String floor) {
        if (floor.equals("0") || floor.equals("0G")) {
            this.floor = "0G";
        } else if (floor.equals("1") || floor.equals("01")) {
            this.floor = "01";
        } else if (floor.equals("2") || floor.equals("02")) {
            this.floor = "02";
        } else if (floor.equals("3") || floor.equals("03")) {
            this.floor = "03";
        } else {
            if (!(floor.equals("L1") || floor.equals("L2"))) {
                throw new AssertionError("Unknown Floor Number. Must be any of: 0G, 01, 02, 03, L1, L2");
            }
            this.floor = floor;
        }
        return this;
    }

    public Node build() {
        if (nodeType.length() != 4) {
            throw new AssertionError("Node type must be 4 characters long");
        }

        if (nodeID == null) {
            if (numNodeType == -1) {
                throw new AssertionError("When creating a new node, you must set the number of " +
                        "nodes of that type.");
            }
            String numNodeTypeStr;
            if (nodeType == "ELEV") {
                if (elevatorChar == '\0') {
                    throw new AssertionError("When creating an elevator assign the elevator name.");
                }
                numNodeTypeStr = "00" + elevatorChar;
            } else {
                numNodeTypeStr = String.format("%03d", numNodeType);
            }

            if (floor == null) {
                throw new AssertionError("You must assign a floor to a node.");
            }

            nodeID = "X" + nodeType + numNodeTypeStr + floor;
        }

        if(position == null){
            throw new AssertionError("You must set a position.");
        }
        if (wireframePosition == null) {
            wireframePosition = new Point2D(position.getX(), position.getY());
        }

        if(building == null){
            throw new AssertionError("You must set a building.");
        }

        if (shortName == null) {
            if(longName != null){
                shortName = longName;
            }
            else{
                shortName = nodeID + "[Unset shortName]";
            }
        }

        if (longName == null) {
            longName = nodeID + " [Unset longName]";
        }

        return new Node(position, wireframePosition, additionalWeight, nodeID, floor, building, nodeType, longName, shortName, teamAssigned);
    }
}

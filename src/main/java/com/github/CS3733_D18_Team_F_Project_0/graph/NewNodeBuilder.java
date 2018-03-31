package com.github.CS3733_D18_Team_F_Project_0.graph;

import javafx.geometry.Point2D;

public class NewNodeBuilder extends NodeBuilder<NewNodeBuilder> {
    // keeps track of the number of nodes of this node type
    private int numNodeType = -1;
    // elevator character
    private char elevatorChar;
    // the type of location this node is at
    private String nodeType = null;
    // the name of the floor where this node is located
    private String floor = null;

    public NewNodeBuilder() {
        super(NewNodeBuilder.class);
    }

    public Node build() {
        if (nodeType == null) {
            throw new AssertionError("A node type must be specified.");
        }
        if (nodeType.length() != 4) {
            throw new AssertionError("Node type must be 4 characters long.");
        }
        if (numNodeType == -1) {
            throw new AssertionError("When creating a new node, you must set the number of " +
                    "nodes of that type.");
        }
        String numNodeTypeStr;
        if (nodeType.equals("ELEV")) {
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

        nodeID = "F" + nodeType + numNodeTypeStr + floor;

        if (position == null) {
            throw new AssertionError("You must set a position.");
        }
        if (wireframePosition == null) {
            // TODO: apply matrix transformation
            wireframePosition = new Point2D(position.getX(), position.getY());
        }

        if (building == null) {
            throw new AssertionError("You must set a building.");
        }

        if (shortName == null) {
            shortName = nodeID + "[Unset shortName]";
        }

        return new Node(position, wireframePosition, 0, nodeID, floor, building, nodeType, shortName);
    }

    public NewNodeBuilder setNumNodeType(int numNodeType) {
        this.numNodeType = numNodeType;
        return this;
    }

    public NewNodeBuilder setElevatorChar(char elevatorChar) {
        if (!(Character.isLetter(elevatorChar))) {
            throw new AssertionError("You must assign a valid elevator character!");
        }

        this.elevatorChar = elevatorChar;
        return this;
    }

    public NewNodeBuilder setNodeType(String nodeType) {
        if (nodeType.length() != 4) {
            throw new AssertionError("A node type must be 4 characters long.");
        }
        this.nodeType = nodeType;
        return this;
    }

    public NewNodeBuilder setFloor(String floor) {
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
}

package com.github.CS3733_D18_Team_F_Project_0.graph;

import javafx.geometry.Point2D;

import static java.lang.Math.cos;
import static java.lang.StrictMath.sin;

public class NewNodeBuilder extends NodeBuilder<NewNodeBuilder> {
    // keeps track of the number of nodes of this node type
    private int numNodeType = -1;
    // elevator character
    private char elevatorChar;
    // the type of location this node is at
    private String nodeType = null;
    // the name of the floor where this node is located
    private String floor = null;

    /**
     * A builder class to make entirely new nodes
     */
    public NewNodeBuilder() {
        super(NewNodeBuilder.class);
    }

    /**
     * @return the node built from this builder class
     */
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

        String nodeID = "F" + nodeType + numNodeTypeStr + floor;

        if (position == null) {
            throw new AssertionError("You must set a position.");
        }
        if (wireframePosition == null) {

            //double transX = 960.0;
            //double transY = -120.0;
            //double scaleX = 0.75;
            //double scaleY = 0.60;
            //double rotateAngle = 0.4;

            double transX = 1350;
            double transY = -1050;
            double scaleX = 1;
            double scaleY = 1;
            double rotateAngle = .47;

            double a = scaleX * cos(rotateAngle);
            double b = -scaleY * sin(rotateAngle);
            double c = transX * scaleX * cos(rotateAngle) - transY * scaleY * sin(rotateAngle) + transX;
            double d = scaleX * sin(rotateAngle);
            double e = scaleY * cos(rotateAngle);
            double f = transX * scaleX * sin(rotateAngle) + transY * scaleY * cos(rotateAngle) + transY;

            /*double a = scaleX * cos(rotateAngle);
            double b = -scaleY * sin(rotateAngle);
            double c = transX;// * scaleX * cos(rotateAngle) - transY * scaleY * sin(rotateAngle);
            double d = scaleX * sin(rotateAngle);
            double e = scaleY * cos(rotateAngle);
            double f = transY;*/

            wireframePosition = new Point2D(a*position.getX() + b*position.getY() + c,
                    d * position.getX() + e * position.getY() + f);
        }

        if (building == null) {
            throw new AssertionError("You must set a building.");
        }

        if (shortName == null) {
            shortName = nodeID + "[Unset shortName]";
        }
        if (longName == null) {
            longName = shortName + "[Unset longName]";
        }

        return new Node(position, wireframePosition, 0, nodeID, floor, building, nodeType, shortName, longName);
    }

    /**
     * @param numNodeType the number of nodes of this node type
     * @return this to allow chained builder calls
     */
    public NewNodeBuilder setNumNodeType(int numNodeType) {
        this.numNodeType = numNodeType;
        return this;
    }

    /**
     * @param elevatorChar the elevator letter, only if this is an elevator
     * @return this to allow chained builder calls
     */
    public NewNodeBuilder setElevatorChar(char elevatorChar) {
        if (!(Character.isLetter(elevatorChar))) {
            throw new AssertionError("You must assign a valid elevator character!");
        }

        this.elevatorChar = elevatorChar;
        return this;
    }

    /**
     * @param nodeType the type of this node
     * @return this to allow chained builder calls
     */
    public NewNodeBuilder setNodeType(String nodeType) {
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
        return this;
    }

    /**
     * @param floor the floor of this node
     * @return this to allow chained builder calls
     */
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

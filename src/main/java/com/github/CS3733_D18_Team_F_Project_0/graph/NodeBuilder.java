package com.github.CS3733_D18_Team_F_Project_0.graph;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

public abstract class NodeBuilder<T> {
    protected final Class<T> subClass;

    // the database ID of this node
    protected String nodeID = null;
    // the name of the floor where this node is located
    protected String floor = null;
    // actual 3d position of node
    protected Point3D position = null;
    // position of the node on the wireframe map
    protected Point2D wireframePosition = null;
    // the name of the building this node is located in
    protected String building = null;
    // the type of location this node is at
    protected String nodeType = null;
    // an abbreviation of the name of this node
    protected String shortName = null;

    protected NodeBuilder(Class<T> subClass) {
        this.subClass = subClass;
    }

    public T setPosition(Point3D position) {
        this.position = position;
        return subClass.cast(this);
    }

    public T setWireframePosition(Point2D wireframePosition) {
        this.wireframePosition = wireframePosition;
        return subClass.cast(this);
    }

    public T setBuilding(String building) {
        this.building = building;
        return subClass.cast(this);
    }

    public T setNodeType(String nodeType) {
        if (nodeType.length() != 4) {
            throw new AssertionError("A node type must be 4 characters long.");
        }
        this.nodeType = nodeType;
        return subClass.cast(this);
    }

    public T setShortName(String shortName) {
        this.shortName = shortName;
        return subClass.cast(this);
    }


    public T setFloor(String floor) {
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
        return subClass.cast(this);
    }

}


package com.github.CS3733_D18_Team_F_Project_0.graph;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

public abstract class NodeBuilder<T> {
    protected final Class<T> subClass;

    // the database ID of this node
    protected String nodeID = null;
    // actual 3d position of node
    protected Point3D position = null;
    // position of the node on the wireframe map
    protected Point2D wireframePosition = null;
    // the name of the building this node is located in
    protected String building = null;
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

    public T setShortName(String shortName) {
        this.shortName = shortName;
        return subClass.cast(this);
    }

}


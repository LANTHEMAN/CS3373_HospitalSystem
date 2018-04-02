package com.github.CS3733_D18_Team_F_Project_0.graph;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

public abstract class NodeBuilder<T> {
    protected final Class<T> subClass;

    // actual 3d position of node
    protected Point3D position = null;
    // position of the node on the wireframe map
    protected Point2D wireframePosition = null;
    // the name of the building this node is located in
    protected String building = null;
    // an abbreviation of the name of this node
    protected String shortName = null;
    //The full name of this node location
    protected String longName = null;


    protected NodeBuilder(Class<T> subClass) {
        this.subClass = subClass;
    }

    /**n
     * @param position the position of this node
     * @return this to allow chained builder calls
     */
    public T setPosition(Point3D position) {
        this.position = position;
        return subClass.cast(this);
    }

    /**
     * @param wireframePosition the wireframe map position of this node
     * @return this to allow chained builder calls
     */
    public T setWireframePosition(Point2D wireframePosition) {
        this.wireframePosition = wireframePosition;
        return subClass.cast(this);
    }

    /**
     * @param building the building of this node
     * @return this to allow chained builder calls
     */
    public T setBuilding(String building) {
        this.building = building;
        return subClass.cast(this);
    }

    /**
     * @param shortName the short name of this node
     * @return this to allow chained builder calls
     */
    public T setShortName(String shortName) {
        this.shortName = shortName;
        return subClass.cast(this);
    }

    /**
     * @param longName the full name of this node
     * @return this to allow chained builder calls
     */
    public T setLongName(String longName) {
        this.longName = longName;
        return subClass.cast(this);
    }

}


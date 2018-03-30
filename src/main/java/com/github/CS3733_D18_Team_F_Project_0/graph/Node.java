package com.github.CS3733_D18_Team_F_Project_0.graph;

import javafx.geometry.Point3D;

public class Node {
    // actual position
    private Point3D position;
    // used to 'block' certain nodes
    private double additionalWeight = 0;

    /**
     * Create a new Node
     * @param position The 3D position of the node
     */
    Node(Point3D position) {
        this.position = position;
    }

    /**
     * Find the cartesian distance from this node to another node
     * @param node The node to find the distance to
     * @return The distance from this node to node
     */
    public double displacementTo(Node node) {
        return (this.position.distance(node.position));
    }

    /**
     * Set an additional weight for this node to make AStar avoid it
     * @param additionalWeight The additional weight to add
     */
    public void setAdditionalWeight(double additionalWeight) {
        this.additionalWeight = additionalWeight;
    }

    /**
     * Get the additional weight of this node
     * @return The additional weight
     */
    public double getAdditionalWeight() {
        return additionalWeight;
    }
}

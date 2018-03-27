package com.github.CS3733_D18_Team_F_Project_0;

import javafx.geometry.Point3D;

import java.util.*;

public class Node {
    // actual position
    private Point3D position;
    // used to 'block' certain nodes
    private double additionalWeight = 0;

    Node(Point3D position) {
        this.position = position;
    }

    public double displacementTo(Node node) {
        return (this.position.distance(node.position));
    }

    public void setAdditionalWeight(double additionalWeight) {
        this.additionalWeight = additionalWeight;
    }

    public double getAdditionalWeight() {
        return additionalWeight;
    }
}

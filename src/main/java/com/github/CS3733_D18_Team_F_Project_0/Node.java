package com.github.CS3733_D18_Team_F_Project_0;

import java.util.HashSet;
import javafx.geometry.Point3D;

public class Node {
    private Point3D position;
    private Hashset<Node> neighbors;

    Node(Point3D position, Hashset<Node> neighbors) {
        this.position = position;
        this.neighbors = neighbors;
    }
}

package com.github.CS3733_D18_Team_F_Project_0.graph;

import javafx.geometry.Point3D;
import org.junit.Test;

public class TestGraph {

    private static final Node node1 = new NewNodeBuilder()
            .setNodeType("HALL")
            .setNumNodeType(0)
            .setFloor("0G")
            .setBuilding("Home")
            .setPosition(new Point3D(0,0,0))
            .setShortName("place 1").build();

    private static final Node node2 = new NewNodeBuilder()
            .setNodeType("HALL")
            .setNumNodeType(1)
            .setFloor("0G")
            .setBuilding("Home")
            .setPosition(new Point3D(1,0,0))
            .setShortName("place 2").build();

    @Test(expected = AssertionError.class)
    public void testRemoveEdgeWithNonExistingNode() {
        Graph graph = new Graph();
        graph.addNode(node1);
        graph.removeEdge(node1, node2);
    }

}

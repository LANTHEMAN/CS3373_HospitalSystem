package com.github.CS3733_D18_Team_F_Project_0.graph;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class TestNode {

    private static final Node node1 = new NewNodeBuilder()
            .setNodeType("HALL")
            .forceNumNodeType(0)
            .setFloor("0G")
            .setBuilding("Home")
            .setPosition(new Point2D(0, 0))
            .setShortName("place 1").build();

    private static final Node node2 = new NewNodeBuilder()
            .setNodeType("HALL")
            .forceNumNodeType(1)
            .setFloor("0G")
            .setBuilding("Home")
            .setPosition(new Point2D(1, 0))
            .setShortName("place 2").build();

    @Test
    public void testDisplacementTo() {
        assertEquals(1.0, node1.displacementTo(node2), 0.001);
    }
}

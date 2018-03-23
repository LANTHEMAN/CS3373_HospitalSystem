package com.github.CS3733_D18_Team_F_Project_0;

import javafx.geometry.Point3D;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestNode {

    @Test
    public void testAStar() {
        Node nodeA = new Node(new Point3D(0,0,0));
        Node nodeB = new Node(new Point3D(1,1,1));
        Node nodeC = new Node(new Point3D(4,4,4));
        Node nodeD = new Node(new Point3D(2,2,2));

        nodeA.addNeighbor(nodeB).addNeighbor(nodeC);
        nodeB.addNeighbor(nodeC).addNeighbor(nodeD);
        nodeC.addNeighbor(nodeD);

        ArrayList<Node> path = new ArrayList<>();
        path.add(nodeA);
        path.add(nodeB);
        path.add(nodeD);

        assertEquals(path, nodeA.findPath(nodeD));
    }

}

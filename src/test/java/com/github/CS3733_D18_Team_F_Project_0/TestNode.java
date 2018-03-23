package com.github.CS3733_D18_Team_F_Project_0;

import javafx.geometry.Point3D;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestNode {

    @Test
    public void testAStar1() {
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

    @Test
    public void testAStar2() {
        Node nodeS = new Node(new Point3D(0,0,0));

        Node nodeA = new Node(new Point3D(0,-1,0));
        Node nodeB = new Node(new Point3D(1,-1,0));
        Node nodeC = new Node(new Point3D(1,0,0));
        Node nodeD = new Node(new Point3D(2,0,0));
        Node nodeE = new Node(new Point3D(2,-1,0));

        Node nodeQ = new Node(new Point3D(-0.5,0,0));
        Node nodeR = new Node(new Point3D(-1,0,0));

        Node nodeF = new Node(new Point3D(2,-2,0));

        nodeS.addNeighbor(nodeA).addNeighbor(nodeQ);

        nodeA.addNeighbor(nodeB);
        nodeB.addNeighbor(nodeC);
        nodeC.addNeighbor(nodeD);
        nodeD.addNeighbor(nodeE);
        nodeE.addNeighbor(nodeF);

        nodeQ.addNeighbor(nodeR);
        nodeR.addNeighbor(nodeF);

        ArrayList<Node> path = new ArrayList<>();
        path.add(nodeS);
        path.add(nodeQ);
        path.add(nodeR);
        path.add(nodeF);

        assertEquals(path, nodeS.findPath(nodeF));
    }

    @Test
    public void testAStar3() {
        Node nodeS = new Node(new Point3D(0,0,0));

        Node nodeA = new Node(new Point3D(0,-1,0));
        Node nodeB = new Node(new Point3D(1,-1,0));
        Node nodeC = new Node(new Point3D(1,0,0));
        Node nodeD = new Node(new Point3D(2,0,0));
        Node nodeE = new Node(new Point3D(2,-1,0));

        Node nodeQ = new Node(new Point3D(-1,0,0));
        Node nodeR = new Node(new Point3D(-1,0,1.3));

        Node nodeF = new Node(new Point3D(2,-2,0));

        nodeS.addNeighbor(nodeA).addNeighbor(nodeQ);

        nodeA.addNeighbor(nodeB);
        nodeB.addNeighbor(nodeC);
        nodeC.addNeighbor(nodeD);
        nodeD.addNeighbor(nodeE);
        nodeE.addNeighbor(nodeF);

        nodeQ.addNeighbor(nodeR);
        nodeR.addNeighbor(nodeF);

        ArrayList<Node> path = new ArrayList<>();
        path.add(nodeS);
        path.add(nodeA);
        path.add(nodeB);
        path.add(nodeC);
        path.add(nodeD);
        path.add(nodeE);
        path.add(nodeF);

        assertEquals(path, nodeS.findPath(nodeF));
    }

}

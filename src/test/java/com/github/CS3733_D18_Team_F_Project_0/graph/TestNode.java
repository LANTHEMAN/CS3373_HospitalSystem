package com.github.CS3733_D18_Team_F_Project_0.graph;

import javafx.geometry.Point3D;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestNode {

    @Test
    public void testAStar1() {
        Node nodeA = new Node(new Point3D(0, 0, 0));
        Node nodeB = new Node(new Point3D(1, 1, 1));
        Node nodeC = new Node(new Point3D(4, 4, 4));
        Node nodeD = new Node(new Point3D(2, 2, 2));

        Graph graph = new Graph();
        graph.addNode(nodeA).addNode(nodeB).addNode(nodeC).addNode(nodeD);

        graph.addEdge(nodeA, nodeB, "Test1").addEdge(nodeA, nodeC, "Test2");
        graph.addEdge(nodeB, nodeC, "Test3").addEdge(nodeB, nodeD, "Test4");
        graph.addEdge(nodeC, nodeD, "Test5");

        ArrayList<Node> path = new ArrayList<>();
        path.add(nodeA);
        path.add(nodeB);
        path.add(nodeD);

        assertEquals(path, AStar.getPath(graph, nodeA, nodeD));
    }


    @Test
    public void testAStar2() {
        Node nodeS = new Node(new Point3D(0, 0, 0));

        Node nodeA = new Node(new Point3D(0, -1, 0));
        Node nodeB = new Node(new Point3D(1, -1, 0));
        Node nodeC = new Node(new Point3D(1, 0, 0));
        Node nodeD = new Node(new Point3D(2, 0, 0));
        Node nodeE = new Node(new Point3D(2, -1, 0));

        Node nodeQ = new Node(new Point3D(-0.5, 0, 0));
        Node nodeR = new Node(new Point3D(-1, 0, 0));

        Node nodeF = new Node(new Point3D(2, -2, 0));

        Graph graph = new Graph();
        graph.addNode(nodeS).addNode(nodeA).addNode(nodeB).addNode(nodeC).addNode(nodeD).addNode(nodeE)
                .addNode(nodeQ).addNode(nodeR).addNode(nodeF);

        graph.addEdge(nodeS, nodeA, "Test1").addEdge(nodeS, nodeQ, "Test2")
                .addEdge(nodeA, nodeB, "Test3")
                .addEdge(nodeB, nodeC, "Test3")
                .addEdge(nodeC, nodeD, "Test3")
                .addEdge(nodeD, nodeE, "Test3")
                .addEdge(nodeE, nodeF, "Test3")
                .addEdge(nodeQ, nodeR, "Test3")
                .addEdge(nodeR, nodeF, "Test3");

        ArrayList<Node> path = new ArrayList<>();
        path.add(nodeS);
        path.add(nodeQ);
        path.add(nodeR);
        path.add(nodeF);

        assertEquals(path, AStar.getPath(graph, nodeS, nodeF));
    }

    @Test
    public void testAStar3WithBlock() {
        Node nodeS = new Node(new Point3D(0, 0, 0));

        Node nodeA = new Node(new Point3D(0, -1, 0));
        Node nodeB = new Node(new Point3D(1, -1, 0));
        Node nodeC = new Node(new Point3D(1, 0, 0));
        Node nodeD = new Node(new Point3D(2, 0, 0));
        Node nodeE = new Node(new Point3D(2, -1, 0));

        Node nodeQ = new Node(new Point3D(-1, 0, 0));
        Node nodeR = new Node(new Point3D(-1, 0, 1.3));

        Node nodeF = new Node(new Point3D(2, -2, 0));

        Graph graph = new Graph();
        graph.addNode(nodeS).addNode(nodeA).addNode(nodeB).addNode(nodeC).addNode(nodeD).addNode(nodeE)
                .addNode(nodeQ).addNode(nodeR).addNode(nodeF);

        graph.addEdge(nodeS, nodeA, "Test1").addEdge(nodeS, nodeQ, "Test2")
                .addEdge(nodeA, nodeB, "ExName")
                .addEdge(nodeB, nodeC, "ExName")
                .addEdge(nodeC, nodeD, "ExName")
                .addEdge(nodeD, nodeE, "ExName")
                .addEdge(nodeE, nodeF, "ExName")
                .addEdge(nodeQ, nodeR, "ExName")
                .addEdge(nodeR, nodeF, "ExName");

        ArrayList<Node> path = new ArrayList<>();
        path.add(nodeS);
        path.add(nodeA);
        path.add(nodeB);
        path.add(nodeC);
        path.add(nodeD);
        path.add(nodeE);
        path.add(nodeF);

        assertEquals(path, AStar.getPath(graph, nodeS, nodeF));

        // now 'block' a specific path by adding additional weight

        ArrayList<Node> path2 = new ArrayList<>();
        path2.add(nodeS);
        path2.add(nodeQ);
        path2.add(nodeR);
        path2.add(nodeF);

        nodeB.setAdditionalWeight(1);

        assertEquals(path2, AStar.getPath(graph, nodeS, nodeF));
    }

}

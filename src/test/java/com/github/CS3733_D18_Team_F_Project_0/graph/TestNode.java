package com.github.CS3733_D18_Team_F_Project_0.graph;

import javafx.geometry.Point3D;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class TestNode {

    @Test
    public void testAStar1() {
        Node nodeA = new NewNodeBuilder()
                .setPosition(new Point3D(0, 0, 0))
                .setNodeType("HALL")
                .setNumNodeType(1)
                .setBuilding("Place")
                .setFloor("0G")
                .build();
        Node nodeB = new NewNodeBuilder()
                .setPosition(new Point3D(1, 1, 1))
                .setNodeType("HALL")
                .setNumNodeType(2)
                .setBuilding("Place")
                .setFloor("0G")
                .build();
        Node nodeC = new NewNodeBuilder()
                .setPosition(new Point3D(4, 4, 4))
                .setNodeType("HALL")
                .setNumNodeType(3)
                .setBuilding("Place")
                .setFloor("0G")
                .build();
        Node nodeD = new NewNodeBuilder()
                .setPosition(new Point3D(2, 2, 2))
                .setNodeType("HALL")
                .setNumNodeType(4)
                .setBuilding("Place")
                .setFloor("0G")
                .build();

        Graph graph = new Graph();
        graph.addNode(nodeA).addNode(nodeB).addNode(nodeC).addNode(nodeD);

        graph.addEdge(nodeA, nodeB, "Test1").addEdge(nodeA, nodeC, "Test2");
        graph.addEdge(nodeB, nodeC, "Test3").addEdge(nodeB, nodeD, "Test4");
        graph.addEdge(nodeC, nodeD, "Test5");

        // create all paths for possible routes for testing
        ArrayList<Node> path1Arr = new ArrayList<>();
        path1Arr.add(nodeA);
        path1Arr.add(nodeB);
        path1Arr.add(nodeD);
        Path path1 = new Path(path1Arr, graph);
        ArrayList<Node> path2Arr = new ArrayList<>();
        path2Arr.add(nodeA);
        path2Arr.add(nodeC);
        path2Arr.add(nodeD);
        Path path2 = new Path(path2Arr, graph);

        assertEquals(path1, AStar.getPath(graph, nodeA, nodeD));

        graph.removeEdge(nodeA, nodeB);
        assertEquals(path2, AStar.getPath(graph, nodeA, nodeD));

        graph.addEdge(nodeA, nodeB);
        assertEquals(path1, AStar.getPath(graph, nodeA, nodeD));

        graph.removeNode(nodeB);
        assertEquals(path2, AStar.getPath(graph, nodeA, nodeD));

        // check that filter methods work for nodes
        graph.addNode(nodeB);
        HashSet<Node> filteredNodes = graph.getNodes(
                node -> node.getPosition().distance(0.d, 0.d, 0.d) < 2);
        HashSet<Node> closeToOrigin = new HashSet<>();
        closeToOrigin.add(nodeA);
        closeToOrigin.add(nodeB);
        assertEquals(closeToOrigin, filteredNodes);

        // check that filter methods work for edges
        HashSet<Edge> filteredEdges = graph.getEdges(edge -> edge.hasNode(nodeD));
        HashSet<Edge> connectedToNodeD = new HashSet<>();
        connectedToNodeD.add(graph.getEdge(nodeC, nodeD));
        assertEquals(connectedToNodeD, filteredEdges);
    }

    @Test(expected = AssertionError.class)
    public void testInvalidNode() {
        new NewNodeBuilder().build();
    }

    /*


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

    */

}

package com.github.CS3733_D18_Team_F_Project_0;

import com.github.CS3733_D18_Team_F_Project_0.graph.AStar;
import com.github.CS3733_D18_Team_F_Project_0.graph.Graph;
import com.github.CS3733_D18_Team_F_Project_0.graph.Node;
import javafx.geometry.Point3D;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestNode {

    /*
    public HashMap<String, Node> CSVParser() throws FileNotFoundException {
        Scanner nodeScanner = new Scanner(new File(getClass().getResource("MapFNodes.csv").getFile()));
        Scanner edgeScanner = new Scanner(new File(getClass().getResource("MapFEdges.csv").getFile()));

        // graph to hold all the nodes and their identifiers
        HashMap<String, Node> graph = new HashMap<>();

        // put all the nodes into the graph
        while (nodeScanner.hasNextLine()) {
            String line = nodeScanner.nextLine();
            String[] fields = line.split(",");

            if (fields.length == 9 && fields[8].equals("Team F")) {
                Node newNode = new Node(new Point3D(Integer.parseInt(fields[1]), Integer.parseInt(fields[2]),
                        Integer.parseInt(fields[3])));
                graph.put(fields[0], newNode);
            }
        }

        // create all the neighbors for the nodes in the graph
        while (edgeScanner.hasNextLine()) {
            String line = edgeScanner.nextLine();
            String[] fields = line.split(",");

            if (fields.length == 3 && graph.containsKey(fields[1]) && graph.containsKey(fields[2])) {
                Node firstNode = graph.get(fields[1]);
                Node secondNode = graph.get(fields[2]);

                firstNode.addNeighbor(secondNode);
            }
        }

        return graph;
    }

    @Test
    public void BrighamTestSmall() throws FileNotFoundException {
        HashMap<String, Node> graph = CSVParser();

        ArrayList<Node> path = graph.get("FHALL00701").findPath(graph.get("FHALL03201"));

        ArrayList<Node> actualPath = new ArrayList<>();
        actualPath.add(graph.get("FHALL00701"));
        actualPath.add(graph.get("FHALL01801"));
        actualPath.add(graph.get("FHALL03201"));

        assertEquals(path, actualPath);
    }

    */

    @Test
    public void testAStar1() {
        Node nodeA = new Node(new Point3D(0, 0, 0));
        Node nodeB = new Node(new Point3D(1, 1, 1));
        Node nodeC = new Node(new Point3D(4, 4, 4));
        Node nodeD = new Node(new Point3D(2, 2, 2));

        Graph graph = new Graph();
        graph.addNode(nodeA).addNode(nodeB).addNode(nodeC).addNode(nodeD);

        graph.addEdge(nodeA, nodeB).addEdge(nodeA, nodeC);
        graph.addEdge(nodeB, nodeC).addEdge(nodeB, nodeD);
        graph.addEdge(nodeC, nodeD);

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

        graph.addEdge(nodeS, nodeA).addEdge(nodeS, nodeQ)
                .addEdge(nodeA, nodeB)
                .addEdge(nodeB, nodeC)
                .addEdge(nodeC, nodeD)
                .addEdge(nodeD, nodeE)
                .addEdge(nodeE, nodeF)
                .addEdge(nodeQ, nodeR)
                .addEdge(nodeR, nodeF);

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

        graph.addEdge(nodeS, nodeA).addEdge(nodeS, nodeQ)
                .addEdge(nodeA, nodeB)
                .addEdge(nodeB, nodeC)
                .addEdge(nodeC, nodeD)
                .addEdge(nodeD, nodeE)
                .addEdge(nodeE, nodeF)
                .addEdge(nodeQ, nodeR)
                .addEdge(nodeR, nodeF);

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

package com.github.CS3733_D18_Team_F_Project_0.graph;

import java.util.ArrayList;

import static junit.framework.TestCase.assertTrue;

public class Path {
    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;

    public Path(ArrayList<Node> path, Graph graph) {
        this.nodes = path;
        this.edges = new ArrayList<>();

        // get edges
        Node prevNode = null;
        for (Node node : nodes) {
            if (prevNode != null) {
                edges.add(graph.getEdge(node, prevNode));
            }
            prevNode = node;
        }
        assertTrue(nodes.size() > 1 && nodes.size() != edges.size() - 1);
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Path)) {
            return false;
        }
        Path path = (Path) obj;
        return this.nodes.equals(path.nodes);
    }
}

package edu.wpi.cs3733d18.teamF.graph;

import java.util.ArrayList;

public class Path {
    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;

    /**
     * A collection of nodes and edges representing a path along a graph
     *
     * @param path  the list of nodes
     * @param graph the graph containing the nodes
     */
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

        //if (!(nodes.size() > 1 && nodes.size() != edges.size() - 1)) {
        //    throw new AssertionError("The nodes must be connected by edges!");
        //}
    }

    /**
     * @return the nodes of this path
     */
    public ArrayList<Node> getNodes() {
        return nodes;
    }

    /**
     * @return the edges in this path
     */
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

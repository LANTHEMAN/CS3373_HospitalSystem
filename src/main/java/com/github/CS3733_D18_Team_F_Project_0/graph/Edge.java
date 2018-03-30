package com.github.CS3733_D18_Team_F_Project_0.graph;

public class Edge {
    String edgeID;
    private Node node1;
    private Node node2;

    /**
     * Create a new edge
     *
     * @param node1  The first node that this edge is connected to
     * @param node2  The second node that this edge is connected to
     * @param edgeID The String ID of this node
     */
    public Edge(Node node1, Node node2, String edgeID) {
        this.edgeID = edgeID;
        this.node1 = node1;
        this.node2 = node2;
    }

    String getEdgeID() {
        return edgeID;
    }

    /**
     * Determine if this edge is the edge between two nodes
     *
     * @param node1 The first node
     * @param node2 The second node
     * @return True if this edge is between these two nodes, false otherwise
     */
    public Boolean edgeOfNodes(Node node1, Node node2) {
        if ((this.node1 == node1 && this.node2 == node2) ||
                this.node2 == node1 && this.node1 == node2) return true;
        else return false;
    }

    public Node getNode1() {
        return node1;
    }

    public Node getNode2() {
        return node2;
    }
}

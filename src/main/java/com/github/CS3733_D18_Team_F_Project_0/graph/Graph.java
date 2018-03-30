package com.github.CS3733_D18_Team_F_Project_0.graph;

import java.util.HashMap;
import java.util.HashSet;

// TODO this class needs exceptions for all guards
public class Graph {
    private HashMap<Node, HashSet<Node>> adjacencyList;
    private HashSet<Edge> edges;

    /**
     * Create a new graph of the hospital
     */
    public Graph() {
        this.adjacencyList = new HashMap<>();
        this.edges = new HashSet<>();
    }

    /**
     * Add a node to this graph
     *
     * @param node The node to add
     * @return The graph with the new node
     */
    public Graph addNode(Node node) {
        adjacencyList.put(node, new HashSet<>());
        return this;
    }

    /**
     * Remove a node from the graph it it exists
     *
     * @param node The node to remove
     * @return The graph if successful, null if not
     */
    public Graph removeNode(Node node) {
        if (!adjacencyList.containsKey(node)) {
            return null;
        }
        // remove all edges containing node
        HashSet<Node> adjacentNodes = adjacencyList.get(node);
        for (Node adjacentNode : adjacentNodes) {
            adjacencyList.get(adjacentNode).remove(node);
        }
        // remove the node
        adjacencyList.remove(node);

        return this;
    }


    public Graph addEdge(Node node1, Node node2) {
        // make sure both nodes exist
        if (!adjacencyList.containsKey(node1) || !adjacencyList.containsKey(node2)) {
            return this;
        }
        // if the node already exists
        if (edges.stream().anyMatch(edge -> (edge.getNode1() == node1 && edge.getNode2() == node2)
                || (edge.getNode1() == node2 && edge.getNode2() == node1))) {
            return this;
        }
        return addEdge(node1, node2, node1.getNodeID() + "_" + node2.getNodeID());
    }

    /**
     * Add an edge between two nodes
     *
     * @param node1  The first node
     * @param node2  The second node
     * @param edgeID The string ID of this new edge
     * @return The graph with the new edge
     */
    public Graph addEdge(Node node1, Node node2, String edgeID) {
        // make sure both nodes exist
        if (!adjacencyList.containsKey(node1) || !adjacencyList.containsKey(node2)) {
            return this;
        }

        // double ch if the edge exists in the adjacency list
        HashSet<Node> adjacentNodes = adjacencyList.get(node1);

        // check if the edge already exists
        if (edges.stream().anyMatch(edge -> edge.getEdgeID().equals(edgeID))) {
            if (edges.stream().anyMatch(edge -> (edge.getNode1() == node1 && edge.getNode2() == node2)
                    || (edge.getNode1() == node2 && edge.getNode2() == node1))) {
                if (!adjacentNodes.contains(node2)) {
                    throw new AssertionError("An Edge exists, but the Nodes do not actually connect! RIP");
                }
                return this;
            } else {
                throw new AssertionError("Edge ID already exists!");
            }
        }

        // add the edge if it does not exist
        adjacentNodes.add(node2);
        adjacencyList.get(node2).add(node1);

        edges.add(new Edge(node1, node2, edgeID));

        return this;
    }

    /**
     * Remove an edge from this graph
     *
     * @param node1 The first node that this edge is connected to
     * @param node2 The second node that this edge is connected to
     * @return The graph that has been modified, null if not removed
     */
    public Graph removeEdge(Node node1, Node node2) {
        // make sure both nodes exist
        if (!adjacencyList.containsKey(node1) || !adjacencyList.containsKey(node2)) {
            return null;
        }
        adjacencyList.get(node1).remove(node2);
        adjacencyList.get(node2).remove(node1);

        // update edge list
        for (Edge edge : edges) {
            if (edge.edgeOfNodes(node1, node2)) {
                edges.remove(edge);
                break;
            }
        }
        return this;
    }

    /**
     * Return the neighbors to a specific node in this graph
     *
     * @param node The node to find the neighbors of
     * @return A set of nodes
     */
    public HashSet<Node> getNeighbors(Node node) {
        if (!adjacencyList.containsKey(node)) {
            throw new AssertionError();
        }
        return adjacencyList.get(node);
    }

    /**
     * Get a set off all the nodes in the graph
     *
     * @return The set of all nodes
     */
    public HashSet<Node> getNodes() {
        HashSet<Node> nodes = new HashSet<>();
        adjacencyList.forEach((key, value) -> {
            nodes.add(key);
        });
        return nodes;
    }
}

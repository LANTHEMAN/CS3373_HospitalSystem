package com.github.CS3733_D18_Team_F_Project_0.Graph;

import java.util.HashMap;
import java.util.LinkedList;

// TODO this class needs exceptions for all guards
public class Graph {

    private HashMap<Node, LinkedList<Node>> adjacencyList;

    public Graph() {
        this.adjacencyList = new HashMap<Node, LinkedList<Node>>();
    }

    public Graph addNode(Node node) {
        adjacencyList.put(node, new LinkedList<>());
        return this;
    }

    public void removeNode(Node node) {
        if (!adjacencyList.containsKey(node)) {
            return;
        }
        // remove all edges containing node
        LinkedList<Node> adjacentNodes = adjacencyList.get(node);
        for (Node adjacentNode : adjacentNodes) {
            adjacencyList.get(adjacentNode).remove(node);
        }
        // remove the node
        adjacencyList.remove(node);
    }

    public Graph addEdge(Node node1, Node node2) {
        // make sure both nodes exist
        if (!adjacencyList.containsKey(node1) || !adjacencyList.containsKey(node2)) {
            return this;
        }
        // check if the edge already exists
        LinkedList<Node> adjacentNodes = adjacencyList.get(node1);
        if (adjacentNodes.contains(node2)) {
            return this;
        }
        // add the edge if it does not exist
        adjacentNodes.add(node2);
        adjacencyList.get(node2).add(node1);

        return this;
    }

    public void removeEdge(Node node1, Node node2) {
        // make sure both nodes exist
        if (!adjacencyList.containsKey(node1) || !adjacencyList.containsKey(node2)) {
            return;
        }
        adjacencyList.get(node1).remove(node2);
        adjacencyList.get(node2).remove(node2);
    }

    public LinkedList<Node> getNeighbors(Node node) {
        if (!adjacencyList.containsKey(node)) {
            throw new AssertionError();
        }
        return adjacencyList.get(node);
    }

    public LinkedList<Node> getNodes() {
        LinkedList<Node> nodes = new LinkedList<>();
        adjacencyList.forEach((key, value) -> {
            nodes.add(key);
        });
        return nodes;
    }
}

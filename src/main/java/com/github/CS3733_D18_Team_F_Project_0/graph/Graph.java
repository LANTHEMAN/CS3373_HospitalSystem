package com.github.CS3733_D18_Team_F_Project_0.graph;

import org.junit.Assert;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.HashSet;

// TODO this class needs exceptions for all guards
public class Graph {

    private HashMap<Node, HashSet<Node>> adjacencyList;
    private HashSet<Edge> edges = new HashSet<>();

    public Graph() {
        this.adjacencyList = new HashMap<>();
    }

    public Graph addNode(Node node) {
        adjacencyList.put(node, new HashSet<>());
        return this;
    }

    public void removeNode(Node node) {
        if (!adjacencyList.containsKey(node)) {
            return;
        }
        // remove all edges containing node
        HashSet<Node> adjacentNodes = adjacencyList.get(node);
        for (Node adjacentNode : adjacentNodes) {
            adjacencyList.get(adjacentNode).remove(node);
        }
        // remove the node
        adjacencyList.remove(node);
    }

    public Graph addEdge(Node node1, Node node2, String edgeID) {
        // make sure both nodes exist
        if (!adjacencyList.containsKey(node1) || !adjacencyList.containsKey(node2)) {
            return this;
        }
        // check if the edge already exists
        HashSet<Node> adjacentNodes = adjacencyList.get(node1);
        if (adjacentNodes.contains(node2)) {
            return this;
        }
        // add the edge if it does not exist
        adjacentNodes.add(node2);
        adjacencyList.get(node2).add(node1);

        edges.add(new Edge(node1, node2, edgeID));

        return this;
    }

    public void removeEdge(Node node1, Node node2) {
        // make sure both nodes exist
        if (!adjacencyList.containsKey(node1) || !adjacencyList.containsKey(node2)) {
            return;
        }
        adjacencyList.get(node1).remove(node2);
        adjacencyList.get(node2).remove(node1);

        // update edge list
        for(Edge edge : edges){
            if(edge.edgeOfNodes(node1, node2)){
                edges.remove(edge);
                break;
            }
        }
    }

    public HashSet<Node> getNeighbors(Node node) {
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

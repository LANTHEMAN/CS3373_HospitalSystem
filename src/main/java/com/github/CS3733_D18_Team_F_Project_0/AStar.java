package com.github.CS3733_D18_Team_F_Project_0;

import java.util.*;

public class AStar {

    // computes the shortest path between 2 nodes using A*
    static ArrayList<Node> getPath(Graph graph, Node source, Node destination) {
        // see if the destination exists or if src equals dst
        if (destination == null || source == null) {
            throw new AssertionError();
        } else if (source == destination) {
            ArrayList<Node> path = new ArrayList<>();
            path.add(source);
            return path;
        }

        // ensures that every Node only has 1 corresponding AStarNode
        HashMap<Node, AStarNode> knownNodes = new HashMap<>();
        knownNodes.put(source, new AStarNode(source, destination, 0, knownNodes));
        knownNodes.put(destination, new AStarNode(destination, destination, Double.MAX_VALUE, knownNodes));

        // nodes that have already been evaluated
        HashSet<AStarNode> closedSet = new HashSet<>();
        // nodes that are discovered but not evaluated
        PriorityQueue<AStarNode> openSet = new PriorityQueue<>();

        // for each node, it saves where it can most efficiently be reached from
        HashMap<AStarNode, AStarNode> cameFrom = new HashMap<>();

        // set up initial conditions with source node
        AStarNode sourceNode = knownNodes.get(source);
        AStarNode dstNode = knownNodes.get(destination);
        openSet.add(sourceNode);

        while (!openSet.isEmpty()) {
            AStarNode currentNode = openSet.poll();
            if (currentNode == dstNode) {
                ArrayList<Node> path = new ArrayList<>();
                path.add(currentNode.getNode());
                AStarNode itNode = currentNode;
                while (cameFrom.containsKey(itNode)) {
                    itNode = cameFrom.get(itNode);
                    path.add(itNode.getNode());
                }
                Collections.reverse(path);
                return path;
            }

            closedSet.add(currentNode);

            LinkedList<AStarNode> neighbors = currentNode.getNeighbors(graph);
            for (AStarNode neighbor : neighbors) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }
                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                }

                double costCurrentToNeighbor = currentNode.getGScore()
                        + currentNode.displacementTo(neighbor)
                        + neighbor.node.getAdditionalWeight();

                // this is a worse path to the same point
                if (costCurrentToNeighbor > neighbor.getGScore()) {
                    continue;
                }

                // this path is the best to this point
                cameFrom.put(neighbor, currentNode);

                // update nodes and priority queue
                neighbor.setDistanceTraveled(costCurrentToNeighbor);
                openSet.remove(neighbor);
                openSet.add(neighbor);
            }
        }

        // no path was found
        return new ArrayList<>();
    }

    // wrapper class for Node that allows priority queue comparisons
    static private class AStarNode implements Comparable<AStarNode> {
        private Node node;
        private Node destination;
        private double distanceTraveled;
        private HashMap<Node, AStarNode> knownNodes;

        // only call this with a node that exists in reachedNodes
        private AStarNode(Node node, Node destination, double distanceTraveled
                , HashMap<Node, AStarNode> knownNodes) {
            this.node = node;
            this.destination = destination;
            this.distanceTraveled = distanceTraveled;
            this.knownNodes = knownNodes;
        }

        // 3d displacement between 2 nodes
        private double displacementTo(AStarNode node) {
            return this.node.displacementTo(node.node);
        }

        // returns all neighboring nodes
        private LinkedList<AStarNode> getNeighbors(Graph graph) {
            LinkedList<AStarNode> neighbors = new LinkedList<>();
            for (Node neighbor : graph.getNeighbors(this.node)) {
                if (knownNodes.containsKey(neighbor)) {
                    neighbors.add(knownNodes.get(neighbor));
                } else {
                    AStarNode newNode = new AStarNode(neighbor, destination, Double.MAX_VALUE, knownNodes);
                    knownNodes.put(neighbor, newNode);
                    neighbors.add(newNode);
                }
            }
            return neighbors;
        }

        private Node getNode() {
            return node;
        }

        private void setDistanceTraveled(double distanceTraveled) {
            this.distanceTraveled = distanceTraveled;
        }

        // get the cost to this point from the start
        private double getGScore() {
            return distanceTraveled + node.getAdditionalWeight();
        }

        // cost to get to this node + heuristic
        private double getFScore() {
            return distanceTraveled + this.node.displacementTo(this.destination) + node.getAdditionalWeight();
        }

        // used for priority queue comparisons
        @Override
        public int compareTo(AStarNode other) {
            return getFScore() < other.getFScore() ? -1 : 1;
        }
    }

}

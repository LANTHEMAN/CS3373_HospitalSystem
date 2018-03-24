package com.github.CS3733_D18_Team_F_Project_0;

import javafx.geometry.Point3D;

import java.util.*;

public class Node {
    private Point3D position;
    private HashSet<Node> neighbors;

    Node(Point3D position) {
        this.position = position;
        this.neighbors = new HashSet<>();
    }

    public Double displacementTo(Node node) {
        return (this.position.distance(node.position));
    }

    public Node addNeighbor(Node node) {
        if (node == this) {
            throw new AssertionError();
        }
        neighbors.add(node);
        return this;
    }

    // A* implementation
    public ArrayList<Node> findPath(Node dst) {
        // see if the destination exists or if src equals dst
        if (dst == null) {
            throw new AssertionError();
        } else if (this == dst) {
            ArrayList<Node> path = new ArrayList<>();
            path.add(this);
            return path;
        }

        // wrapper class for Node that allows priority queue comparisons
        class AStarNode implements Comparable<AStarNode> {
            private Node node;
            private Node destination;
            private double distanceTraveled;
            private HashMap<Node, AStarNode> knownNodes;

            // only call this from a node that exists in reachedNodes
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
            private LinkedList<AStarNode> getNeighbors() {
                LinkedList<AStarNode> neighbors = new LinkedList<>();
                for (Node neighbor : node.neighbors) {
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

            private void setDistanceTraveled(double distanceTraveled) {
                this.distanceTraveled = distanceTraveled;
            }

            private Node getNode() {
                return node;
            }

            // used for priority queue comparisons
            @Override
            public int compareTo(AStarNode other) {
                double thisEstimate = (distanceTraveled + this.node.displacementTo(this.destination));
                double otherEstimate = (other.distanceTraveled + other.node.displacementTo(this.destination));
                return thisEstimate < otherEstimate ? -1 : 1;
            }
        }

        // ensures that every Node only has 1 corresponding AStarNode
        HashMap<Node, AStarNode> knownNodes = new HashMap<>();
        knownNodes.put(this, new AStarNode(this, dst, 0, knownNodes));
        knownNodes.put(dst, new AStarNode(dst, dst, Double.MAX_VALUE, knownNodes));

        // nodes that have already been evaluated
        HashSet<AStarNode> closedSet = new HashSet<>();
        // nodes that are discovered but not evaluated
        PriorityQueue<AStarNode> openSet = new PriorityQueue<>();

        // for each node, it saves where it can most efficiently be reached from
        HashMap<AStarNode, AStarNode> cameFrom = new HashMap<>();
        // for each node, it saves the cost of getting from the start node to that node
        // if a node does not exist, assume the value of infinity
        HashMap<AStarNode, Double> gScore = new HashMap<>();

        // set up initial conditions with source node
        AStarNode thisNode = knownNodes.get(this);
        AStarNode dstNode = knownNodes.get(dst);
        openSet.add(thisNode);
        gScore.put(thisNode, (double) 0);

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

            LinkedList<AStarNode> neighbors = currentNode.getNeighbors();
            for (AStarNode neighbor : neighbors) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }
                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                }

                double gScore_current = gScore.getOrDefault(currentNode, Double.MAX_VALUE);
                double gScore_neighbor = gScore.getOrDefault(neighbor, Double.MAX_VALUE);
                double tentative_gScore = gScore_current + currentNode.displacementTo(neighbor);

                // this is a worse path to the same point
                if (tentative_gScore > gScore_neighbor) {
                    continue;
                }

                // this path is the best to this point
                cameFrom.put(neighbor, currentNode);
                gScore.put(neighbor, tentative_gScore);

                // update nodes and priority queue
                neighbor.setDistanceTraveled(tentative_gScore);
                openSet.remove(neighbor);
                openSet.add(neighbor);
            }
        }

        // no path was found
        return new ArrayList<>();
    }


}

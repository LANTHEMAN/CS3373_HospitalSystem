package edu.wpi.cs3733d18.teamF.graph;

import java.util.*;

public class BestFirst extends SearchTemplate{

    /**
     * Static function to determine the best route from one node to another
     * given a graph
     *
     * @param graph       The graph to find path on
     * @param source      The source node in the graph
     * @param destination The destination node in the graph
     * @return An array of Nodes that represent a path through the graph
     */

    public  Path getPath(Graph graph, Node source, Node destination) {
        // see if the destination exists or if src equals dst
        if (destination == null || source == null) {
            throw new AssertionError();
        } else if (source == destination) {
            ArrayList<Node> path = new ArrayList<>();
            path.add(source);
            return new Path(path, graph);
        }

        // ensures that every Node only has 1 corresponding BestFirstNode
        HashMap<Node, BestFirst.BestFirstNode> knownNodes = new HashMap<>();
        knownNodes.put(source, new BestFirst.BestFirstNode(source, destination, 0, knownNodes));
        knownNodes.put(destination, new BestFirst.BestFirstNode(destination, destination, Double.MAX_VALUE, knownNodes));

        // nodes that have already been evaluated
        HashSet<BestFirst.BestFirstNode> closedSet = new HashSet<>();
        // nodes that are discovered but not evaluated
        PriorityQueue<BestFirst.BestFirstNode> openSet = new PriorityQueue<>();

        // for each node, it saves where it can most efficiently be reached from
        HashMap<BestFirst.BestFirstNode, BestFirst.BestFirstNode> cameFrom = new HashMap<>();

        // set up initial conditions with source node
        BestFirst.BestFirstNode sourceNode = knownNodes.get(source);
        BestFirst.BestFirstNode dstNode = knownNodes.get(destination);
        openSet.add(sourceNode);

        while (!openSet.isEmpty()) {
            BestFirst.BestFirstNode currentNode = openSet.poll();
            if (currentNode == dstNode) {
                ArrayList<Node> path = new ArrayList<>();
                path.add(currentNode.getNode());
                BestFirst.BestFirstNode itNode = currentNode;
                while (cameFrom.containsKey(itNode)) {
                    itNode = cameFrom.get(itNode);
                    path.add(itNode.getNode());
                }
                Collections.reverse(path);
                return new Path(path, graph);
            }

            closedSet.add(currentNode);

            LinkedList<BestFirst.BestFirstNode> neighbors = currentNode.getNeighbors(graph);
            for (BestFirst.BestFirstNode neighbor : neighbors) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }
                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                }


                // this path is the best to this point
                cameFrom.put(neighbor, currentNode);

                // update nodes and priority queue
                openSet.remove(neighbor);
                openSet.add(neighbor);
            }
        }

        // no path was found
        return new Path(new ArrayList<>(), graph);
    }

    // wrapper class for Node that allows priority queue comparisons
    private static class BestFirstNode implements Comparable<BestFirst.BestFirstNode> {
        private Node node;
        private Node destination;
        private double distanceTraveled;
        private HashMap<Node, BestFirst.BestFirstNode> knownNodes;

        /**
         * A specific version of a node that stores values specific to this path
         *
         * @param node             The current node to base the BestFirstNode off of
         * @param destination      The destination node in the graph
         * @param distanceTraveled The distance traveled up until this node
         * @param knownNodes       The known nodes along this path so far
         */
        private BestFirstNode(Node node, Node destination, double distanceTraveled
                , HashMap<Node, BestFirst.BestFirstNode> knownNodes) {
            this.node = node;
            this.destination = destination;
            this.distanceTraveled = distanceTraveled;
            this.knownNodes = knownNodes;
        }


        /**
         * Get all the neighbors of this node
         *
         * @param graph The graph that contains this node
         * @return A LinkedList of all the neighbor nodes
         */
        private LinkedList<BestFirst.BestFirstNode> getNeighbors(Graph graph) {
            LinkedList<BestFirst.BestFirstNode> neighbors = new LinkedList<>();
            for (Node neighbor : graph.getNeighbors(this.node)) {
                if (knownNodes.containsKey(neighbor)) {
                    neighbors.add(knownNodes.get(neighbor));
                } else {
                    BestFirst.BestFirstNode newNode = new BestFirst.BestFirstNode(neighbor, destination, Double.MAX_VALUE, knownNodes);
                    knownNodes.put(neighbor, newNode);
                    neighbors.add(newNode);
                }
            }
            return neighbors;
        }

        /**
         * Get this node
         *
         * @return This node
         */
        private Node getNode() {
            return node;
        }


        /**
         * Get the F score for BestFirst algorithm
         *
         * @return The F Score
         */
        private double getFScore() {
            return  this.node.displacementTo(this.destination) + node.getAdditionalWeight();
        }

        /**
         * Compare two nodes for priority queue purposes
         *
         * @param other The other node to compare
         * @return Negative if this node is "less", positive otherwise
         */
        @Override
        public int compareTo(BestFirst.BestFirstNode other) {
            return getFScore() < other.getFScore() ? -1 : 1;
        }
    }

}

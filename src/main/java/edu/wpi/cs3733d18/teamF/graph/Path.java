package edu.wpi.cs3733d18.teamF.graph;

import java.util.ArrayList;
import java.util.Collections;

public class Path {
    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;
    private Graph graph;

    /**
     * A collection of nodes and edges representing a path along a graph
     *
     * @param path  the list of nodes
     * @param graph the graph containing the nodes
     */
    public Path(ArrayList<Node> path, Graph graph) {
        this.nodes = path;
        this.edges = new ArrayList<>();
        this.graph = graph;

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

    public double getLength() {
        return edges.stream()
                .map(node -> node.getDistance() + node.getNode1().getAdditionalWeight() + node.getNode2().getAdditionalWeight())
                .mapToDouble(value -> value)
                .sum();
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

    public ArrayList<Path> separateIntoFloors() {
        if (nodes.size() == 0) {
            return new ArrayList<>();
        }
        if (edges.size() == 0) {
            return new ArrayList<>(Collections.singletonList(new Path(new ArrayList<>(nodes), graph)));
        }

        ArrayList<Path> paths = new ArrayList<>();
        ArrayList<Node> nodes = new ArrayList<>();

        for (Edge edge : edges) {
            Node node1 = edge.getNode1();
            Node node2 = edge.getNode2();

            nodes.add(node1);

            if (!node1.getFloor().equals(node2.getFloor())) {
                paths.add(new Path(nodes, graph));
                nodes.clear();
            }
            // if its the last edge
            if (edge == edges.get(edges.size() - 1)) {
                nodes.add(node2);
                paths.add(new Path(nodes, graph));
            }
        }
        System.out.println("paths = " + paths);
        return paths;
    }

    public ArrayList<String> makeTextDirections() {
        ArrayList<String> directions = new ArrayList<>();

        double dist = 0;

        if (nodes.size() == 0) {
            return directions;
        }

        directions.add("Begin at " + this.getNodes().get(0).getShortName());

        for (int nodeIndex = 1; nodeIndex < this.nodes.size() - 1; nodeIndex++) {

            Node previousNode = this.getNodes().get(nodeIndex - 1);
            Node currentNode = this.getNodes().get(nodeIndex);
            Node nextNode = this.getNodes().get(nodeIndex + 1);

            double angle = getAngle(previousNode, currentNode, nextNode);

            dist += previousNode.displacementTo(currentNode) / 7.f;

            if (currentNode.getNodeType().equals("ELEV") && nextNode.getNodeType().equals("ELEV")) {
                if (Node.floorToInt.get(currentNode.getFloor()) < Node.floorToInt.get(nextNode.getFloor())) {
                    directions.add("Take elevator up to floor: " + nextNode.getFloor());
                } else {
                    directions.add("Take elevator down to floor: " + nextNode.getFloor());
                }
                continue;
            } else if (currentNode.getNodeType().equals("STAI") && nextNode.getNodeType().equals("STAI")) {
                if (Node.floorToInt.get(currentNode.getFloor()) < Node.floorToInt.get(nextNode.getFloor())) {
                    directions.add("Take stairs up to floor: " + nextNode.getFloor());
                } else {
                    directions.add("Take stairs down to floor: " + nextNode.getFloor());
                }
                continue;
            }

            if (angle < -30) {
                directions.add(String.format("Walk straight for %.0f feet", dist));
                if (currentNode.getNodeType().equals("HALL"))
                    directions.add("Turn left");
                else
                    directions.add("Turn left at " + currentNode.getShortName());
                dist = 0;
            } else if (angle > 30) {
                directions.add(String.format("Walk straight for %.0f feet", dist));
                if (currentNode.getNodeType().equals("HALL"))
                    directions.add("Turn right");
                else
                    directions.add("Turn right at " + currentNode.getShortName());
                dist = 0;
            }
        }

        if (nodes.size() > 1) {
            directions.add(String.format("Walk straight for %.0f feet", nodes.get(nodes.size() - 2).displacementTo(nodes.get(nodes.size() - 1)) + dist));
            directions.add("Arrive at " + nodes.get(nodes.size() - 1).getShortName());
        }

        return directions;
    }

    public double getAngle(Node previousNode, Node currentNode, Node nextNode) {

        double v1X = currentNode.getPosition().getX() - previousNode.getPosition().getX();
        double v1Y = currentNode.getPosition().getY() - previousNode.getPosition().getY();

        double v2X = nextNode.getPosition().getX() - currentNode.getPosition().getX();
        double v2Y = nextNode.getPosition().getY() - currentNode.getPosition().getY();

        double dot = v1X * v2X + v1Y * v2Y;
        double det = v1X * v2Y - v1Y * v2X;

        return Math.toDegrees(Math.atan2(det, dot));
    }

    public double getUnweightedLength() {
        return edges.stream()
                .map(Edge::getDistance)
                .mapToDouble(value -> value)
                .sum();
    }
}

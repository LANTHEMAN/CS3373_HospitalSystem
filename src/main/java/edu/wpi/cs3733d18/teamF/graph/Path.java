package edu.wpi.cs3733d18.teamF.graph;

import javafx.geometry.Point2D;

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

    public ArrayList<String> makeTextDirections(){
        ArrayList<String> directions = new ArrayList<String>();

        for(int nodeIndex = 1; nodeIndex < this.nodes.size()-1;nodeIndex++){
            Node previousNode = this.getNodes().get(nodeIndex-1);
            Node currentNode = this.getNodes().get(nodeIndex);
            Node nextNode = this.getNodes().get(nodeIndex + 1);
            double angle = getAngle(null, currentNode,nextNode);
            if(angle < 90 && angle > 20){
                directions.add("Turn right at node " + nodeIndex);
            } else if (angle < 340 && angle > 270) {
                directions.add("Turn left at node " + nodeIndex);
            }
        }
        return directions;
    }

    public double getAngle(Node previousNode, Node currentNode, Node nextNode){
        //Case for the first node
        if(previousNode == null){
            Node n = new NewNodeBuilder().build();
            //Make a node with a position creating a right triangle, to find the angle between the points.
            n.setPosition(new Point2D(currentNode.getPosition().getX(),nextNode.getPosition().getY()));
            previousNode = n;
        }
        double previousToCurrent = previousNode.displacementTo(currentNode);
        double currentToNext = currentNode.displacementTo(nextNode);
        double previousToNext = previousNode.displacementTo(nextNode);
        //Law of cosines: csquared = asquared + bsquared - 2ab Cos(C) rearranged to solve for Cos(C)
        double cosine = (Math.pow(previousToNext,2) - Math.pow(previousToCurrent,2) - Math.pow(currentToNext,2)) / (-1 * (2 * previousToCurrent * currentToNext));

        return Math.toDegrees(Math.acos(cosine));




    }
}

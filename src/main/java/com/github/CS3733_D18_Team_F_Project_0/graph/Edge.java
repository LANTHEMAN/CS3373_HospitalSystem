package com.github.CS3733_D18_Team_F_Project_0.graph;

public class Edge {
    String edgeID;
    private Node node1;
    private Node node2;

    public Edge(Node node1, Node node2, String edgeID){
        this.edgeID = edgeID;
        this.node1 = node1;
        this.node2 = node2;
    }

    public Boolean edgeOfNodes(Node node1, Node node2){
        if((this.node1 == node1 && this.node2 == node2) ||
                this.node2 == node1 && this.node1 == node2) return true;
        else return false;
    }
}

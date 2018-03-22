package com.github.CS3733_D18_Team_F_Project_0;

import java.util.HashSet;
import javafx.geometry.Point3D;

public class Node {
    private Point3D position;
    private HashSet<Node> neighbors;

    // infrastructure of heuristic map for A*
    private Double fScore;
    private Double gScore;
    private Double hScore;

    private Double fScoreWeight = 1;

    Node(Point3D position) {
        this.position = position;
        this.neighbors = new HashSet<>();
    }

    public Double getDisplacement(Node node){
        return(this.position.distance(node.position));
    }

    public void setgScore(Double gScore){
        this.gScore = gScore;
    }

    public void sethScore(Double hScore){
        this.hScore = hScore;
    }

    public void calcfScore(){
        this.fScore = this.gScore + (fScoreWeight * this.hScore);
    }

    public Double getfScore(){
        return fScore;
    }

    public Double gethScore(){
        return hScore;
    }

    public Double getgScore(){
        return gScore;
    }
}

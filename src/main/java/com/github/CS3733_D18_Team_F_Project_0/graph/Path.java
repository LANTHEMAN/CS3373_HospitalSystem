package com.github.CS3733_D18_Team_F_Project_0.graph;

import java.util.ArrayList;

public class Path {
    ArrayList<Node> nodes;

    public Path(ArrayList<Node> path, Graph graph) {
        this.nodes = path;

    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Path)){
            return false;
        }
        Path path = (Path) obj;
        return this.nodes.equals(path.nodes);
    }
}

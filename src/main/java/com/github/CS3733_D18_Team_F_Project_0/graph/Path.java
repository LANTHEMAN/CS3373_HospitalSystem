package com.github.CS3733_D18_Team_F_Project_0.graph;

import java.util.ArrayList;

public class Path {
    ArrayList<Node> path;

    public Path(ArrayList<Node> path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Path)){
            return false;
        }
        Path path = (Path) obj;
        return this.path.equals(path.path);
    }
}

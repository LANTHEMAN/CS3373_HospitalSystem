package com.github.CS3733_D18_Team_F_Project_0.gfx;

import com.github.CS3733_D18_Team_F_Project_0.graph.Node;

public abstract class NodeDrawable implements Drawable{
    protected Node node;

    protected NodeDrawable(Node node) {
        this.node = node;
    }

    protected NodeDrawable() {

    }

    public void update(Node node){
        this.node = node;
    }
}

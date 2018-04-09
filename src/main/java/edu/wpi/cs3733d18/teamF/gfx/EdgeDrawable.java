package com.github.CS3733_D18_Team_F_Project_0.gfx;

import com.github.CS3733_D18_Team_F_Project_0.graph.Edge;

public abstract class EdgeDrawable implements Drawable{
    protected Edge edge = null;

    protected EdgeDrawable(Edge edge){
        this.edge = edge;
    }

    protected EdgeDrawable() {
    }

    public void update(Edge edge){
        this.edge = edge;
    }
}
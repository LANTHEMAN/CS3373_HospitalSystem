package com.github.CS3733_D18_Team_F_Project_0.gfx.impl;

import com.github.CS3733_D18_Team_F_Project_0.gfx.EdgeDrawable;
import com.github.CS3733_D18_Team_F_Project_0.gfx.PathDrawable;
import com.github.CS3733_D18_Team_F_Project_0.graph.Edge;
import javafx.scene.layout.Pane;

public class StalePathDrawer extends PathDrawable {

    private EdgeDrawable edgeDrawer;

    public StalePathDrawer(EdgeDrawable edgeDrawer) {
        this.edgeDrawer = edgeDrawer;
    }

    @Override
    public void draw(Pane pane) {
        for (Edge edge : path.getEdges()) {
            edgeDrawer.update(edge);
            edgeDrawer.draw(pane);
        }
    }
}

package com.github.CS3733_D18_Team_F_Project_0.gfx;

import com.github.CS3733_D18_Team_F_Project_0.graph.Path;

public abstract class PathDrawable implements Drawable {
    protected Path path;

    protected PathDrawable(Path path) {
        this.path = path;
    }

    protected PathDrawable() {
    }

    public void update(Path path) {
        this.path = path;
    }
}

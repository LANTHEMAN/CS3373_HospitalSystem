package com.github.CS3733_D18_Team_F_Project_0.gfx;

import com.github.CS3733_D18_Team_F_Project_0.Map;

public abstract class MapDrawable implements Drawable {
    protected Map map;

    protected MapDrawable(Map map) {
        this.map = map;
    }

    protected MapDrawable() {

    }

    public void update(Map map){
        this.map = map;
    }

}

package com.github.CS3733_D18_Team_F_Project_0.gfx.impl;

import com.github.CS3733_D18_Team_F_Project_0.Map;
import com.github.CS3733_D18_Team_F_Project_0.gfx.MapDrawable;
import com.github.CS3733_D18_Team_F_Project_0.gfx.PaneController;
import javafx.scene.layout.Pane;

import java.util.Observable;
import java.util.Observer;

public class PaneMapController extends PaneController implements Observer {

    private Map map;
    private MapDrawable mapDrawer;

    public PaneMapController(Pane root, Map map, MapDrawable mapDrawer) {
        super(root);
        this.map = map;
        this.mapDrawer = mapDrawer;
        map.addObserver(this);

        mapDrawer.update(map);

        addDrawable(mapDrawer);

        draw();
    }

    public void refresh() {
        clear();
        draw();
    }


    @Override
    public void update(Observable o, Object arg) {
        refresh();
    }
}

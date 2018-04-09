package com.github.CS3733_D18_Team_F_Project_0.gfx;

import com.github.CS3733_D18_Team_F_Project_0.Map;
import com.github.CS3733_D18_Team_F_Project_0.graph.Node;
import com.github.CS3733_D18_Team_F_Project_0.graph.Path;
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

    public void selectNode(Node node){
        mapDrawer.selectNode(node);
        refresh();
    }

    public void showPath(Path path){
        mapDrawer.showPath(path);
        refresh();
    }
    public void unshowPath(){
        mapDrawer.unshowPath();
        refresh();
    }

    public void showNodes() {
        mapDrawer.showNodes();
        refresh();
    }

    public void showEdges() {
        mapDrawer.showEdges();
        refresh();
    }

    public void unshowEdges() {
        mapDrawer.unshowEdges();
        refresh();
    }

    public void unshowNodes() {
        mapDrawer.unshowNodes();
        refresh();
    }

    @Override
    public void update(Observable o, Object arg) {
        refresh();
    }
}

package edu.wpi.cs3733d18.teamF.controller.page.element.mapView;

import edu.wpi.cs3733d18.teamF.Map;
import edu.wpi.cs3733d18.teamF.gfx.MapDrawable;
import edu.wpi.cs3733d18.teamF.gfx.PaneController;
import edu.wpi.cs3733d18.teamF.graph.Node;
import edu.wpi.cs3733d18.teamF.graph.Path;
import javafx.scene.layout.Pane;

import java.util.Observable;
import java.util.Observer;

public class PaneMapController extends PaneController implements Observer {

    private MapDrawable mapDrawer;
    public Path drawnPath = null;

    PaneMapController(Pane root, Map map, MapDrawable mapDrawer) {
        super(root);
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
        this.drawnPath = path;

        if(path.getNodes().size() > 0){
            addDefaultStartNode(path.getNodes().get(0));
        }

        refresh();
    }
    public void unshowPath(){
        mapDrawer.unshowPath();
        this.drawnPath = null;
        refresh();
    }

    public Path getDrawnPath() {
        return drawnPath;
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

    public void unselectNode() {
        mapDrawer.unselectNode();
        refresh();
    }

    void addDefaultStartNode(Node node){
        mapDrawer.addDefaultStartNode(node);
        refresh();
    }

    @Override
    public void update(Observable o, Object arg) {
        refresh();
    }
}

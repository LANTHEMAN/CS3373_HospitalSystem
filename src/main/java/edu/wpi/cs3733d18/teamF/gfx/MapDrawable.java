package edu.wpi.cs3733d18.teamF.gfx;

import edu.wpi.cs3733d18.teamF.graph.Map;
import edu.wpi.cs3733d18.teamF.graph.Node;
import edu.wpi.cs3733d18.teamF.graph.Path;

public abstract class MapDrawable implements Drawable {
    protected Map map;

    protected MapDrawable(Map map) {
        this.map = map;
    }

    protected MapDrawable() {
    }

    public void update(Map map) {
        this.map = map;
    }

    public abstract void selectNode(Node node);

    public abstract void unselectNode();

    public abstract void showPath(Path path);

    public abstract void redrawPath(Path path);

    public abstract void unshowPath();

    public abstract void showNodes();

    public abstract void unshowNodes();

    public abstract void showEdges();

    public abstract void unshowEdges();

    public abstract void addDefaultStartNode(Node node);

    public abstract void hoverNode(Node node);

    public abstract void unhoverNode();

    public abstract void update3DPathDisplay(boolean showAllFloors);
}

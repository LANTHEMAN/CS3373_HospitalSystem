package edu.wpi.cs3733d18.teamF.controller.page.element.mapView;

import edu.wpi.cs3733d18.teamF.graph.Node;
import edu.wpi.cs3733d18.teamF.graph.Path;
import javafx.geometry.Point2D;

public interface MapViewListener {
    void onNewPathSelected(Path path);
    void onNewDestinationNode(Node node);
    void onUpdateModifyNodePane(boolean isHidden, boolean is2D, Node modifiedNode);
    void onNewNodePopup(Point2D sceneLocation, Point2D nodeLocation);
    void onHideNewNodePopup();
    void onFloorRefresh();
}

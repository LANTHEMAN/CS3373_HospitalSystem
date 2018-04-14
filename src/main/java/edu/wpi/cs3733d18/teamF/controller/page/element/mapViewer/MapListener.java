package edu.wpi.cs3733d18.teamF.controller.page.element.mapViewer;

import edu.wpi.cs3733d18.teamF.graph.Node;

public interface MapListener {
    void onSrcNodeSelected(Node node);
    void onDstNodeSelected(Node node);
}

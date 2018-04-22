package edu.wpi.cs3733d18.teamF.graph.pathfinding;

import edu.wpi.cs3733d18.teamF.graph.Graph;
import edu.wpi.cs3733d18.teamF.graph.Node;
import edu.wpi.cs3733d18.teamF.graph.Path;

public abstract class SearchTemplate implements PathFindingAlgorithm {
    public abstract Path getPath(Graph graph, Node source, Node destination);

    public void Search(Graph graph, Node source, Node destination){
        getPath(graph, source, destination);
    }
}

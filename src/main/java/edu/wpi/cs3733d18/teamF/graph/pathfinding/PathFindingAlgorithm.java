package edu.wpi.cs3733d18.teamF.graph.pathfinding;

import edu.wpi.cs3733d18.teamF.graph.Graph;
import edu.wpi.cs3733d18.teamF.graph.Node;
import edu.wpi.cs3733d18.teamF.graph.Path;

public interface PathFindingAlgorithm {
      Path getPath(Graph graph, Node source, Node destination);
}

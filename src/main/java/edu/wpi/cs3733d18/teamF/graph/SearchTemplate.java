package edu.wpi.cs3733d18.teamF.graph;

public abstract class SearchTemplate {
    public abstract Path getPath(Graph graph, Node source, Node destination);

    public void Search(Graph graph, Node source, Node destination){
        getPath(graph, source, destination);
    }
}

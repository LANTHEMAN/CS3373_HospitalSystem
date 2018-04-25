package edu.wpi.cs3733d18.teamF.controller.page.element.mapView;

import edu.wpi.cs3733d18.teamF.graph.Path;
import javafx.geometry.Point2D;
import net.kurobako.gesturefx.GesturePane;

import java.awt.*;

public class MapState {

    private final Path path;
    private final double zoomAmount;
    private final Point2D target;
    private final String floor;

    public MapState(Path path, String floor, double zoomAmount, Point2D target) {
        this.path = path;
        this.floor = floor;
        this.zoomAmount = zoomAmount;
        this.target = target;

    }

    public Path getPath() {
        return path;
    }

    public String getFloor(){ return floor; }

    public double getZoomAmount() {
        return zoomAmount;
    }

    public Point2D getTarget() {
        return target;
    }
}

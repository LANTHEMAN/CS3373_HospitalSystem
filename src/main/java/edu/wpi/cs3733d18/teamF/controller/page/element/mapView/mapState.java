package edu.wpi.cs3733d18.teamF.controller.page.element.mapView;

import edu.wpi.cs3733d18.teamF.graph.Path;
import javafx.geometry.Point2D;
import net.kurobako.gesturefx.GesturePane;

import java.awt.*;

public class mapState {

    private final Path path;
    private final double zoomAmount;
    private final Point2D target;
    //private final GesturePane.Transformable transformable;

    public mapState(Path path, double zoomAmount, Point2D target){
        this.path = path;
        this.zoomAmount = zoomAmount;
        this.target = target;

    }

//    public mapState(Path path, GesturePane.Transformable transformable){
//        this.path = path;
//        this.transformable = transformable;
//    }

    public Path getPath() {
        return path;
    }

    public double getZoomAmount() {
        return zoomAmount;
    }

    public Point2D getTarget() {
        return target;
    }


//    public GesturePane.Transformable getTransformable() {
//        return transformable;
//    }
}

package edu.wpi.cs3733d18.teamF.controller.page.element.mapView;

import edu.wpi.cs3733d18.teamF.controller.page.HomeController;
import edu.wpi.cs3733d18.teamF.graph.MapSingleton;

public class MapMementoSingleton {
    private static MapMementoSingleton instance = new MapMementoSingleton();
    private MapViewElement source;
    private MapState initialState, lastState;

    public static MapMementoSingleton getInstance() {
        return instance;
    }

    private MapMementoSingleton(){

    }

    public void init(MapViewElement source){
        this.source = source;
        initialState = getState();
    }

    public void setSource(MapViewElement source){
        this.source = source;
    }

    public MapState getState(){
        return new MapState(source.getMapDrawController().getDrawnPath(), MapSingleton.getInstance().getMap().getFloor(), source.getGesturePane().getCurrentScale(), source.getGesturePane().targetPointAtViewportCentre());
    }

    public void saveState() {
        this.lastState = getState();
    }

    public void returnToState(MapState state) {
        if (state == null) return;

        if (state.getPath() != null) {
            source.getMapDrawController().showPath(state.getPath());
            source.getMapViewListener().onNewPathSelected(state.getPath());
            source.getMapViewListener().onPathsChanged(state.getPath().separateIntoFloors());

        }else{
            source.getMapDrawController().unshowPath();
            ((HomeController)source.getMapViewListener()).clearFloorTraversal();
            ((HomeController)source.getMapViewListener()).resetFloorButtonBorders();
        }

        ((HomeController) source.getMapViewListener()).changeFloor(state.getFloor());
        source.getGesturePane().zoomTo(state.getZoomAmount(), state.getTarget());
        source.getGesturePane().centreOn(state.getTarget());

    }

    public void returnToLastState() {
        returnToState(lastState);
    }

    public MapState getLastState() {
        return lastState;
    }

    public void reset(){
        returnToState(initialState);
        ((HomeController) source.getMapViewListener()).resetHomeController();
    }
}

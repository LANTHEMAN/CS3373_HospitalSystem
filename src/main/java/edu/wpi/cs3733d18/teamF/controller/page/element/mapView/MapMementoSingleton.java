package edu.wpi.cs3733d18.teamF.controller.page.element.mapView;

public class MapMementoSingleton {
    private static MapMementoSingleton instance = new MapMementoSingleton();
    private MapViewElement source;
    private MapState lastState;

    public static MapMementoSingleton getInstance() {
        return instance;
    }

    private MapMementoSingleton(){

    }

    public void setSource(MapViewElement source){
        this.source = source;
    }


    public void saveState() {
        this.lastState = new MapState(source.getMapDrawController().getDrawnPath(), source.getGesturePane().getCurrentScale(), source.getGesturePane().targetPointAtViewportCentre());
    }

    public void returnToState(MapState state) {
        if (state == null) return;
        if (state.getPath() != null) {
            source.getMapDrawController().showPath(state.getPath());
            source.getMapViewListener().onNewPathSelected(state.getPath());
        }
        source.getGesturePane().zoomTo(state.getZoomAmount(), state.getTarget());
        source.getGesturePane().centreOn(state.getTarget());
    }

    public void returnToLastState() {
        returnToState(lastState);
    }

    public MapState getLastState() {
        return lastState;
    }

}

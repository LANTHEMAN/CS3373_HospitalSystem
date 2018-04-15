package edu.wpi.cs3733d18.teamF.controller.page.element.mapView;

import edu.wpi.cs3733d18.teamF.ImageCacheSingleton;
import edu.wpi.cs3733d18.teamF.Map;
import edu.wpi.cs3733d18.teamF.controller.PaneSwitcher;
import edu.wpi.cs3733d18.teamF.controller.PermissionSingleton;
import edu.wpi.cs3733d18.teamF.controller.page.PageElement;
import edu.wpi.cs3733d18.teamF.gfx.impl.UglyMapDrawer;
import edu.wpi.cs3733d18.teamF.graph.Node;
import edu.wpi.cs3733d18.teamF.graph.Path;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.kurobako.gesturefx.GesturePane;

import java.util.Observable;
import java.util.Observer;


public class MapViewElement extends PageElement {
    // used to see if the floor has changed to update the map drawn
    MapListener mapListener;
    ViewMode viewMode = ViewMode.VIEW;
    //EditMode editMode = EditMode.PAN;
    EditMode editMode = EditMode.EDITNODE;
    Point2D mousePressedPosition = new Point2D(0, 0);
    @FXML
    private GesturePane gesturePane;
    @FXML
    private ImageView mapImage;
    @FXML
    private Pane mapContainer;
    @FXML
    private AnchorPane root;
    private Node selectedNodeStart = null;
    private Node selectedNodeEnd = null;
    private Node modifyNode = null;
    private boolean ctrlHeld = false;
    private boolean draggingNode;
    private Node heldNode;
    private PaneMapController mapDrawController;
    private String mapFloorDrawn;
    private boolean isMap2D;
    private Map map;
    private MapViewListener listener;

    public void initialize(MapViewListener listener, Map map, PaneSwitcher switcher, AnchorPane sourcePane) {
        // initialize fundamentals
        this.listener = listener;
        this.map = map;
        map.addObserver(mapListener = new MapListener());
        mapFloorDrawn = map.getFloor();
        isMap2D = map.is2D();
        initElement(sourcePane, root);

        // draw the nodes
        mapDrawController = new PaneMapController(mapContainer, map, new UglyMapDrawer());
        // set the correct floor
        refreshFloorDrawn();
        // set default zoom
        gesturePane.zoomTo(2, new Point2D(600, 600));

        // disable gesturePane when ctrl is held
        switcher.getScene().setOnKeyPressed(ke -> {
            if (ke.isControlDown()) {
                gesturePane.setGestureEnabled(false);
                ctrlHeld = true;
            }
        });
        switcher.getScene().setOnKeyReleased(ke -> {
            if (!ke.isControlDown()) {
                gesturePane.setGestureEnabled(true);
                if (ctrlHeld) {
                    selectedNodeEnd = null;
                }
                ctrlHeld = false;
            }
        });


        mapContainer.setOnMouseReleased(e -> {
            draggingNode = false;
            if (!PermissionSingleton.getInstance().isAdmin() || viewMode == ViewMode.VIEW) {
                return;
            }

            // don't select new node or path when panning
            if (mousePressedPosition.distance(new Point2D(e.getSceneX(), e.getSceneY())) > 25) {
                return;
            }

            // mouse position on map
            Point2D mapPos = getMapPos(e);
            boolean nodeIsSelected = isNodeSelected(mapPos);

            if (nodeIsSelected) {
                Node node = map.findNodeClosestTo(mapPos.getX(), mapPos.getY(), map.is2D(),
                        node1 -> node1.getFloor().equals(map.getFloor()));
                mapDrawController.selectNode(node);

                if (e.getButton() == MouseButton.PRIMARY && ctrlHeld) {
                    map.addEdge(node, selectedNodeEnd);
                }

                if (e.getButton() == MouseButton.SECONDARY) {
                    map.removeEdge(node, selectedNodeEnd);
                }
            }
        });

        mapContainer.setOnMousePressed(e -> {
            mousePressedPosition = new Point2D(e.getSceneX(), e.getSceneY());
        });

        mapContainer.setOnMouseClicked(e -> {
            // don't select new node or path when panning
            if (mousePressedPosition.distance(new Point2D(e.getSceneX(), e.getSceneY())) > 25) {
                return;
            }

            // mouse position on map
            Point2D mapPos = getMapPos(e);
            boolean nodeIsSelected = isNodeSelected(mapPos);

            // on single left click
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 1) {
                switch (viewMode) {
                    case EDIT: {
                        if(!nodeIsSelected){
                            return;
                        }
                        // get the selected node
                        Node node = map.findNodeClosestTo(mapPos.getX(), mapPos.getY(), map.is2D(), node1 -> node1.getFloor().equals(map.getFloor()));
                        if (viewMode == ViewMode.EDIT) {
                            mapDrawController.selectNode(node);
                        }
                        selectedNodeEnd = node;
                        modifyNode = node;

                        switch (editMode) {
                            case ADDNODE:
                            case REMNODE:
                            case ADDEDGE:
                            case REMEDGE:
                            case MOVENODE:
                            case PAN:
                                break;
                            case EDITNODE:
                                listener.onUpdateModifyNodePane(false, map.is2D(), modifyNode);
                                break;
                        }
                    }
                    break;
                    case VIEW: {
                        // select a new destination if a node is close enough to the mouse
                        Node dst = map.findNodeClosestTo(mapPos.getX(), mapPos.getY(), map.is2D(), node -> node.getFloor().equals(map.getFloor()));
                        if ((map.is2D() ? mapPos.distance(dst.getPosition()) : mapPos.distance(dst.getWireframePosition())) < 120 && selectedNodeStart != null) {
                            selectedNodeEnd = dst;
                            Path path = map.getPath(map.findNodeClosestTo(selectedNodeStart.getPosition().getX(), selectedNodeStart.getPosition().getY(), true), dst);
                            mapDrawController.showPath(path);
                            listener.onNewPathSelected(path);
                            listener.onNewDestinationNode(selectedNodeEnd);
                        } else {
                            return;
                        }
                    }
                    break;
                }
            }

            if (e.getButton() == MouseButton.SECONDARY && e.getClickCount() == 1) {
                if (nodeIsSelected && viewMode == ViewMode.VIEW) {
                    Node src = map.findNodeClosestTo(mapPos.getX(), mapPos.getY(), map.is2D(), node -> node.getFloor().equals(map.getFloor()));
                    if ((map.is2D() ? mapPos.distance(src.getPosition()) : mapPos.distance(src.getWireframePosition())) > 120) {
                        return;
                    }

                    selectedNodeStart = src;
                    Path path = map.getPath(selectedNodeStart, selectedNodeEnd);
                    if (path.getNodes().size() < 2) {
                        if (map.getNeighbors(selectedNodeStart).size() > 0) {
                            path = map.getPath(selectedNodeStart, map.getNeighbors(selectedNodeStart).iterator().next());
                        }
                    }
                    if (path.getNodes().size() >= 2) {
                        mapDrawController.showPath(path);
                        listener.onNewPathSelected(path);
                    }
                }
            }

            // remove a node
            if (e.getButton() == MouseButton.SECONDARY && e.getClickCount() == 2) {
                if (!map.is2D()) {
                    return;
                }

                if (nodeIsSelected && viewMode == ViewMode.EDIT) {
                    mapDrawController.unshowPath();
                    map.removeNode(map.findNodeClosestTo(mapPos.getX(), mapPos.getY(), map.is2D(), node -> node.getFloor().equals(map.getFloor())));
                    selectedNodeEnd = null;
                }
            }
            // create a new node
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                if (!map.is2D() || !PermissionSingleton.getInstance().isAdmin() || viewMode == ViewMode.VIEW) {
                    return;
                }

                double map_x = 5000;
                double map_y = 3400;
                double map3D_y = 2772;
                if (!map.is2D()) {
                    map_y = map3D_y;
                }
                listener.onNewNodePopup(new Point2D(e.getSceneX(), e.getSceneY())
                        , new Point2D(e.getX() * map_x / mapContainer.getMaxWidth(), (e.getY() * map_y / mapContainer.getMaxHeight())));
                selectedNodeEnd = null;
            }
        });

        mapContainer.setOnDragDetected(e -> {
            double map_x = 5000;
            double map_y = 3400;
            double map3D_y = 2772;
            if (!map.is2D()) {
                map_y = map3D_y;
            }

            Point2D mapPos = new Point2D(e.getX() * map_x / mapContainer.getMaxWidth()
                    , e.getY() * map_y / mapContainer.getMaxHeight());


            if (PermissionSingleton.getInstance().isAdmin()) {
                Node node = map.findNodeClosestTo(mapPos.getX(), mapPos.getY(), map.is2D());
                double deltaX = mapPos.getX() - node.getPosition().getX();
                double deltaY = mapPos.getY() - node.getPosition().getY();
                draggingNode = false;
                if (Math.sqrt((deltaX * deltaX) + (deltaY * deltaY)) < 20) {
                    draggingNode = true;
                    heldNode = node;
                }
            }
        });

        mapContainer.setOnMouseDragged(e -> {
            double map_x = 5000;
            double map_y = 3400;
            double map3D_y = 2772;
            if (!map.is2D()) {
                map_y = map3D_y;
            }

            Point2D mapPos = new Point2D(e.getX() * map_x / mapContainer.getMaxWidth()
                    , e.getY() * map_y / mapContainer.getMaxHeight());

            if (PermissionSingleton.getInstance().isAdmin() && viewMode == ViewMode.EDIT) {
                if (draggingNode) {
                    heldNode.setPosition(mapPos);
                }
            }
        });
    }

    public Path changePathDestination(Node destinationNode) {
        Path path = map.getPath(selectedNodeStart, destinationNode);
        mapDrawController.showPath(path);
        return path;
    }

    public PaneMapController getMapDrawController() {
        return mapDrawController;
    }

    public Node getSelectedNodeStart() {
        return selectedNodeStart;
    }

    public MapViewElement setSelectedNodeStart(Node selectedNodeStart) {
        this.selectedNodeStart = selectedNodeStart;
        return this;
    }

    public Path swapSrcAndDst() {
        Node tempNode = selectedNodeStart;
        selectedNodeStart = selectedNodeEnd;
        selectedNodeEnd = tempNode;
        return changePath(selectedNodeStart, selectedNodeEnd);
    }

    public Node getModifyNode() {
        return modifyNode;
    }

    public Node getSelectedNodeEnd() {

        return selectedNodeEnd;
    }

    public MapViewElement setSelectedNodeEnd(Node selectedNodeEnd) {
        this.selectedNodeEnd = selectedNodeEnd;
        return this;
    }

    public void toggleEditorMode() {
        if (viewMode == ViewMode.VIEW) {
            setViewMode(ViewMode.EDIT);
        } else {
            setViewMode(ViewMode.VIEW);
        }
    }

    public void setViewMode(ViewMode viewMode) {
        this.viewMode = viewMode;
        if (viewMode == ViewMode.EDIT) {
            mapDrawController.showNodes();
            mapDrawController.showEdges();
            mapDrawController.unshowPath();
            mapDrawController.unselectNode();
        } else {
            mapDrawController.unshowNodes();
            mapDrawController.unshowEdges();
            mapDrawController.unshowPath();
            mapDrawController.unselectNode();
            // hide the modify node pane
            listener.onUpdateModifyNodePane(true, false, null);
        }
    }


    public Path changePath(Node src, Node dst) {
        selectedNodeStart = src;
        selectedNodeEnd = dst;

        Path newPath = map.getPath(src, dst);
        mapDrawController.showPath(newPath);
        return newPath;
    }

    private void refreshFloorDrawn() {
        mapFloorDrawn = map.getFloor();
        isMap2D = map.is2D();
        int index;
        if (map.getFloor().equals("L2")) {
            index = 0;
        } else if (map.getFloor().equals("L1")) {
            index = 1;
        } else if (map.getFloor().equals("0G")) {
            index = 2;
        } else if (map.getFloor().equals("01")) {
            index = 3;
        } else if (map.getFloor().equals("02")) {
            index = 4;
        } else {
            index = 5;
        }
        if (map.is2D()) {
            mapImage.setImage(ImageCacheSingleton.maps2D[index]);
        } else {
            mapImage.setImage(ImageCacheSingleton.maps3D[index]);
        }
    }

    public void setEditMode(EditMode editMode) {
        this.editMode = editMode;

        // hide the modify node pane
        listener.onUpdateModifyNodePane(true, false, null);

        switch (editMode) {
            case ADDNODE:
                break;
            case REMNODE:
                break;
            case ADDEDGE:
                break;
            case REMEDGE:
                break;
            case MOVENODE:
                break;
            case EDITNODE:
                listener.onUpdateModifyNodePane(false, map.is2D(), modifyNode);
                break;
            case PAN:
                break;
        }

    }

    private Point2D getMapPos(MouseEvent e) {
        double map_x = 5000;
        double map_y = 3400;
        double map3D_y = 2772;
        if (!map.is2D()) {
            map_y = map3D_y;
        }
        return new Point2D(e.getX() * map_x / mapContainer.getMaxWidth()
                , e.getY() * map_y / mapContainer.getMaxHeight());
    }

    private boolean isNodeSelected(Point2D mapPos) {
        if (map.is2D()) {
            return map.getNodes(node -> new Point2D(node.getPosition().getX()
                    , node.getPosition().getY()).distance(mapPos) < 8 && node.getFloor().equals(map.getFloor())).size() > 0;
        } else {
            return map.getNodes(node -> new Point2D(node.getWireframePosition().getX()
                    , node.getWireframePosition().getY()).distance(mapPos) < 8 && node.getFloor().equals(map.getFloor())).size() > 0;
        }
    }

    public enum ViewMode {
        EDIT, VIEW
    }

    public enum EditMode {
        ADDNODE, REMNODE, ADDEDGE, REMEDGE, MOVENODE, EDITNODE, PAN
    }

    /**
     * Just used to see if the map floor image has to be changed
     */
    private class MapListener implements Observer {
        @Override
        public void update(Observable o, Object arg) {
            if (!mapFloorDrawn.equals(map.getFloor()) || isMap2D != map.is2D()) {
                refreshFloorDrawn();
            }
        }
    }
}

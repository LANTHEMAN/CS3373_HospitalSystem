package edu.wpi.cs3733d18.teamF.controller.page.element.mapView;

import edu.wpi.cs3733d18.teamF.controller.PaneSwitcher;
import edu.wpi.cs3733d18.teamF.controller.PermissionSingleton;
import edu.wpi.cs3733d18.teamF.controller.page.PageElement;
import edu.wpi.cs3733d18.teamF.gfx.ImageCacheSingleton;
import edu.wpi.cs3733d18.teamF.gfx.impl.map.UglyMapDrawer;
import edu.wpi.cs3733d18.teamF.graph.Map;
import edu.wpi.cs3733d18.teamF.graph.Node;
import edu.wpi.cs3733d18.teamF.graph.Path;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;

import static junit.framework.TestCase.assertEquals;

public class MapViewElement extends PageElement {
    // TODO change this to default starting location
    String startNodeID = "FRETL00101";
    EditMode editMode = EditMode.MOVENODE;
    boolean showAllFloors = false;
    // used to see if the floor has changed to update the map drawn
    private MapListener mapListener;
    private ViewMode viewMode = ViewMode.VIEW;
    private Point2D mousePressedPosition = new Point2D(0, 0);
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
    private Node selectEdgeNode = null;
    private boolean ctrlHeld = false;
    private boolean draggingNode;
    private boolean nodeSelectedOnMousePressed = false;
    private Node heldNode;
    private PaneMapController mapDrawController;
    private String mapFloorDrawn;
    private boolean isMap2D;
    private Map map;
    private MapViewListener listener;
    private ArrayList<Path> floorPath = new ArrayList<>();

    public void updateHomeLocation() {
        if (modifyNode == null) {
            return;
        }
        startNodeID = modifyNode.getNodeID();
    }

    public void resetStartLocation() {
        if (map.getNodes(node -> node.getNodeID().equals(startNodeID)).size() > 0) {
            setSelectedNodeStart(map.getNodes(node -> node.getNodeID().equals(startNodeID)).iterator().next());
            setSelectedNodeEnd(map.getNodes(node -> node.getNodeID().equals(startNodeID)).iterator().next());
            map.setFloor(selectedNodeStart.getFloor());
        } else {
            setSelectedNodeStart(map.findNodeClosestTo(1950, 840, true, node -> node.getFloor().equals("01")));
            setSelectedNodeEnd(map.findNodeClosestTo(1950, 840, true, node -> node.getFloor().equals("01")));
        }
    }

    private void onChangePath(boolean changedStartNode) {
        zoomToPath(changedStartNode ? 0 : -1);
    }

    public void zoomToPath(int pathIndex) {
        if (mapDrawController.getDrawnPath() == null) {
            return;
        }
        floorPath = mapDrawController.getDrawnPath().separateIntoFloors();

        if (pathIndex == -1) {
            pathIndex = floorPath.size() - 1;
        }

        
        if (floorPath.size() > 0 && floorPath.size() > pathIndex) {
            zoomToPath(floorPath.get(pathIndex));
        }

        listener.onPathsChanged(floorPath);
    }

    public void toggleShowAllFloors() {
        update3DPathDisplay(!getShowAllFloors());
    }

    private void zoomToPath(Path path) {
        Rectangle rect = getMapDrawController().getPathBoundingBox(path, map.is2D());

        Point2D midPoint = new Point2D(rect.x + (rect.getWidth() / 2.f), rect.y + (rect.getHeight() / 2.f));

        double scaleFactor = Math.floor((844.0 * 578.0) / (rect.getWidth() * rect.getHeight()));
        if (scaleFactor > 10) scaleFactor = 10;
        scaleFactor -= 3;

        if (rect.getHeight() > 0 && rect.getWidth() > 0) {
            getGesturePane().animate(new Duration(200)).zoomTo(scaleFactor, midPoint);
            Timeline oneHitWonder = new Timeline(new KeyFrame(Duration.millis(200),
                    event -> getGesturePane().animate(Duration.millis(500)).centreOn(midPoint)));
            oneHitWonder.setCycleCount(1);
            oneHitWonder.play();
        }
    }

    public boolean getShowAllFloors() {
        return showAllFloors;
    }

    public void update3DPathDisplay(boolean showAllFloors) {
        mapDrawController.update3DPathDisplay(showAllFloors);
    }

    public void initialize(MapViewListener listener, Map map, PaneSwitcher switcher, AnchorPane sourcePane) {
        // initialize fundamentals
        this.listener = listener;
        this.map = map;
        map.addObserver(mapListener = new MapListener());
        mapFloorDrawn = map.getFloor();
        isMap2D = map.is2D();
        initElement(sourcePane, root);
        sourcePane.autosize();

        // draw the nodes
        mapDrawController = new PaneMapController(mapContainer, map, new UglyMapDrawer());
        // set default start location
        resetStartLocation();
        // set the correct floor
        refreshFloorDrawn();
        // set default zoom
        gesturePane.zoomTo(2, new Point2D(600, 600));
        // disable gesturePane when ctrl is held
        switcher.getScene().setOnKeyPressed(ke -> {
            if (ke.isControlDown()) {
                if (mapDrawController.getDrawnPath() != null) {
                    mapDrawController.getDrawnPath().separateIntoFloors();
                }
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

        mapDrawController.update3DPathDisplay(true);

        mapContainer.setOnMouseMoved(e -> {
            Point2D mapPos = getMapPos(e);
            boolean nodeIsSelected = isNodeSelected(mapPos);
            if (nodeIsSelected) {
                Node node = map.findNodeClosestTo(mapPos.getX(), mapPos.getY(), map.is2D(),
                        node1 -> node1.getFloor().equals(map.getFloor()));
                if (!(mapDrawController.getDrawnPath() != null
                        && mapDrawController.getDrawnPath().getNodes().contains(node)
                        && (node.getNodeType().equals("ELEV") || node.getNodeType().equals("STAI"))
                        && map.getNeighbors(node)
                        .stream()
                        .filter(node1 -> mapDrawController.getDrawnPath().getNodes().contains(node1))
                        .filter(node1 -> !node1.getFloor().equals(node.getFloor()))
                        .toArray().length > 0)) {
                    return;
                }
                if (!mapDrawController.isHoveringNode(node)) {
                    mapDrawController.hoverNode(node);
                }
            } else {
                if (!mapDrawController.isHoveringNode(null)) {
                    mapDrawController.unhoverNode();
                }
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
                    if (!map.getNeighbors(node).contains(selectEdgeNode)) {
                        map.addEdge(node, selectedNodeEnd);
                    }
                }

                if (e.getButton() == MouseButton.SECONDARY) {
                    if (map.getNeighbors(node).contains(selectEdgeNode)) {
                        map.removeEdge(node, selectedNodeEnd);
                    }
                }
            }
        });

        mapContainer.setOnMousePressed(e -> {
                    mousePressedPosition = new Point2D(e.getSceneX(), e.getSceneY());
                    Point2D mapPos = getMapPos(e);
                    nodeSelectedOnMousePressed = isNodeSelected(mapPos);

                    if (nodeSelectedOnMousePressed && editMode == EditMode.MOVENODE) {
                        gesturePane.setGestureEnabled(false);
                    } else {
                        gesturePane.setGestureEnabled(true);
                    }
                }
        );

        mapContainer.setOnMouseClicked(e -> {
            // don't select new node or path when panning
            if (mousePressedPosition.distance(new Point2D(e.getSceneX(), e.getSceneY())) > 25) {
                listener.onHideNewNodePopup();
                return;
            }

            // mouse position on map
            Point2D mapPos = getMapPos(e);
            boolean nodeIsSelected = isNodeSelected(mapPos);

            // on single left click
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 1) {
                switch (viewMode) {
                    case EDIT: {
                        if (nodeIsSelected) {
                            // get the selected node
                            Node node = map.findNodeClosestTo(mapPos.getX(), mapPos.getY(), map.is2D(), node1 -> node1.getFloor().equals(map.getFloor()));
                            if (viewMode == ViewMode.EDIT) {
                                mapDrawController.selectNode(node);
                            }
                            selectedNodeEnd = node;
                            modifyNode = node;

                            switch (editMode) {
                                case REMNODE:
                                    mapDrawController.unshowPath();
                                    map.removeNode(map.findNodeClosestTo(mapPos.getX(), mapPos.getY(), map.is2D(), n -> n.getFloor().equals(map.getFloor())));
                                    selectedNodeEnd = null;
                                    break;
                                case ADDEDGE:
                                case REMEDGE: {
                                    if (selectEdgeNode == null) {
                                        selectEdgeNode = node;
                                    } else if (selectEdgeNode == node) {
                                        selectEdgeNode = null;
                                        mapDrawController.unselectNode();
                                    } else {
                                        if (editMode == EditMode.ADDEDGE) {
                                            map.addEdge(selectEdgeNode, node);
                                        } else {
                                            map.removeEdge(selectEdgeNode, node);
                                        }
                                        selectEdgeNode = null;
                                        mapDrawController.unselectNode();
                                    }
                                }
                                break;
                                case MOVENODE:
                                    break;
                                case PAN:
                                    break;
                                case EDITNODE:
                                    listener.onUpdateModifyNodePane(false, map.is2D(), modifyNode);
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            if (editMode == EditMode.ADDNODE) {
                                if (!map.is2D()) {
                                    return;
                                }
                                double map_x = 5000;
                                double map_y = 3400;
                                listener.onNewNodePopup(new Point2D(e.getSceneX(), e.getSceneY())
                                        , new Point2D(e.getX() * map_x / mapContainer.getMaxWidth(), (e.getY() * map_y / mapContainer.getMaxHeight())));
                                selectedNodeEnd = null;
                            }
                        }

                    }
                    break;
                    case VIEW: {
                        // select a new destination if a node is close enough to the mouse
                        Node dst = map.findNodeClosestTo(mapPos.getX(), mapPos.getY(), map.is2D(), node -> node.getFloor().equals(map.getFloor()));
                        if ((map.is2D() ? mapPos.distance(dst.getPosition()) : mapPos.distance(dst.getWireframePosition())) < 120 && selectedNodeStart != null) {
                            if (mapDrawController.getDrawnPath() != null
                                    && (dst.getNodeType().equals("ELEV") || dst.getNodeType().equals("STAI"))
                                    && mapDrawController.getDrawnPath().getNodes().contains(dst)) {
                                // change floors
                                HashSet<Node> neighborNodes = map.getNeighbors(dst);
                                for (Node node : neighborNodes) {
                                    if (!node.getFloor().equals(dst.getFloor()) && mapDrawController.getDrawnPath().getNodes().contains(node)) {
                                        map.setFloor(node.getFloor());
                                        listener.onFloorRefreshButtons();
                                        floorPath = mapDrawController.getDrawnPath().separateIntoFloors();

                                        assertEquals(1, floorPath.stream().filter(path -> path.getNodes().contains(node)).toArray().length);

                                        zoomToPath(floorPath.stream().filter(path -> path.getNodes().contains(node)).findFirst().get());
                                        return;
                                    }
                                }
                            }
                            selectedNodeEnd = dst;
                            Path path = map.getPath(map.findNodeClosestTo(selectedNodeStart.getPosition().getX(), selectedNodeStart.getPosition().getY(), true), dst);
                            mapDrawController.showPath(path);
                            listener.onNewPathSelected(path);
                            listener.onNewDestinationNode(selectedNodeEnd);
                            onChangePath(false);
                        } else {
                            return;
                        }
                    }
                    break;
                }
            }

            if (e.getButton() == MouseButton.SECONDARY && e.getClickCount() == 1) {
                if (viewMode == ViewMode.VIEW) {
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
                        onChangePath(true);
                    }
                }
            }

        });

        mapContainer.setOnDragDetected(e -> {
            Point2D mapPos = getMapPos(e);

            if (viewMode == ViewMode.EDIT && editMode == EditMode.MOVENODE) {
                Node node = map.findNodeClosestTo(mapPos.getX(), mapPos.getY(), map.is2D());
                double deltaX = mapPos.getX() - (map.is2D() ? node.getPosition().getX() : node.getWireframePosition().getX());
                double deltaY = mapPos.getY() - (map.is2D() ? node.getPosition().getY() : node.getWireframePosition().getY());
                draggingNode = false;
                if (Math.sqrt((deltaX * deltaX) + (deltaY * deltaY)) < 20) {
                    draggingNode = true;
                    heldNode = node;
                }
            }
        });

        mapContainer.setOnMouseDragged(e -> {
            Point2D mapPos = getMapPos(e);
            if (viewMode == ViewMode.EDIT && draggingNode && editMode == EditMode.MOVENODE && nodeSelectedOnMousePressed) {
                if (map.is2D()) {
                    heldNode.setPosition(mapPos);
                } else {
                    heldNode.setWireframePosition(mapPos);
                }

            }
        });

        mapContainer.addEventHandler(Event.ANY, e -> {
            if (((gesturePane.targetPointAtViewportCentre().getX() > 427 || gesturePane.targetPointAtViewportCentre().getX() < 417) && (gesturePane.targetPointAtViewportCentre().getY() > 348 || gesturePane.targetPointAtViewportCentre().getY() < 230)) && gesturePane.getCurrentScale() <= 2.3) {
                listener.onRefresh();
            }
        });
    }

    public Path changePathDestination(Node destinationNode) {
        selectedNodeEnd = destinationNode;
        Path path = map.getPath(selectedNodeStart, destinationNode);
        mapDrawController.showPath(path);
        map.setFloor(destinationNode.getFloor());
        onChangePath(false);
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
        mapDrawController.addDefaultStartNode(selectedNodeStart);
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
            mapDrawController.addDefaultStartNode(null);
        } else {
            mapDrawController.unshowNodes();
            mapDrawController.unshowEdges();
            mapDrawController.unshowPath();
            mapDrawController.unselectNode();
            resetStartLocation();
            // hide unused popups
            listener.onUpdateModifyNodePane(true, false, null);
            listener.onHideNewNodePopup();
        }
    }


    public Path changePath(Node src, Node dst) {
        selectedNodeStart = src;
        selectedNodeEnd = dst;
        Path newPath = map.getPath(src, dst);
        mapDrawController.showPath(newPath);
        mapDrawController.addDefaultStartNode(selectedNodeStart);
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

        // hide all popups
        listener.onUpdateModifyNodePane(true, false, null);
        listener.onHideNewNodePopup();

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

    public GesturePane getGesturePane() {
        return gesturePane;
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
            if (isMap2D != map.is2D()) {
                onChangePath(true);
            }
            if (!mapFloorDrawn.equals(map.getFloor()) || isMap2D != map.is2D()) {
                refreshFloorDrawn();
                mapDrawController.refreshPath();
            }

        }
    }
}

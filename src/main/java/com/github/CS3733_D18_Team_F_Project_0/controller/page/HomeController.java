package com.github.CS3733_D18_Team_F_Project_0.controller.page;

import com.github.CS3733_D18_Team_F_Project_0.ImageCacheSingleton;
import com.github.CS3733_D18_Team_F_Project_0.Map;
import com.github.CS3733_D18_Team_F_Project_0.MapSingleton;
import com.github.CS3733_D18_Team_F_Project_0.controller.*;
import com.github.CS3733_D18_Team_F_Project_0.controller.PaneSwitcher;
import com.github.CS3733_D18_Team_F_Project_0.controller.Screens;
import com.github.CS3733_D18_Team_F_Project_0.controller.SwitchableController;
import com.github.CS3733_D18_Team_F_Project_0.controller.UTF8Control;
import com.github.CS3733_D18_Team_F_Project_0.graph.Edge;
import com.github.CS3733_D18_Team_F_Project_0.graph.Node;
import javafx.animation.Interpolator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;

import java.util.Locale;
import java.util.ResourceBundle;


public class HomeController implements SwitchableController {

    private static final int MIN_PIXELS = 200;
    private final ObservableList<String> patientRooms = FXCollections.observableArrayList(
            "Patient Room 1",
            "Patient Room 2",
            "Patient Room 3");
    private final ObservableList<String> bathrooms = FXCollections.observableArrayList(
            "Bathroom 1",
            "Bathroom 2");
    private final ObservableList<String> all = FXCollections.observableArrayList();
    @FXML
    public Button DirectionsSwitch;
    @FXML
    public ComboBox cboxDestinationType;
    @FXML
    public ComboBox cboxAvailableLocations;
    private PaneSwitcher switcher;
    private Map map;
    //private int level = 4;

    public static Image maps2D[];
    public static Image maps3D[];

    @FXML
    private ImageView ivMap;
    @FXML
    private Pane mapContainer;
    @FXML
    private ScrollPane scrollMap;

    @FXML
    private VBox vbxMenu;
    @FXML
    private VBox vbxLocation;
    @FXML
    private VBox vbxDirections;
    @FXML
    private VBox vbxFloor;
    @FXML
    private VBox addLocationPopup;
    @FXML
    private TextField txtXPos;
    @FXML
    private TextField txtYPos;
    @FXML
    private VBox findLocationPopup;
    @FXML
    private Circle locationCircle;
    @FXML
    private Text txtFindLocation;
    @FXML
    private Button btnLocationDirections;

    @FXML
    private GesturePane gesturePane;

    @FXML
    private Button btnLower2Floor;
    @FXML
    private Button btnLower1Floor;
    @FXML
    private Button btnGroundFloor;
    @FXML
    private Button btnFirstFloor;
    @FXML
    private Button btnSecondFloor;
    @FXML
    private Button btnThirdFloor;

    @FXML
    private Text txtUser;
    @FXML
    private Button btnMapDimensions;

    @Override
    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;
        map = MapSingleton.getInstance().getMap();
        //to make initial admin with secure password
        txtUser.setText(PermissionSingleton.getInstance().getCurrUser());

        maps2D = ImageCacheSingleton.maps2D;
        maps3D = ImageCacheSingleton.maps3D;

        // testing area for db sync
        /*
        Node rNode = map.getNodes(node -> node.getNodeID().equals("HREST77702")).iterator().next();
        //map.removeNode(rNode);
        rNode.setWireframePosition(new Point2D(777,777));
        */

        this.draw2DNodes(MapSingleton.floor);

        // zoom*2 on double-click
        gesturePane.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                Point2D pivotOnTarget = gesturePane.targetPointAt(new Point2D(e.getX(), e.getY()))
                        .orElse(gesturePane.targetPointAtViewportCentre());
                // increment of scale makes more sense exponentially instead of linearly
                gesturePane.animate(Duration.millis(200))
                        .interpolateWith(Interpolator.EASE_BOTH)
                        .zoomBy(gesturePane.getCurrentScale(), pivotOnTarget);
            }
        });

        /*
        gesturePane.setOnScroll(e -> {
                    Point2D pivotOnTarget = gesturePane.targetPointAt(new Point2D(e.getX(), e.getY()))
                            .orElse(gesturePane.targetPointAtViewportCentre());
                    gesturePane.zoomBy(-1.5, pivotOnTarget);
                }
        );
        */

        // mouse start *************************************************************

        //double width = ivMap.getImage().getWidth();
        //double height = ivMap.getImage().getHeight();
        //reset(ivMap, width, height);
/*
        ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();

        ivMap.setOnMousePressed(e -> {
            Point2D mousePress = imageViewToImage(ivMap, new Point2D(e.getX(), e.getY()));
            mouseDown.set(mousePress);
        });
        ivMap.setOnMouseDragged(e -> {
            Point2D dragPoint = imageViewToImage(ivMap, new Point2D(e.getX(), e.getY()));
            shift(ivMap, dragPoint.subtract(mouseDown.get()));
            mouseDown.set(imageViewToImage(ivMap, new Point2D(e.getX(), e.getY())));
        });
        ivMap.setOnScroll(e -> {
            double delta = e.getDeltaY();
            Rectangle2D viewport = ivMap.getViewport();

            double scale = clamp(Math.pow(1.01, delta),

                    // don't scale so we're zoomed in to fewer than MIN_PIXELS in any direction:
                    Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),

                    // don't scale so that we're bigger than image dimensions:
                    Math.max(width / viewport.getWidth(), height / viewport.getHeight())

            );

            Point2D mouse = imageViewToImage(ivMap, new Point2D(e.getX(), e.getY()));

            double newWidth = viewport.getWidth() * scale;
            double newHeight = viewport.getHeight() * scale;

            // To keep the visual point under the mouse from moving, we need
            // (x - newViewportMinX) / (x - currentViewportMinX) = scale
            // where x is the mouse X coordinate in the image

            // solving this for newViewportMinX gives

            // newViewportMinX = x - (x - currentViewportMinX) * scale

            // we then clamp this value so the image never scrolls out
            // of the imageview:

            double newMinX = clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale,
                    0, width - newWidth);
            double newMinY = clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale,
                    0, height - newHeight);

            ivMap.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
        });

        ivMap.fitWidthProperty().bindBidirectional(mapContainer.maxWidthProperty());
        ivMap.fitHeightProperty().bindBidirectional(mapContainer.maxHeightProperty());


        // mouse end *************************************************************

        // the add location popup
        ivMap.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && addLocationPopup.isVisible() == false) {
                addLocationPopup.setTranslateX(e.getX() + 10);
                addLocationPopup.setTranslateY(e.getY() - 180);
                txtXPos.setText("" + (int) e.getX());
                txtYPos.setText("" + (int) e.getY());
                addLocationPopup.setVisible(true);
            }
        });
        */

        cboxDestinationType.getItems().clear();
        cboxDestinationType.getItems().addAll(
                "All",
                "Patient Room",
                "Bathroom",
                "ATM",
                "Emergrency Services");
        cboxDestinationType.getSelectionModel().selectFirst(); // or ".select("All");

        //cboxAvailableLocations.getItems().addAll(patientRooms, bathrooms);

        all.addAll(patientRooms);
        all.addAll(bathrooms);
        cboxAvailableLocations.getItems().addAll(all);
    }


    // Popup upon login

    @FXML
    void onLoginPopup() {
        switcher.popup(Screens.Login);
    }


    // Menus on right

    @FXML
    void onFindLocation() {
        vbxMenu.setVisible(false);
        vbxLocation.setVisible(true);
    }

    @FXML
    void onLocationCancel() {
        vbxLocation.setVisible(false);
        vbxMenu.setVisible(true);
    }

    @FXML
    void onGetDirections() {
        vbxMenu.setVisible(false);
        vbxDirections.setVisible(true);
    }

    @FXML
    void onDirectionsCancel() {
        vbxDirections.setVisible(false);
        vbxMenu.setVisible(true);
    }

    @FXML
    void onSwitchFloor() {
        vbxMenu.setVisible(false);
        vbxFloor.setVisible(true);
    }

    @FXML
    void onFloorCancel() {
        vbxFloor.setVisible(false);
        vbxMenu.setVisible(true);
    }


    // Language

    @FXML
    void onEnglish() {
        switcher.switchResource(ResourceBundle.getBundle("LanguageBundle", new Locale("en", "US")),
                Screens.Home);
    }

    @FXML
    void onFrench() {
        switcher.switchResource(ResourceBundle.getBundle("LanguageBundle", new Locale("fr", "FR")),
                Screens.Home);
    }

    @FXML
    void onSpanish() {
        switcher.switchResource(ResourceBundle.getBundle("LanguageBundle", new Locale("es", "ES")),
                Screens.Home);
    }

    @FXML
    void onChinese() {
        switcher.switchResource(ResourceBundle.getBundle("LanguageBundle", new Locale("zh", "CN"), new UTF8Control()),
                Screens.Home);
    }

    @FXML
    void onServiceRequest() {
        switcher.switchTo(Screens.ServiceRequest);
    }


    // Adjusting the map

    @FXML
    void onMapDimensions() {
        if (MapSingleton.is2D) {
            btnMapDimensions.setText("2D Map");
            MapSingleton.is2D = false;
            reloadMap();
        } else {
            btnMapDimensions.setText("3D Map");
            MapSingleton.is2D = true;
            reloadMap();
        }
        // make the map full sized when changed over
        double width = ivMap.getImage().getWidth();
        double height = ivMap.getImage().getHeight();
        reset(ivMap, width, height);
    }

    // Add location on map

    @FXML
    void onAddLocationConfirm() {
        // TODO Send txtXPos.getText() and txtYPos.getText() to database
        addLocationPopup.setVisible(false);
        txtXPos.setText("");
        txtYPos.setText("");
    }

    @FXML
    void onAddLocationCancel() {
        addLocationPopup.setVisible(false);
        txtXPos.setText("");
        txtYPos.setText("");
    }


    // Find Location

    @FXML
    void onDestinationType() {
        // create an object with a translation string and a database string name/id?
        if (cboxDestinationType.getSelectionModel().getSelectedItem().equals("Patient Room")) {
            cboxAvailableLocations.setItems(patientRooms);
        } else if (cboxDestinationType.getSelectionModel().getSelectedItem().equals("Bathroom")) {
            cboxAvailableLocations.setItems(bathrooms);
        } else {
            // default to displaying all types?
            cboxAvailableLocations.setItems(all);
        }
    }

    @FXML
    void onAvailableLocations() {
        // TODO get the actual x, y, and name
        // need that database package object as above^
        // get id from object, get x and y from database

        // TODO need some sort of update() in order to animate/shrink the circle :((
        // display the location at this location
        findLocationPopup.setTranslateX(100);
        findLocationPopup.setTranslateY(100);
        txtFindLocation.setText("" + cboxAvailableLocations.getSelectionModel().getSelectedItem());
        findLocationPopup.setVisible(true);

    }

    @FXML
    void onLocationDirections() {
        // hide location selection
        findLocationPopup.setVisible(false);
        vbxLocation.setVisible(false);
        // find directions with this location
        // TODO need to set field of txtfield first!!!!! (like putting in register before pass)
        onGetDirections();
    }


    @FXML
    void changeFloorMap(ActionEvent e) {
        if (e.getSource().equals(btnLower2Floor)) {
            //level = 0;
            MapSingleton.floor = "L2";
        } else if (e.getSource().equals(btnLower1Floor)) {
            //level = 1;
            MapSingleton.floor = "L1";
        } else if (e.getSource().equals(btnGroundFloor)) {
            //level = 2;
            MapSingleton.floor = "0G";
        } else if (e.getSource().equals(btnFirstFloor)) {
            //level = 3;
            MapSingleton.floor = "01";
        } else if (e.getSource().equals(btnSecondFloor)) {
            //level = 4;
            MapSingleton.floor = "02";
        } else if (e.getSource().equals(btnThirdFloor)) {
            //level = 5;
            MapSingleton.floor = "03";
        }
        reloadMap();
    }

    private void reloadMap() {
        int index;
        if (MapSingleton.floor.equals("L2")) {
            index = 0;
        } else if (MapSingleton.floor.equals("L1")) {
            index = 1;
        } else if (MapSingleton.floor.equals("0G")) {
            index = 2;
        } else if (MapSingleton.floor.equals("01")) {
            index = 3;
        } else if (MapSingleton.floor.equals("02")) {
            index = 4;
        } else {
            index = 5;
        }

        if (!MapSingleton.is2D) {
            ivMap.setImage(maps3D[index]);
            clearNodes();
            draw3DNodes(MapSingleton.floor);
        } else {
            ivMap.setImage(maps2D[index]);
            clearNodes();
            draw2DNodes(MapSingleton.floor);
        }
    }

    // Image movement helper functions

    // reset to the top left:
    private void reset(ImageView imageView, double width, double height) {
        imageView.setViewport(new Rectangle2D(0, 0, width, height));
    }

    // shift the viewport of the imageView by the specified delta, clamping so
    // the viewport does not move off the actual image:
    private void shift(ImageView imageView, Point2D delta) {
        Rectangle2D viewport = imageView.getViewport();

        double width = imageView.getImage().getWidth();
        double height = imageView.getImage().getHeight();

        double maxX = width - viewport.getWidth();
        double maxY = height - viewport.getHeight();

        double minX = clamp(viewport.getMinX() - delta.getX(), 0, maxX);
        double minY = clamp(viewport.getMinY() - delta.getY(), 0, maxY);

        imageView.setViewport(new Rectangle2D(minX, minY, viewport.getWidth(), viewport.getHeight()));
    }

    private double clamp(double value, double min, double max) {

        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    // convert mouse coordinates in the imageView to coordinates in the actual image:
    private Point2D imageViewToImage(ImageView imageView, Point2D imageViewCoordinates) {
        double xProportion = imageViewCoordinates.getX() / imageView.getBoundsInLocal().getWidth();
        double yProportion = imageViewCoordinates.getY() / imageView.getBoundsInLocal().getHeight();

        Rectangle2D viewport = imageView.getViewport();
        return new Point2D(
                viewport.getMinX() + xProportion * viewport.getWidth(),
                viewport.getMinY() + yProportion * viewport.getHeight());
    }

    private void draw2DNodes(String newLevel) {
        map = MapSingleton.getInstance().getMap();
        for (Edge edge : map.getEdges(edge -> edge.getNode2().getFloor().equals(newLevel))) {
            Line line = new Line();
            line.setEndX(edge.getNode1().getPosition().getX() * mapContainer.getMaxWidth() / 5000.f);
            line.setEndY(edge.getNode1().getPosition().getY() * mapContainer.getMaxHeight() / 3400.f);
            line.setStartX(edge.getNode2().getPosition().getX() * mapContainer.getMaxWidth() / 5000.f);
            line.setStartY(edge.getNode2().getPosition().getY() * mapContainer.getMaxHeight() / 3400.f);
            mapContainer.getChildren().add(line);
        }
        for (Node node : map.getNodes(node -> node.getFloor().equals(newLevel))) {
            Circle circle = new Circle(1.5, Color.RED);
            circle.setCenterX(node.getPosition().getX() * mapContainer.getMaxWidth() / 5000.f);
            circle.setCenterY(node.getPosition().getY() * mapContainer.getMaxHeight() / 3400.f);
            mapContainer.getChildren().add(circle);
        }
    }

    private void draw3DNodes(String newLevel) {
        map = MapSingleton.getInstance().getMap();
        for (Edge edge : map.getEdges(edge -> edge.getNode2().getFloor().equals(newLevel))) {
            Line line = new Line();
            line.setEndX(edge.getNode1().getWireframePosition().getX() * mapContainer.getMaxWidth() / 5000.f);
            line.setEndY(edge.getNode1().getWireframePosition().getY() * mapContainer.getMaxHeight() / 2772.f);
            line.setStartX(edge.getNode2().getWireframePosition().getX() * mapContainer.getMaxWidth() / 5000.f);
            line.setStartY(edge.getNode2().getWireframePosition().getY() * mapContainer.getMaxHeight() / 2772.f);
            mapContainer.getChildren().add(line);
        }
        for (Node node : map.getNodes(node -> node.getFloor().equals(newLevel))) {
            Circle circle = new Circle(1.5, Color.RED);
            circle.setCenterX(node.getWireframePosition().getX() * mapContainer.getMaxWidth() / 5000.f);
            circle.setCenterY(node.getWireframePosition().getY() * mapContainer.getMaxHeight() / 2772.f);
            mapContainer.getChildren().add(circle);
        }
    }

    private void clearNodes() {
        mapContainer.getChildren().clear();
        mapContainer.getChildren().add(ivMap);
    }
}
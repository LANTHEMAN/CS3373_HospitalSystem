package com.github.CS3733_D18_Team_F_Project_0.controller.page;

import com.github.CS3733_D18_Team_F_Project_0.Map;
import com.github.CS3733_D18_Team_F_Project_0.MapSingleton;
import com.github.CS3733_D18_Team_F_Project_0.controller.PaneSwitcher;
import com.github.CS3733_D18_Team_F_Project_0.controller.Screens;
import com.github.CS3733_D18_Team_F_Project_0.controller.SwitchableController;
import com.github.CS3733_D18_Team_F_Project_0.controller.UTF8Control;
import com.github.CS3733_D18_Team_F_Project_0.graph.Node;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.Locale;
import java.util.ResourceBundle;


public class HomeController implements SwitchableController {

    private final ObservableList<String> patientRooms = FXCollections.observableArrayList(
            "Patient Room 1",
            "Patient Room 2",
            "Patient Room 3");
    private final ObservableList<String> bathrooms = FXCollections.observableArrayList(
            "Bathroom 1",
            "Bathroom 2");
    private final ObservableList<String> all = FXCollections.observableArrayList();
    private PaneSwitcher switcher;
    private Map map;
    private int level = 0;
    private Image maps2D[] = {
            new Image("com/github/CS3733_D18_Team_F_Project_0/controller/BW2D Maps/02_thesecondfloor.png")
    };
    private Image maps3D[] = {
            new Image("com/github/CS3733_D18_Team_F_Project_0/controller/Wireframes/2-ICONS.png")
    };

    @FXML
    public Button DirectionsSwitch;
    @FXML
    public ComboBox cboxDestinationType;
    @FXML
    public ComboBox cboxAvailableLocations;
    @FXML
    private ImageView ivMap;
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
    private Button btnMapDimensions;

    @FXML
    private Pane mapContainer;
    private static final int MIN_PIXELS = 200;

    @Override
    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;
        map = MapSingleton.getInstance().getMap();

        /*
        Pane root = switcher.getPane(Screens.Home);
        root.getChildren().add(new Button("Hello World"));
        */

        // preload the 2D and 3D floor map
        //image3D = new Image("com/github/CS3733_D18_Team_F_Project_0/controller/Wireframes/04 L2 NO ICONS.png");
        //image2D = new Image("com/github/CS3733_D18_Team_F_Project_0/controller/BW2D Maps/02_thesecondfloor.png");
        // testing area for db sync
        /*
        Node rNode = map.getNodes(node -> node.getNodeID().equals("HREST77702")).iterator().next();
        //map.removeNode(rNode);
        rNode.setWireframePosition(new Point2D(777,777));
        */

        // preload the 2D and 3D floor map
       // image3D = new Image("com/github/CS3733_D18_Team_F_Project_0/controller/Wireframes/04 L2 NO ICONS.png");
        //image2D = new Image("com/github/CS3733_D18_Team_F_Project_0/controller/BW2D Maps/02_thesecondfloor.png");
        // mouse start *************************************************************

        double width = ivMap.getImage().getWidth();
        double height = ivMap.getImage().getHeight();
        reset(ivMap, width, height);

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
/*        ivMap.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                reset(ivMap, width, height);
            }
        });
*/
        ivMap.fitWidthProperty().bind(mapContainer.widthProperty());
        ivMap.fitHeightProperty().bind(mapContainer.heightProperty());

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

        cboxDestinationType.getItems().clear();
        cboxDestinationType.getItems().addAll(
                "All",
                "Patient Room",
                "Bathroom",
                "ATM",
                "Emergrency Services");
        cboxDestinationType.getSelectionModel().selectFirst(); // or ".select("All");

        /*cboxAvailableLocations.getItems().clear();
        cboxAvailableLocations.getItems().addAll(
                "Patient Room 1",
                "Patient Room 2",
                "Patient Room 3");
        cboxAvailableLocations.setItems(patientRooms);*/
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
        if (btnMapDimensions.getText().equals("3D Map")) {
            //Image image = new Image("com/github/CS3733_D18_Team_F_Project_0/controller/Wireframes/04 L2 NO ICONS.png");
            btnMapDimensions.setText("2D Map");
            ivMap.setImage(maps3D[level]);
            double width = ivMap.getImage().getWidth();
            double height = ivMap.getImage().getHeight();
            reset(ivMap, width, height);
        } else {
            //Image image = new Image("com/github/CS3733_D18_Team_F_Project_0/controller/BW2D Maps/02_thesecondfloor.png");
            btnMapDimensions.setText("3D Map");
            ivMap.setImage(maps2D[level]);
            double width = ivMap.getImage().getWidth();
            double height = ivMap.getImage().getHeight();
            reset(ivMap, width, height);
        }

    }

    @FXML
    void onZoomIn() {
        // Viewports?
        ivMap.setFitHeight(ivMap.getFitHeight() * 2);
        ivMap.setFitWidth(ivMap.getFitWidth() * 2);
    }

    @FXML
    void onZoomOut() {
        ivMap.setFitHeight(ivMap.getFitHeight() * 0.5);
        ivMap.setFitWidth(ivMap.getFitWidth() * 0.5);
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


    // Image movement helper functions

    // reset to the top left:
    private void reset(ImageView imageView, double width, double height) {
        imageView.setViewport(new Rectangle2D(0, 0, width, height));
    }

    // shift the viewport of the imageView by the specified delta, clamping so
    // the viewport does not move off the actual image:
    private void shift(ImageView imageView, Point2D delta) {
        Rectangle2D viewport = imageView.getViewport();

        double width = imageView.getImage().getWidth() ;
        double height = imageView.getImage().getHeight() ;

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

}
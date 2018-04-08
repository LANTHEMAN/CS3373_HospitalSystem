package com.github.CS3733_D18_Team_F_Project_0.controller.page;

import com.github.CS3733_D18_Team_F_Project_0.ImageCacheSingleton;
import com.github.CS3733_D18_Team_F_Project_0.Map;
import com.github.CS3733_D18_Team_F_Project_0.MapSingleton;
import com.github.CS3733_D18_Team_F_Project_0.controller.*;
import com.github.CS3733_D18_Team_F_Project_0.gfx.PaneMapController;
import com.github.CS3733_D18_Team_F_Project_0.gfx.impl.UglyMapDrawer;
import com.github.CS3733_D18_Team_F_Project_0.graph.NewNodeBuilder;
import com.github.CS3733_D18_Team_F_Project_0.graph.Node;
import com.github.CS3733_D18_Team_F_Project_0.graph.NodeBuilder;
import com.github.CS3733_D18_Team_F_Project_0.voice.VoiceLauncher;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.SynchronousQueue;

public class HomeController implements SwitchableController, Observer{

    ConcurrentLinkedQueue<String> commands = new ConcurrentLinkedQueue<>();

    private static final int MIN_PIXELS = 200;
    private static Image maps2D[] = ImageCacheSingleton.maps2D;
    private static Image maps3D[] = ImageCacheSingleton.maps3D;
    private final ObservableList<String> patientRooms = FXCollections.observableArrayList(
            "Patient Room 1",
            "Patient Room 2",
            "Patient Room 3");
    private final ObservableList<String> bathrooms = FXCollections.observableArrayList(
            "Bathroom 1",
            "Bathroom 2");
    private final ObservableList<String> all = FXCollections.observableArrayList();


    private PaneMapController mapDrawController;
    private Circle newNodeCircle = new Circle(2, Color.BLUEVIOLET);
    private Node selectedNodeStart = null;
    private Node modifyNode = null;
    private boolean ctrlHeld = false;
    private PaneSwitcher switcher;
    private Map map;
    private ObservableResourceFactory resFactory = new ObservableResourceFactory();
    @FXML
    private ImageView ivMap;
    @FXML
    private Pane mapContainer;
    @FXML
    private VBox addLocationPopup;


    /**
     * New node window
     */
    @FXML
    private TextField newNode_x;
    @FXML
    private TextField newNode_y;
    @FXML
    private TextField newNode_shortName;
    @FXML
    private ComboBox<String> newNode_type = new ComboBox<>(FXCollections.observableArrayList(NodeBuilder.getNodeTypes()));

    @FXML
    private GesturePane gesturePane;
    @FXML
    private Text txtUser;
    @FXML
    private Button btnMapDimensions;
    @FXML
    private Button loginPopup;
    // the modify Node information panel on the left
    @FXML
    private GridPane gpaneNodeInfo;
    @FXML
    private TextField modNode_shortName;
    @FXML
    private TextField modNode_longName;
    @FXML
    private TextField modNode_x;
    @FXML
    private TextField modNode_y;

    Timeline commandExecuter = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            String command = commands.poll();
            if(command != null){
                if(command.equals("HELP")){
                    onHelpPopup();
                }
            }
        }
    }));

    @Override
    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;
        VoiceLauncher.getInstance().addObserver(this);

        commandExecuter.setCycleCount(Timeline.INDEFINITE);
        commandExecuter.play();

        // initialize map and map drawer
        map = MapSingleton.getInstance().getMap();
        mapDrawController = new PaneMapController(mapContainer, map, new UglyMapDrawer());
        mapDrawController.showPath(map.getPath
                (map.findNodeClosestTo(0, 0), map.findNodeClosestTo(10000, 0)));

        //to make initial admin with secure password
        txtUser.setText(PermissionSingleton.getInstance().getCurrUser());
        if (!PermissionSingleton.getInstance().getCurrUser().equals("Guest")) {
            loginPopup.setText("Logout");
        }

        // set default zoom
        gesturePane.zoomTo(2, new Point2D(600, 600));

        // dynamically change 2d/3d switcher button text
        if (map.is2D()) {
            StringBinding mapDim = switcher.resFac.getStringBinding("3DMap");
            btnMapDimensions.setText(mapDim.get());
        } else {
            StringBinding mapDim = switcher.resFac.getStringBinding("2DMap");
            btnMapDimensions.setText(mapDim.get());
        }

        // set up new node panel
        newNode_type.getItems().addAll(NodeBuilder.getNodeTypes());
        newNode_type.getSelectionModel().selectFirst();

        if(PermissionSingleton.getInstance().isAdmin()){
            mapDrawController.showNodes();
            mapDrawController.showEdges();
        }


        // react to key presses and mouse clicks
        switcher.getScene().setOnKeyPressed(ke -> {
            if (ke.isControlDown()) {
                gesturePane.setGestureEnabled(false);
                ctrlHeld = true;
            }
        });
        switcher.getScene().setOnKeyReleased(ke -> {
            if (!ke.isControlDown()) {
                gesturePane.setGestureEnabled(true);
                selectedNodeStart = null;
                ctrlHeld = false;
            }
        });
        mapContainer.setOnMouseReleased(e -> {
            if (selectedNodeStart == null || !map.is2D()) {
                return;
            }

            if (!PermissionSingleton.getInstance().isAdmin() || !ctrlHeld) {
                return;
            }

            double map_x = 5000;
            double map_y = 3400;
            double map3D_y = 2772;
            if (!map.is2D()) {
                map_y = map3D_y;
            }

            Point2D mapPos = new Point2D(e.getX() * map_x / mapContainer.getMaxWidth()
                    , e.getY() * map_y / mapContainer.getMaxHeight());

            HashSet<Node> nodes = map.getNodes(node -> new Point2D(node.getPosition().getX()
                    , node.getPosition().getY()).distance(mapPos) < 8
            );

            if (nodes.size() > 0) {
                // TODO get closest node
                Node node = nodes.iterator().next();
                mapDrawController.selectNode(node);

                if (node == selectedNodeStart) {
                    selectedNodeStart = null;
                    selectedNodeStart = null;
                    return;
                }

                if (e.getButton() == MouseButton.PRIMARY) {
                    map.addEdge(node, selectedNodeStart);
                }

                if (e.getButton() == MouseButton.SECONDARY) {
                    map.removeEdge(node, selectedNodeStart);
                }
                selectedNodeStart = null;
            } else {
                selectedNodeStart = null;
            }

        });
        mapContainer.setOnMouseClicked(e -> {
            double map_x = 5000;
            double map_y = 3400;
            double map3D_y = 2772;
            if (!map.is2D()) {
                map_y = map3D_y;
            }

            Point2D mapPos = new Point2D(e.getX() * map_x / mapContainer.getMaxWidth()
                    , e.getY() * map_y / mapContainer.getMaxHeight());

            HashSet<Node> nodes;
            if (map.is2D()) {
                nodes = map.getNodes(node -> new Point2D(node.getPosition().getX()
                        , node.getPosition().getY()).distance(mapPos) < 8);
            } else {
                nodes = map.getNodes(node -> new Point2D(node.getWireframePosition().getX()
                        , node.getWireframePosition().getY()).distance(mapPos) < 8);
            }

            if (nodes.size() > 0) {
                Node node = map.findNodeClosestTo(mapPos.getX(), mapPos.getY(), map.is2D());
                mapDrawController.selectNode(node);
                selectedNodeStart = node;

                if (PermissionSingleton.getInstance().isAdmin()) {
                    gpaneNodeInfo.setVisible(true);
                }

                modNode_x.setText(String.valueOf(node.getPosition().getX()));
                modNode_y.setText(String.valueOf(node.getPosition().getY()));
                modNode_shortName.setText(node.getShortName());
                modNode_longName.setText(node.getLongName());
                // TODO implement change building, change type

                modifyNode = node;

            } else {
                selectedNodeStart = null;
            }

            if (!PermissionSingleton.getInstance().isAdmin()) {
                return;
            }


            // remove a node
            if (e.getButton() == MouseButton.SECONDARY && e.getClickCount() == 2) {
                if (!map.is2D()) {
                    return;
                }
                if (nodes.size() > 0) {
                    // TODO get closest node
                    map.removeNode(selectedNodeStart);
                    selectedNodeStart = null;
                }
                selectedNodeStart = null;
            }
            // create a new node
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                if (!map.is2D()) {
                    return;
                }
                addLocationPopup.setTranslateX(e.getSceneX() - 90);
                addLocationPopup.setTranslateY(e.getSceneY() - 400);
                newNode_x.setEditable(false);
                newNode_y.setEditable(false);
                newNode_x.setText("" + (int) (e.getX() * map_x / mapContainer.getMaxWidth()));
                newNode_y.setText("" + (int) (e.getY() * map_y / mapContainer.getMaxHeight()));
                newNodeCircle.setVisible(true);
                newNodeCircle.setCenterX(Double.parseDouble(newNode_x.getText()) * mapContainer.getMaxWidth() / map_x);
                newNodeCircle.setCenterY(Double.parseDouble(newNode_y.getText()) * mapContainer.getMaxHeight() / map_y);
                addLocationPopup.setVisible(true);
                selectedNodeStart = null;
            }
        });
    }





    // Popup upon login

    @FXML
    void onLoginPopup() {
        if (loginPopup.getText().equals("Logout")) {
            PermissionSingleton.getInstance().logout();
            loginPopup.setText("Admin Login");
            txtUser.setText(PermissionSingleton.getInstance().getCurrUser());
            return;
        }
        switcher.popup(Screens.Login);
    }


    // Popup upon help request

    @FXML
    void onHelpPopup() {
        switcher.popup(Screens.Help);
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
        StringBinding mapDim;
        if (map.is2D()) {
            mapDim = switcher.resFac.getStringBinding("2DMap");
            btnMapDimensions.setText(mapDim.get());
            map.setIs2D(false);
            reloadMap();
        } else {
            mapDim = switcher.resFac.getStringBinding("3DMap");
            btnMapDimensions.setText(mapDim.get());
            map.setIs2D(true);
            reloadMap();
        }
    }


    // Add location on map

    @FXML
    void onAddLocationConfirm() {
        String type = newNode_type.getSelectionModel().getSelectedItem();
        // TODO move into NodeBuilder


        Node newNode = new NewNodeBuilder()
                .setNodeType(type)
                .setNumNodeType(map)
                .setFloor(map.getFloor())
                .setBuilding("New Building")    // TODO set building
                .setShortName(newNode_shortName.getText())
                .setLongName(newNode_shortName.getText()) // TODO get long name
                .setPosition(new Point2D(Double.parseDouble(newNode_x.getText()), Double.parseDouble(newNode_y.getText())))
                .build();
        map.createNode(newNode);

        addLocationPopup.setVisible(false);
        newNode_shortName.setText("");
        newNodeCircle.setVisible(false);

        reloadMap();
    }

    @FXML
    void onAddLocationCancel() {
        addLocationPopup.setVisible(false);
        newNode_shortName.setText("");
        newNodeCircle.setVisible(false);
    }

    @FXML
    void onModificationCancel() {
        gpaneNodeInfo.setVisible(false);
    }

    @FXML
    void onNodeModify() {
        modifyNode.setPosition(new Point2D(Double.parseDouble(modNode_x.getText())
                , Double.parseDouble(modNode_y.getText())));
        modifyNode.setShortName(modNode_shortName.getText());
        modifyNode.setLongName(modNode_longName.getText());
    }

    // Find Location
    @FXML
    void onDestinationType() {
        /*
        // create an object with a translation string and a database string name/id?
        if (cboxDestinationType.getSelectionModel().getSelectedItem().equals("Patient Room")) {
            cboxAvailableLocations.setItems(patientRooms);
        } else if (cboxDestinationType.getSelectionModel().getSelectedItem().equals("Bathroom")) {
            cboxAvailableLocations.setItems(bathrooms);
        } else {
            // default to displaying all types?
            cboxAvailableLocations.setItems(all);
        }
        */
    }

    @FXML
    void onAvailableLocations() {
        /*
        // TODO get the actual x, y, and name
        // need that database package object as above^
        // get id from object, get x and y from database

        // TODO need some sort of update() in order to animate/shrink the circle :((
        // display the location at this location
        findLocationPopup.setTranslateX(100);
        findLocationPopup.setTranslateY(100);
        txtFindLocation.setText("" + cboxAvailableLocations.getSelectionModel().getSelectedItem());
        findLocationPopup.setVisible(true);
        */
    }

    @FXML
    void onLocationDirections() {
        /*
        // hide location selection
        findLocationPopup.setVisible(false);
        vbxLocation.setVisible(false);
        // find directions with this location
        // TODO need to set field of txtfield first!!!!! (like putting in register before pass)
        onGetDirections();
        */
    }


    @FXML
    void changeFloorMap(ActionEvent e) {
        /*
        if (e.getSource().equals(btnLower2Floor)) {
            map.setFloor("L2");
            MainTitle.setText("Brigham and Women's Hospital: Lower Level 2");
        } else if (e.getSource().equals(btnLower1Floor)) {
            map.setFloor("L1");
            MainTitle.setText("Brigham and Women's Hospital: Lower Level 1");
        } else if (e.getSource().equals(btnGroundFloor)) {
            map.setFloor("0G");
            MainTitle.setText("Brigham and Women's Hospital: Ground Floor");
        } else if (e.getSource().equals(btnFirstFloor)) {
            map.setFloor("01");
            MainTitle.setText("Brigham and Women's Hospital: Level 1");
        } else if (e.getSource().equals(btnSecondFloor)) {
            map.setFloor("02");
            MainTitle.setText("Brigham and Women's Hospital: Level 2");
        } else if (e.getSource().equals(btnThirdFloor)) {
            map.setFloor("03");
            MainTitle.setText("Brigham and Women's Hospital: Level 3");
        }
        reloadMap();
        */
    }

    private void reloadMap() {
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

        if (!map.is2D()) {
            ivMap.setImage(maps3D[index]);

        } else {
            ivMap.setImage(maps2D[index]);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(o instanceof VoiceLauncher)) {
            return;
        }

        if(arg instanceof String){
            String cmd = (String) arg;

            if(arg.toString().equals("HOSPITAL KIOSK")){
                System.out.println("Hospital Kiosk");
            }

            if(arg.toString().equals("HELP")){
                commands.add(cmd);
            }
        }
    }
}
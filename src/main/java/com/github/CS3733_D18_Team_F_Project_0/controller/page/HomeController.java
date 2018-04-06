package com.github.CS3733_D18_Team_F_Project_0.controller.page;

import com.github.CS3733_D18_Team_F_Project_0.ImageCacheSingleton;
import com.github.CS3733_D18_Team_F_Project_0.Map;
import com.github.CS3733_D18_Team_F_Project_0.MapSingleton;
import com.github.CS3733_D18_Team_F_Project_0.controller.*;
import com.github.CS3733_D18_Team_F_Project_0.graph.*;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import net.kurobako.gesturefx.GesturePane;

import java.util.*;

import static com.github.CS3733_D18_Team_F_Project_0.MapSingleton.floor;
import static java.lang.Math.cos;
import static java.lang.StrictMath.sin;


public class HomeController implements SwitchableController {

    private static final int MIN_PIXELS = 200;
    public static Image maps2D[];
    public static Image maps3D[];
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
    public ComboBox<String> cboxDestinationType;
    @FXML
    public ComboBox<String> cboxAvailableLocations;
    Circle newNodeCircle = new Circle(2, Color.BLUEVIOLET);
    HashMap<Node, Circle> nodeCircleHashMap = new HashMap<>();
    Node selectedNodeStart = null;
    Node pathStartNode = null;
    Node pathEndNode = null;
    Node modifyNode = null;
    boolean ctrlHeld = false;
    private PaneSwitcher switcher;
    private Map map;
    private ObservableResourceFactory resFactory = new ObservableResourceFactory();
    @FXML
    private ImageView ivMap;
    @FXML
    private Pane mapContainer;
    @FXML
    private ScrollPane scrollMap;
    @FXML
    private Text MainTitle;
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
    private TextField newNode_x;
    @FXML
    private TextField newNode_y;
    @FXML
    private TextField newNode_shortName;
    @FXML
    private ComboBox<String> newNode_type;
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
    private ComboBox modNode_type;
    @FXML
    private ComboBox modNode_building;
    @FXML
    private TextField modNode_x;
    @FXML
    private TextField modNode_y;


    @FXML
    private Slider tf_transx;
    @FXML
    private Slider tf_transy;
    @FXML
    private Slider tf_scalex;
    @FXML
    private Slider tf_scaley;
    @FXML
    private Slider tf_rotate;
    @FXML
    private Slider tf_transxp;
    @FXML
    private Slider tf_transyp;

    @Override
    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;
        map = MapSingleton.getInstance().getMap();
        //to make initial admin with secure password
        txtUser.setText(PermissionSingleton.getInstance().getCurrUser());
        if (!PermissionSingleton.getInstance().getCurrUser().equals("Guest")) {
            loginPopup.setText("Logout");
        }



        tf_transx.setMin(-3000);
        tf_transx.setMax(3000);
        tf_transy.setMin(-3000);
        tf_transy.setMax(3000);

        tf_transxp.setMin(0);
        tf_transxp.setMax(4000);
        tf_transyp.setMin(0);
        tf_transyp.setMax(4000);

        tf_scalex.setMin(0);
        tf_scalex.setMax(1000);
        tf_scaley.setMin(0);
        tf_scaley.setMax(1000);

        tf_rotate.setMin(0);
        tf_rotate.setMax(700);


        tf_transx.setOnMouseReleased(event -> {
            clearNodes();
            draw3DNodes(MapSingleton.floor);
        });
        tf_transy.setOnMouseReleased(event -> {
            clearNodes();
            draw3DNodes(MapSingleton.floor);
        });
        tf_transxp.setOnMouseReleased(event -> {
            clearNodes();
            draw3DNodes(MapSingleton.floor);
        });
        tf_transyp.setOnMouseReleased(event -> {
            clearNodes();
            draw3DNodes(MapSingleton.floor);
        });
        tf_scalex.setOnMouseReleased(event -> {
            clearNodes();
            draw3DNodes(MapSingleton.floor);
        });
        tf_scaley.setOnMouseReleased(event -> {
            clearNodes();
            draw3DNodes(MapSingleton.floor);
        });
        tf_rotate.setOnMouseReleased(event -> {
            clearNodes();
            draw3DNodes(MapSingleton.floor);
        });






        gpaneNodeInfo.setVisible(false);

        maps2D = ImageCacheSingleton.maps2D;
        maps3D = ImageCacheSingleton.maps3D;

        if (MapSingleton.is2D) {
            StringBinding mapDim = switcher.resFac.getStringBinding("3DMap");
            btnMapDimensions.setText(mapDim.get());
        } else {
            StringBinding mapDim = switcher.resFac.getStringBinding("2DMap");
            btnMapDimensions.setText(mapDim.get());
        }

        gesturePane.zoomTo(2, new Point2D(600, 600));

        newNode_type.getItems().addAll("HALL"
                , "ELEV" // TODO set elevator query
                , "REST"
                , "STAI"
                , "DEPT"
                , "LABS"
                , "INFO"
                , "CONF"
                , "EXIT"
                , "ELEV"
                , "RETL"
                , "SERV");
        newNode_type.getSelectionModel().selectFirst();

        // testing area for db sync
        /*
        Node rNode = map.getNodes(node -> node.getNodeID().equals("HREST77702")).iterator().next();
        //map.removeNode(rNode);
        rNode.setWireframePosition(new Point2D(777,777));
        */
        reloadMap();
        switcher.getScene().setOnKeyPressed(ke -> {
            if (ke.isControlDown()) {
                gesturePane.setGestureEnabled(false);
                ctrlHeld = true;
            }
        });
        switcher.getScene().setOnKeyReleased(ke -> {
            clearNodes();
            draw3DNodes(MapSingleton.floor);

            if (!ke.isControlDown()) {
                gesturePane.setGestureEnabled(true);
                selectedNodeStart = null;
                ctrlHeld = false;
            }
        });

        mapContainer.setOnMouseReleased(e -> {
            if (selectedNodeStart == null || !MapSingleton.is2D) {
                return;
            }

            if (!PermissionSingleton.getInstance().isAdmin() || !ctrlHeld) {
                return;
            }

            double map_x = 5000;
            double map_y = 3400;
            double map3D_x = 5000;
            double map3D_y = 2772;
            if (!MapSingleton.is2D) {
                map_x = map3D_x;
                map_y = map3D_y;
            }

            Point2D mapPos = new Point2D(e.getX() * map_x / mapContainer.getMaxWidth()
                    , e.getY() * map_y / mapContainer.getMaxHeight());

            HashSet<Node> nodes = map.getNodes(node -> new Point2D(node.getPosition().getX()
                    , node.getPosition().getY()).distance(mapPos) < 8
            );

            if (nodes.size() > 0) {
                // TODO get closest node
                clearNodes();
                reloadMap();
                Node node = nodes.iterator().next();
                nodeCircleHashMap.get(node).setFill(Color.BLUE);

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
                clearNodes();
                reloadMap();
            } else {
                selectedNodeStart = null;
            }

        });

        mapContainer.setOnMouseClicked(e -> {
            double map_x = 5000;
            double map_y = 3400;
            double map3D_x = 5000;
            double map3D_y = 2772;
            if (!MapSingleton.is2D) {
                map_x = map3D_x;
                map_y = map3D_y;
            }

            Point2D mapPos = new Point2D(e.getX() * map_x / mapContainer.getMaxWidth()
                    , e.getY() * map_y / mapContainer.getMaxHeight());

            HashSet<Node> nodes;
            if (MapSingleton.is2D) {
                nodes = map.getNodes(node -> new Point2D(node.getPosition().getX()
                        , node.getPosition().getY()).distance(mapPos) < 8);
            } else {
                nodes = map.getNodes(node -> new Point2D(node.getWireframePosition().getX()
                        , node.getWireframePosition().getY()).distance(mapPos) < 8);
            }


            if (nodes.size() > 0) {
                // TODO get closest node
                clearNodes();
                reloadMap();
                Node node = nodes.iterator().next();
                nodeCircleHashMap.get(node).setFill(Color.BLUE);
                selectedNodeStart = node;
                // TODO set modification fields to the appropriate values****

                if (PermissionSingleton.getInstance().isAdmin()) {
                    gpaneNodeInfo.setVisible(true);
                }

                if (MapSingleton.is2D) {
                    modNode_x.setText(String.valueOf(node.getPosition().getX()));
                    modNode_y.setText(String.valueOf(node.getPosition().getY()));
                }else{
                    modNode_x.setText(String.valueOf(node.getWireframePosition().getX()));
                    modNode_y.setText(String.valueOf(node.getWireframePosition().getY()));
                }

                modNode_shortName.setText(node.getShortName());
                modNode_longName.setText(node.getLongName());

                // TODO implement change building, change type

                modifyNode = node;


                if (vbxDirections.isVisible()) {
                    if (pathStartNode == null) {
                        pathStartNode = selectedNodeStart;
                    } else if (selectedNodeStart != pathStartNode) {
                        pathEndNode = selectedNodeStart;
                    }
                }

            } else {
                selectedNodeStart = null;
            }

            if (!PermissionSingleton.getInstance().isAdmin()) {
                return;
            }


            // remove a node
            if (e.getButton() == MouseButton.SECONDARY && e.getClickCount() == 2) {
                if (!MapSingleton.is2D) {
                    return;
                }
                if (nodes.size() > 0) {
                    // TODO get closest node
                    map.removeNode(selectedNodeStart);
                    selectedNodeStart = null;
                    clearNodes();
                    reloadMap();
                }
                selectedNodeStart = null;
            }
            // create a new node
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                if (!MapSingleton.is2D) {
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

        /*for(int i = 0; i < 25; i++){
            for(int j = 0; j < 34; j++){
                int typeCount = 0;
                String type = "HALL";
                    try {
                        typeCount = map.getNodes()
                                .stream()
                                .filter(node -> node.getNodeType().equals(type))
                                .map(node -> node.getNodeID().substring(5, 8))
                                .map(Integer::parseInt)
                                .max(Integer::compare)
                                .get();
                        typeCount++;
                    } catch (Exception e) {
                        typeCount = 0;
                    }
                Node newNode = new NewNodeBuilder()
                        .setNodeType(type)
                        .setNumNodeType(typeCount)
                        .setFloor("02")
                        .setBuilding("New Building")    // TODO set building
                        .setShortName("afwif" + i + "" + j)
                        .setLongName("aiwheifuah" + i + "" + j) // TODO get long name
                        .setPosition(new Point3D(i * 5000/25, j * 3400/34, 100))
                        .build();
                map.createNode(newNode);
            }
        }*/

        reloadMap();
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

    @FXML
    void onDrawPath() {
        if (pathStartNode != null && pathEndNode != null) {
            Path path = MapSingleton.getInstance().getMap().getPath(pathStartNode, pathEndNode);
            ArrayList<Node> nodes = path.getNodes();
            ArrayList<Edge> edges = path.getEdges();

            if (MapSingleton.is2D) {
                for (Edge edge : edges) {
                    Line line = new Line();
                    line.setEndX(edge.getNode1().getPosition().getX() * mapContainer.getMaxWidth() / 5000.f);
                    line.setEndY(edge.getNode1().getPosition().getY() * mapContainer.getMaxHeight() / 3400.f);
                    line.setStartX(edge.getNode2().getPosition().getX() * mapContainer.getMaxWidth() / 5000.f);
                    line.setStartY(edge.getNode2().getPosition().getY() * mapContainer.getMaxHeight() / 3400.f);
                    line.setFill(Color.BLUE);
                    mapContainer.getChildren().add(line);
                }
                for (Node node : nodes) {
                    Circle circle = new Circle(1.5, Color.RED);
                    circle.setCenterX(node.getPosition().getX() * mapContainer.getMaxWidth() / 5000.f);
                    circle.setCenterY(node.getPosition().getY() * mapContainer.getMaxHeight() / 3400.f);
                    circle.setFill(Color.PURPLE);
                    mapContainer.getChildren().add(circle);
                }

                Text startText = new Text(pathStartNode.getShortName());
                Text endText = new Text(pathEndNode.getShortName());

                startText.setX(pathStartNode.getPosition().getX() * mapContainer.getMaxWidth() / 5000.f);
                startText.setY(pathStartNode.getPosition().getY() * mapContainer.getMaxHeight() / 3400.f);
                startText.setFont(Font.font("Verdana", 5));
                endText.setX(pathEndNode.getPosition().getX() * mapContainer.getMaxWidth() / 5000.f);
                endText.setY(pathEndNode.getPosition().getY() * mapContainer.getMaxHeight() / 3400.f);
                endText.setFont(Font.font("Verdana", 5));

                mapContainer.getChildren().addAll(startText, endText);
            } else {
                for (Edge edge : edges) {
                    Line line = new Line();
                    line.setEndX(edge.getNode1().getWireframePosition().getX() * mapContainer.getMaxWidth() / 5000.f);
                    line.setEndY(edge.getNode1().getWireframePosition().getY() * mapContainer.getMaxHeight() / 2772.f);
                    line.setStartX(edge.getNode2().getWireframePosition().getX() * mapContainer.getMaxWidth() / 5000.f);
                    line.setStartY(edge.getNode2().getWireframePosition().getY() * mapContainer.getMaxHeight() / 2772.f);
                    line.setFill(Color.BLUE);
                    mapContainer.getChildren().add(line);
                }
                for (Node node : nodes) {
                    Circle circle = new Circle(1.5, Color.RED);
                    circle.setCenterX(node.getWireframePosition().getX() * mapContainer.getMaxWidth() / 5000.f);
                    circle.setCenterY(node.getWireframePosition().getY() * mapContainer.getMaxHeight() / 2772.f);
                    circle.setFill(Color.PURPLE);
                    mapContainer.getChildren().add(circle);
                }
                Text startText = new Text(pathStartNode.getShortName());
                Text endText = new Text(pathEndNode.getShortName());

                startText.setX(pathStartNode.getWireframePosition().getX() * mapContainer.getMaxWidth() / 5000.f);
                startText.setY(pathStartNode.getWireframePosition().getY() * mapContainer.getMaxHeight() / 3400.f);
                startText.setFont(Font.font("Verdana", 5));
                endText.setX(pathEndNode.getWireframePosition().getX() * mapContainer.getMaxWidth() / 5000.f);
                endText.setY(pathEndNode.getWireframePosition().getY() * mapContainer.getMaxHeight() / 3400.f);
                endText.setFont(Font.font("Verdana", 5));

                mapContainer.getChildren().addAll(startText, endText);
            }
        } else {
            System.out.println("Invalid nodes");
        }
    }


    // Popup upon help request

    @FXML
    void onHelpPopup() {
        switcher.popup(Screens.Help);
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

        clearNodes();
        reloadMap();

        pathEndNode = null;
        pathStartNode = null;
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
        StringBinding mapDim;
        if (MapSingleton.is2D) {
            mapDim = switcher.resFac.getStringBinding("2DMap");
            btnMapDimensions.setText(mapDim.get());
            MapSingleton.is2D = false;
            reloadMap();
        } else {
            mapDim = switcher.resFac.getStringBinding("3DMap");
            btnMapDimensions.setText(mapDim.get());
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
        String type = newNode_type.getSelectionModel().getSelectedItem();
        // TODO move into NodeBuilder
        int height;
        switch (floor) {
            case "01":
                height = 100;
                break;
            case "02":
                height = 200;
                break;
            case "03":
                height = 300;
                break;
            case "L2":
                height = -200;
                break;
            case "L1":
                height = -100;
                break;
            case "0G":
                height = 0;
                break;
            default:
                throw new AssertionError("Invalid floor level stored in Database: " + floor);
        }

        int typeCount = 0;
        if (!type.equals("ELEV")) {
            try {
                typeCount = map.getNodes()
                        .stream()
                        .filter(node -> node.getNodeType().equals(type))
                        .map(node -> node.getNodeID().substring(5, 8))
                        .map(Integer::parseInt)
                        .max(Integer::compare)
                        .get();
                typeCount++;
            } catch (Exception e) {
                typeCount = 0;
            }
        }

        Node newNode = new NewNodeBuilder()
                .setNodeType(type)
                .setNumNodeType(typeCount)
                .setFloor(floor)
                .setBuilding("New Building")    // TODO set building
                .setShortName(newNode_shortName.getText())
                .setLongName(newNode_shortName.getText()) // TODO get long name
                .setPosition(new Point3D(Double.parseDouble(newNode_x.getText()), Double.parseDouble(newNode_y.getText()), height))
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
        modifyNode.setPosition(new Point3D(Double.parseDouble(modNode_x.getText())
                , Double.parseDouble(modNode_y.getText())
                , modifyNode.getPosition().getZ()));
        modifyNode.setShortName(modNode_shortName.getText());
        modifyNode.setLongName(modNode_longName.getText());

        clearNodes();
        reloadMap();
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
            floor = "L2";
            MainTitle.setText("Brigham and Women's Hospital: Lower Level 2");
        } else if (e.getSource().equals(btnLower1Floor)) {
            floor = "L1";
            MainTitle.setText("Brigham and Women's Hospital: Lower Level 1");
        } else if (e.getSource().equals(btnGroundFloor)) {
            floor = "0G";
            MainTitle.setText("Brigham and Women's Hospital: Ground Floor");
        } else if (e.getSource().equals(btnFirstFloor)) {
            floor = "01";
            MainTitle.setText("Brigham and Women's Hospital: Level 1");
        } else if (e.getSource().equals(btnSecondFloor)) {
            floor = "02";
            MainTitle.setText("Brigham and Women's Hospital: Level 2");
        } else if (e.getSource().equals(btnThirdFloor)) {
            floor = "03";
            MainTitle.setText("Brigham and Women's Hospital: Level 3");
        }
        reloadMap();
    }

    private void reloadMap() {
        int index;
        if (floor.equals("L2")) {
            index = 0;
        } else if (floor.equals("L1")) {
            index = 1;
        } else if (floor.equals("0G")) {
            index = 2;
        } else if (floor.equals("01")) {
            index = 3;
        } else if (floor.equals("02")) {
            index = 4;
        } else {
            index = 5;
        }

        if (!MapSingleton.is2D) {
            ivMap.setImage(maps3D[index]);
            clearNodes();
            draw3DNodes(floor);
        } else {
            ivMap.setImage(maps2D[index]);
            clearNodes();
            draw2DNodes(floor);
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
        nodeCircleHashMap.clear();
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
            nodeCircleHashMap.put(node, circle);
        }

        mapContainer.getChildren().add(newNodeCircle);
        newNodeCircle.setVisible(false);
    }

    private void draw3DNodes(String newLevel) {


        System.out.println("----------------------------------");
        double transX = tf_transx.getValue();
        System.out.println("transX = " + transX);
        double transY = tf_transy.getValue();
        System.out.println("transY = " + transY);
        double transXp = tf_transxp.getValue() / 1000.f;
        System.out.println("transXp = " + transXp / 1000.f);
        double transYp = tf_transyp.getValue() / 1000.f;
        System.out.println("transYp = " + transYp / 1000.f);
        double scaleX = tf_scalex.getValue()  / 1000.f;
        System.out.println("scaleX = " + scaleX / 1000.f);
        double scaleY = tf_scaley.getValue()  / 1000.f;
        System.out.println("scaleY = " + scaleY / 1000.f);
        double rotateAngle = tf_rotate.getValue() / 1000.f;
        System.out.println("rotateAngle = " + rotateAngle / 1000.f);
        System.out.println("----------------------------------");


        double a = scaleX * cos(rotateAngle);
        double b = -scaleY * sin(rotateAngle) * transXp;
        double c = transX * scaleX * cos(rotateAngle) - transY * scaleY * sin(rotateAngle);
        double d = scaleX * sin(rotateAngle) * transYp;
        double e = scaleY * cos(rotateAngle);
        double f = transX * scaleX * sin(rotateAngle) + transY * scaleY * cos(rotateAngle);




        nodeCircleHashMap.clear();
        map = MapSingleton.getInstance().getMap();
        /*

        for (Edge edge : map.getEdges(edge -> edge.getNode2().getFloor().equals(newLevel))) {
            Line line = new Line();
            line.setEndX(edge.getNode1().getPosition().getX() * mapContainer.getMaxWidth() / 5000.f);
            line.setEndY(edge.getNode1().getPosition().getY() * mapContainer.getMaxHeight() / 3400.f);
            line.setStartX(edge.getNode2().getPosition().getX() * mapContainer.getMaxWidth() / 5000.f);
            line.setStartY(edge.getNode2().getPosition().getY() * mapContainer.getMaxHeight() / 3400.f);
            mapContainer.getChildren().add(line);
        }
        */


        for (Node node : map.getNodes(node -> node.getFloor().equals(newLevel))) {
            Circle circle = new Circle(1.5, Color.RED);
            Point2D wireframePosition = new Point2D(a*node.getPosition().getX() + b*node.getPosition().getY() + c,
                    d * node.getPosition().getX() + e * node.getPosition().getY() + f);

            circle.setCenterX(wireframePosition.getX() * mapContainer.getMaxWidth() / 5000.f);
            circle.setCenterY(wireframePosition.getY() * mapContainer.getMaxHeight() / 2772.f);
            mapContainer.getChildren().add(circle);
            nodeCircleHashMap.put(node, circle);
        }

        mapContainer.getChildren().add(newNodeCircle);
        newNodeCircle.setVisible(false);
    }

    private void clearNodes() {
        mapContainer.getChildren().clear();
        mapContainer.getChildren().add(ivMap);
    }
}
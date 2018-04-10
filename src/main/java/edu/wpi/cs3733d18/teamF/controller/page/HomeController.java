package edu.wpi.cs3733d18.teamF.controller.page;

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import edu.wpi.cs3733d18.teamF.ImageCacheSingleton;
import edu.wpi.cs3733d18.teamF.Map;
import edu.wpi.cs3733d18.teamF.MapSingleton;
import edu.wpi.cs3733d18.teamF.controller.*;
import edu.wpi.cs3733d18.teamF.db.DatabaseHandler;
import edu.wpi.cs3733d18.teamF.db.DatabaseSingleton;
import edu.wpi.cs3733d18.teamF.gfx.PaneMapController;
import edu.wpi.cs3733d18.teamF.gfx.PaneVoiceController;
import edu.wpi.cs3733d18.teamF.gfx.impl.UglyMapDrawer;
import edu.wpi.cs3733d18.teamF.graph.NewNodeBuilder;
import edu.wpi.cs3733d18.teamF.graph.Node;
import edu.wpi.cs3733d18.teamF.graph.NodeBuilder;
import edu.wpi.cs3733d18.teamF.voice.VoiceLauncher;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.animation.*;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;
import org.apache.commons.math3.analysis.function.Floor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HomeController implements SwitchableController, Observer {

    private static final int MIN_PIXELS = 200;
    private static Image maps2D[] = ImageCacheSingleton.maps2D;
    private static Image maps3D[] = ImageCacheSingleton.maps3D;
    final Boolean canSayCommand[] = {false};
    private final ObservableList<String> patientRooms = FXCollections.observableArrayList(
            "Patient Room 1",
            "Patient Room 2",
            "Patient Room 3");
    private final ObservableList<String> bathrooms = FXCollections.observableArrayList(
            "Bathroom 1",
            "Bathroom 2");
    private final ObservableList<String> all = FXCollections.observableArrayList();
    ConcurrentLinkedQueue<String> commands = new ConcurrentLinkedQueue<>();
    PaneVoiceController paneVoiceController;
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

    @FXML
    private VBox vbxMenu;
    @FXML
    private VBox vbxLocation;
    @FXML
    private VBox vbxDirections;
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
    Timeline commandExecuter = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            String command = commands.poll();
            if (command != null) {
                if (command.equals("HOSPITAL KIOSK")) {
                    paneVoiceController.setVisibility(true);
                    canSayCommand[0] = true;
                    new Timer(true).schedule(new TimerTask() {
                        @Override
                        public void run() {
                            canSayCommand[0] = false;
                        }
                    }, 5000);

                } else {
                    if (!canSayCommand[0]) {
                        return;
                    }
                    canSayCommand[0] = false;
                    paneVoiceController.setVisibility(false);

                    switch (command) {
                        case "HELP":
                            onHelpPopup();
                            break;
                        case "ADMIN LOGIN":
                            //onLoginPopup();
                            //TODO
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }));
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
    @FXML
    private Pane voicePane;

    // kiosk location
    private Point2D startLocation = new Point2D(1875.0, 1025.0);

    // menu in bottom left corner
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXDrawer adminDrawer;
    @FXML
    private JFXDrawer staffDrawer;
    @FXML
    private VBox adminBox;
    @FXML
    private VBox staffBox;

    // login elements
    @FXML
    private JFXDrawer loginDrawer;
    @FXML
    private VBox loginBox;
    @FXML
    private VBox logoutBox;
    @FXML
    private JFXButton loginBtn;
    @FXML
    private JFXButton logoutBtn;
    @FXML
    private JFXTextField loginUsername;
    @FXML
    private JFXPasswordField loginPassword;
    @FXML
    private JFXNodesList floorNode;
    @FXML
    private JFXButton floorBtn;
    @FXML
    private JFXButton l2;
    @FXML
    private JFXButton l1;
    @FXML
    private JFXButton groundFloor;
    @FXML
    private JFXButton floor1;
    @FXML
    private JFXButton floor2;
    @FXML
    private JFXButton floor3;
    @FXML
    private FontAwesomeIconView loginCancel;
    @FXML
    private FontAwesomeIconView logoutCancel;

    // searching for a location
    @FXML
    private JFXTextField sourceLocation;
    @FXML
    private JFXListView searchList;

    @Override
    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;
        VoiceLauncher.getInstance().addObserver(this);

        paneVoiceController = new PaneVoiceController(voicePane);

        commandExecuter.setCycleCount(Timeline.INDEFINITE);
        commandExecuter.play();

        // initialize map and map drawer
        map = MapSingleton.getInstance().getMap();
        mapDrawController = new PaneMapController(mapContainer, map, new UglyMapDrawer());

        /*
        //to make initial admin with secure password
        txtUser.setText(PermissionSingleton.getInstance().getCurrUser());
        if (!PermissionSingleton.getInstance().getCurrUser().equals("Guest")) {
            loginPopup.setText("Logout");
        }
        */

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

        if (PermissionSingleton.getInstance().isAdmin()) {
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

            mapDrawController.showPath(map.getPath
                    (map.findNodeClosestTo(startLocation.getX(), startLocation.getY(), map.is2D())
                            , map.findNodeClosestTo(mapPos.getX(), mapPos.getY(), map.is2D())));

            // if editing maps
            HashSet<Node> nodes = new HashSet<>();
            if (PermissionSingleton.getInstance().isAdmin()) {
                if (map.is2D()) {
                    nodes = map.getNodes(node -> new Point2D(node.getPosition().getX()
                            , node.getPosition().getY()).distance(mapPos) < 8);
                } else {
                    nodes = map.getNodes(node -> new Point2D(node.getWireframePosition().getX()
                            , node.getWireframePosition().getY()).distance(mapPos) < 8);
                }
            }
            // not editing map
            else {
                nodes.add(map.findNodeClosestTo(mapPos.getX(), mapPos.getY(), map.is2D()));
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

        /*


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

        */

        floorBtn.setText("2");
        l2.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            map.setFloor("L2");
            floorBtn.setText("L2");
            reloadMap();
        });
        l1.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            map.setFloor("L1");
            floorBtn.setText("L1");
            reloadMap();
        });
        groundFloor.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            map.setFloor("0G");
            floorBtn.setText("G");
            reloadMap();
        });
        floor1.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            map.setFloor("01");
            floorBtn.setText("1");
            reloadMap();
        });
        floor2.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            map.setFloor("02");
            floorBtn.setText("2");
            reloadMap();
        });
        floor3.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            map.setFloor("03");
            floorBtn.setText("3");
            reloadMap();
        });


        floorNode.addAnimatedNode(floorBtn);
        floorNode.addAnimatedNode(l2);
        floorNode.addAnimatedNode(l1);
        floorNode.addAnimatedNode(groundFloor);
        floorNode.addAnimatedNode(floor1);
        floorNode.addAnimatedNode(floor2);
        floorNode.addAnimatedNode(floor3);



        // set the hamburger menu on bottom left accordingly
        if (PermissionSingleton.getInstance().getUserPrivilege().equals("Guest")) {
            setGuestMenu();
            loginBtn.setText("Login");
        } else if(PermissionSingleton.getInstance().getUserPrivilege().equals("Staff")) {
            setStaffMenu();
            loginBtn.setText(PermissionSingleton.getInstance().getCurrUser());
        } else if (PermissionSingleton.getInstance().isAdmin()) {
            setAdminMenu();
            loginBtn.setText(PermissionSingleton.getInstance().getCurrUser());
        } else {
            setGuestMenu();
            loginBtn.setText(PermissionSingleton.getInstance().getCurrUser());
        }


        setHamburgerEvent();

        // login
        loginBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (PermissionSingleton.getInstance().getUserPrivilege().equals("Guest")) {
                loginBox.setVisible(true);
                loginDrawer.setSidePane(loginBox);
                loginDrawer.setOverLayVisible(false);
                loginBtn.setText("Login");
            } else {
                logoutBox.setVisible(true);
                loginDrawer.setSidePane(logoutBox);
                loginDrawer.setOverLayVisible(false);
                loginBtn.setText(PermissionSingleton.getInstance().getCurrUser());
            }

            if (loginDrawer.isShown()) {
                // login in the user and close the drawer
                if (PermissionSingleton.getInstance().login(loginUsername.getText(), loginPassword.getText())) {
                    loginBtn.setText(PermissionSingleton.getInstance().getCurrUser());

                    if (PermissionSingleton.getInstance().isAdmin()) {
                        setAdminMenu();
                        mapDrawController.showNodes();
                        mapDrawController.showEdges();
                    } else if (PermissionSingleton.getInstance().getUserPrivilege().equals("Staff")) {
                        setStaffMenu();
                    }
                    loginDrawer.close();
                    //loginUsername.setFocusColor(Color.rgb(64, 89, 169));
                    loginUsername.setText("");
                    loginPassword.setText("");

                } else {
                    //loginPassword.setFocusColor(Color.rgb(255, 0, 0));
                    loginPassword.setText("");
                    TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.1),loginPassword);
                    translateTransition.setFromX(7);
                    translateTransition.setToX(-7);
                    translateTransition.setCycleCount(4);
                    translateTransition.setAutoReverse(true);
                    translateTransition.setOnFinished((translateEvent) -> {
                        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.05),loginPassword);
                        tt.setFromX(7);
                        tt.setToX(0);
                        tt.setCycleCount(0);
                        tt.play();
                    });
                    translateTransition.play();
                }

            } else {
                loginDrawer.open();
            }
        });

        loginCancel.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (loginDrawer.isShown()) {
                loginDrawer.close();
            }
        });

        logoutCancel.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (loginDrawer.isShown()) {
                loginDrawer.close();
            }
        });

        // logout
        logoutBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            PermissionSingleton.getInstance().logout();
            loginDrawer.close();
            staffDrawer.close();
            adminDrawer.close();
            adminBox.setVisible(false);
            staffBox.setVisible(false);
            hamburger.setVisible(false);
            mapDrawController.unshowNodes();
            mapDrawController.unshowEdges();
            loginBtn.setText("Login");
        });

        sourceLocation.setOnKeyTyped((KeyEvent e) -> {
            String input = sourceLocation.getText();
            input = input.concat("" + e.getCharacter());

            if (input.length() > 0) {
                String sql = "SELECT longName FROM Node";
                ResultSet resultSet = DatabaseSingleton.getInstance().getDbHandler().runQuery(sql);
                ArrayList<String> autoCompleteStrings = new ArrayList<>();

                try {
                    while (resultSet.next()) {
                        String longName = resultSet.getString(1);
                        if (longName.toLowerCase().contains(input.toLowerCase())) {
                            autoCompleteStrings.add(longName);
                        }
                    }
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
                try {
                    if (autoCompleteStrings.size() > 0) {
                        ObservableList<String> list =FXCollections.observableArrayList (autoCompleteStrings);
                        searchList.setItems(list);
                        searchList.setVisible(true);
                    } else {
                        searchList.setVisible(false);
                    }
                } catch (Exception anyE) {
                    anyE.printStackTrace();
                }
            } else {
                //sourceList.setVisible(false);
                searchList.setVisible(false);
            }
        });

    }


    private void setHamburgerEvent() {
        HamburgerBasicCloseTransition arrowBasicTransition = new HamburgerBasicCloseTransition(hamburger);
        arrowBasicTransition.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            arrowBasicTransition.setRate(arrowBasicTransition.getRate() * -1);
            arrowBasicTransition.play();

            if (staffDrawer.isShown()) {
                staffDrawer.close();
            } else if (PermissionSingleton.getInstance().getUserPrivilege().equals("Staff")) {
                staffDrawer.open();
            }

            if (adminDrawer.isShown()) {
                adminDrawer.close();
            } else if (PermissionSingleton.getInstance().isAdmin()){
                adminDrawer.open();
            }
        });
    }

    private void setGuestMenu() {
        adminBox.setVisible(false);
        staffBox.setVisible(false);
        hamburger.setVisible(false);
    }

    private void setStaffMenu() {
        hamburger.setVisible(true);
        adminBox.setVisible(false);
        staffBox.setVisible(true);
        staffDrawer.setSidePane(staffBox);
        staffDrawer.setOverLayVisible(false);
    }

    private void setAdminMenu() {
        hamburger.setVisible(true);
        adminBox.setVisible(true);
        staffBox.setVisible(false);
        adminDrawer.setSidePane(adminBox);
        adminDrawer.setOverLayVisible(false);
    }


    @FXML
    void setSourceSearch() {
        sourceLocation.setText(searchList.getSelectionModel().getSelectedItem().toString());
        searchList.setVisible(false);
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

    @FXML
    void onGetDirections() {
        vbxMenu.setVisible(false);
        vbxDirections.setVisible(true);
    }

    public void onFindLocation() {
        vbxMenu.setVisible(false);
        vbxLocation.setVisible(true);
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

        if (arg instanceof String) {
            String cmd = (String) arg;
            commands.add(cmd);
        }
    }
}
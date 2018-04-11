package edu.wpi.cs3733d18.teamF.controller.page;

import com.github.fedy2.weather.YahooWeatherService;
import com.github.fedy2.weather.data.Channel;
import com.github.fedy2.weather.data.unit.DegreeUnit;
import com.jfoenix.controls.*;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import edu.wpi.cs3733d18.teamF.ImageCacheSingleton;
import edu.wpi.cs3733d18.teamF.Map;
import edu.wpi.cs3733d18.teamF.MapSingleton;
import edu.wpi.cs3733d18.teamF.controller.*;
import edu.wpi.cs3733d18.teamF.db.DatabaseSingleton;
import edu.wpi.cs3733d18.teamF.gfx.PaneMapController;
import edu.wpi.cs3733d18.teamF.gfx.PaneVoiceController;
import edu.wpi.cs3733d18.teamF.gfx.impl.UglyMapDrawer;
import edu.wpi.cs3733d18.teamF.graph.NewNodeBuilder;
import edu.wpi.cs3733d18.teamF.graph.Node;
import edu.wpi.cs3733d18.teamF.graph.NodeBuilder;
import edu.wpi.cs3733d18.teamF.sr.ServiceRequest;
import edu.wpi.cs3733d18.teamF.sr.ServiceRequestSingleton;
import edu.wpi.cs3733d18.teamF.voice.VoiceLauncher;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HomeController implements SwitchableController, Observer {

    private static final int MIN_PIXELS = 200;
    private static Image maps2D[] = ImageCacheSingleton.maps2D;
    private static Image maps3D[] = ImageCacheSingleton.maps3D;
    private final Boolean canSayCommand[] = {false};
    private final ObservableList<String> priority = FXCollections.observableArrayList(
            "0",
            "1",
            "2",
            "3",
            "4",
            "5");
    private final ObservableList<String> status = FXCollections.observableArrayList(
            "Incomplete",
            "In Progress",
            "Complete");
    private final ObservableList<String> type = FXCollections.observableArrayList(
            "Language Interpreter",
            "Religious Services");
    @FXML
    public ComboBox filterType;
    @FXML
    public ComboBox availableTypes;
    @FXML
    public TableView<ServiceRequest> searchResultTable;
    @FXML
    public TableColumn btnsCol;
    @FXML
    public TableColumn<ServiceRequest, Integer> idNumberCol;
    @FXML
    public TableColumn<ServiceRequest, String> requestTypeCol;
    @FXML
    public TableColumn<ServiceRequest, String> firstNameCol;
    @FXML
    public TableColumn<ServiceRequest, String> lastNameCol;
    @FXML
    public TableColumn<ServiceRequest, String> destinationCol;
    @FXML
    public TableColumn<ServiceRequest, Integer> requestPriorityCol;
    @FXML
    public TableColumn<ServiceRequest, String> theStatusCol;
    @FXML
    public Label typeLabel;
    @FXML
    public Label idLabel;
    @FXML
    public Label firstNameLabel;
    @FXML
    public Label lastNameLabel;
    @FXML
    public Label locationLabel;
    @FXML
    public Label statusLabel;
    @FXML
    public TextArea instructionsTextArea;
    @FXML
    public ComboBox statusBox;
    @FXML
    public Label completedByLabel;
    @FXML
    public Label usernameLabel;
    private ConcurrentLinkedQueue<String> commands = new ConcurrentLinkedQueue<>();
    private PaneVoiceController paneVoiceController;
    private Voice voice;
    private VoiceManager voiceManager = VoiceManager.getInstance();
    @FXML
    public TableColumn chooseCol;
    @FXML
    public TableColumn<User, String> usernameCol;
    @FXML
    public TableColumn<User, String> firstNameUserCol;
    @FXML
    public TableColumn<User, String> lastNameUserCol;
    @FXML
    public TableColumn<User, String> privilegeCol;
    @FXML
    public TableColumn<User, String> occupationCol;
    ////////////////////////////////////////////////////
    //                                                //
    //           Search Service Request Variables     //
    //                                                //
    ////////////////////////////////////////////////////
    private String searchType;
    private String filter;
    private ServiceRequest serviceRequestPopUp;
    private boolean statusChange;
    private String newStatus;
    private boolean nodesShown = false;
    ////////////////////////////////////////////////////
    //                                                //
    //           Edit User Variables                  //
    //                                                //
    ////////////////////////////////////////////////////
    @FXML
    private AnchorPane editUserPane;
    @FXML
    private JFXTextField userTextField;
    @FXML
    private TableView<User> searchUserResultTable;
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

    /*@FXML
    private VBox vbxMenu;
    @FXML
    private VBox vbxLocation;
    @FXML
    private VBox vbxDirections;*/
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
    private VBox adminBox;
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
    private JFXButton btn2D;
    @FXML
    private JFXButton btn3D;
    @FXML
    private FontAwesomeIconView loginCancel;
    @FXML
    private HBox searchBar;
    @FXML
    private Text MainTitle;
    ////////////////////////////////////////
    //                                    //
    //       Edit Service Request         //
    //                                    //
    ////////////////////////////////////////
    @FXML
    private FontAwesomeIconView logoutCancel;
    // searching for a location
    @FXML
    private JFXTextField searchLocation;
    @FXML
    private JFXTextField sourceLocation;
    @FXML
    private JFXTextField destinationLocation;
    @FXML
    private JFXListView searchList;
    @FXML
    private JFXListView directionsList;
    private boolean sourceLocationActive = false;
    @FXML
    private VBox directionsBox;
    @FXML
    private FontAwesomeIconView directionsArrow;
    @FXML
    private FontAwesomeIconView cancelDirections;
    @FXML
    private JFXDrawer directionsDrawer;
    @FXML
    private JFXHamburger hamburgerD;
    @FXML
    private FontAwesomeIconView cancelMenu;
    @FXML
    private AnchorPane searchPane;
    private ObservableList<ServiceRequest> listRequests;
    ////////////////////////////////////////
    //                                    //
    //           Help Screen              //
    //                                    //
    ////////////////////////////////////////
    @FXML
    private Pane helpPane;
    @FXML
    private GridPane userInstructions;
    @FXML
    private GridPane adminInstructions;
    Timeline commandExecuter = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            String command = commands.poll();
            if (command != null) {
                if (command.equals("HEY KIOSK") || command.equals("HELLO KIOSK")) {
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

                    if (command.equals("HELP")) {
                        onHelpPopup();
                        voice.speak("Here is the help menu");
                    } else if (command.contains("DIRECTIONS")) {
                        if (command.contains("BATHROOM")) {
                            mapDrawController.showPath(map.getPath
                                    (map.findNodeClosestTo(startLocation.getX(), startLocation.getY(), true)
                                            , map.findNodeClosestTo(startLocation.getX(), startLocation.getY(), true, node -> node.getNodeType().equals("REST"))));
                            voice.speak("Here is the route to the nearest bathroom");
                        } else if (command.contains("EXIT")) {
                            mapDrawController.showPath(map.getPath
                                    (map.findNodeClosestTo(startLocation.getX(), startLocation.getY(), true)
                                            , map.findNodeClosestTo(startLocation.getX(), startLocation.getY(), true, node -> node.getNodeType().equals("EXIT"))));
                            voice.speak("Here is the route to the nearest exit");
                        } else if (command.contains("NEUROSCIENCE")) {
                            mapDrawController.showPath(map.getPath
                                    (map.findNodeClosestTo(startLocation.getX(), startLocation.getY(), true)
                                            , map.findNodeClosestTo(startLocation.getX(), startLocation.getY(), true, node -> node.getLongName().contains("Neuroscience"))));
                            voice.speak("Here is the route to neuroscience");
                        } else if (command.contains("ORTHOPEDICS") || command.contains("RHEMUTOLOGY")) {
                            mapDrawController.showPath(map.getPath
                                    (map.findNodeClosestTo(startLocation.getX(), startLocation.getY(), true)
                                            , map.findNodeClosestTo(startLocation.getX(), startLocation.getY(), true, node -> node.getLongName().contains("Orthopedics"))));
                            voice.speak("Here is the route to Orthopedics and Rhemutology");
                        } else if (command.contains("PARKING") && command.contains("GARAGE")) {
                            mapDrawController.showPath(map.getPath
                                    (map.findNodeClosestTo(startLocation.getX(), startLocation.getY(), true)
                                            , map.findNodeClosestTo(startLocation.getX(), startLocation.getY(), true, node -> node.getLongName().contains("Parking") &&
                                                    node.getLongName().contains("Garage"))));
                            voice.speak("Here is the route to the parking garage");
                        } else if (command.contains("ELEVATOR")) {
                            mapDrawController.showPath(map.getPath
                                    (map.findNodeClosestTo(startLocation.getX(), startLocation.getY(), true)
                                            , map.findNodeClosestTo(startLocation.getX(), startLocation.getY(), true, node -> node.getNodeType().equals("ELEV"))));
                            voice.speak("Here is the route to the nearest elevator");
                        } else if (command.contains("DENTIST") || command.contains("DENTISTRY") || command.contains("ORAL")) {
                            mapDrawController.showPath(map.getPath
                                    (map.findNodeClosestTo(startLocation.getX(), startLocation.getY(), true)
                                            , map.findNodeClosestTo(startLocation.getX(), startLocation.getY(), true, node -> node.getLongName().contains("Dentistry"))));
                            voice.speak("Here is the route to Dentistry and Oral Medicine");
                        } else if (command.contains("PLASTIC SURGERY")) {
                            mapDrawController.showPath(map.getPath
                                    (map.findNodeClosestTo(startLocation.getX(), startLocation.getY(), true)
                                            , map.findNodeClosestTo(startLocation.getX(), startLocation.getY(), true, node -> node.getLongName().contains("Plastic Surgery"))));
                            voice.speak("Here is the route to Plastic Surgery");
                        } else if (command.contains("RADIOLOGY")) {
                            mapDrawController.showPath(map.getPath
                                    (map.findNodeClosestTo(startLocation.getX(), startLocation.getY(), true)
                                            , map.findNodeClosestTo(startLocation.getX(), startLocation.getY(), true, node -> node.getLongName().contains("Radiation"))));
                            voice.speak("Here is the route to Radiology");
                        } else if (command.contains("NUCLEAR")) {
                            mapDrawController.showPath(map.getPath
                                    (map.findNodeClosestTo(startLocation.getX(), startLocation.getY(), true)
                                            , map.findNodeClosestTo(startLocation.getX(), startLocation.getY(), true, node -> node.getLongName().contains("Nuclear"))));
                            voice.speak("Here is the route to Nuclear Medicine");
                        }
                    } else if (command.contains("STAIRS") && command.contains("DISABLE")) {
                        map.disableStairs();
                        voice.speak("Stairs are now disabled for path finding");
                    } else if (command.contains("STAIRS") && command.contains("ENABLE")) {
                        map.enableStairs();
                        voice.speak("Stairs are now enabled for path finding");
                    } else if (command.contains("ELEVATOR") && command.contains("DISABLE")) {
                        map.disableElevators();
                        voice.speak("Elevators are now disabled for path finding");
                    } else if (command.contains("ELEVATOR") && command.contains("ENABLE")) {
                        map.enableElevators();
                        voice.speak("Elevators are now enabled for path finding");
                    } else if (command.contains("WEATHER") && command.contains("TODAY")) {
                        YahooWeatherService service = null;
                        try {
                            service = new YahooWeatherService();
                        } catch (JAXBException e) {
                            e.printStackTrace();
                        }
                        Channel channel = null;
                        try {
                            channel = service.getForecast("2523945", DegreeUnit.FAHRENHEIT);
                        } catch (JAXBException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        voice.speak(String.format("The temperature is %d degrees fahrenheit", channel.getItem().getCondition().getTemp()));
                        voice.speak(channel.getAtmosphere().toString());
                    }
                }
            }
        }
    }));
    @FXML
    private JFXTextField usernameSearch;
    @FXML
    private JFXListView usernameList;


    ////////////////////////////////////////
    //                                    //
    //           Edit Nodes               //
    //                                    //
    ////////////////////////////////////////
    @FXML
    private AnchorPane editRequestPane;

    @Override
    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;
        VoiceLauncher.getInstance().addObserver(this);

        voice = voiceManager.getVoice("kevin16");
        voice.allocate();

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

        // set up new node panel
        newNode_type.getItems().addAll(NodeBuilder.getNodeTypes());
        newNode_type.getSelectionModel().selectFirst();


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
                    , node.getPosition().getY()).distance(mapPos) < 8 && node.getFloor().equals(map.getFloor())
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

            if (!PermissionSingleton.getInstance().isAdmin()) {
                mapDrawController.showPath(map.getPath
                        (map.findNodeClosestTo(startLocation.getX(), startLocation.getY(), true)
                                , map.findNodeClosestTo(mapPos.getX(), mapPos.getY(), true)));
            }

            // if editing maps
            HashSet<Node> nodes = new HashSet<>();
            if (PermissionSingleton.getInstance().isAdmin()) {
                if (map.is2D()) {
                    nodes = map.getNodes(node -> new Point2D(node.getPosition().getX()
                            , node.getPosition().getY()).distance(mapPos) < 8 && node.getFloor().equals(map.getFloor()));
                } else {
                    nodes = map.getNodes(node -> new Point2D(node.getWireframePosition().getX()
                            , node.getWireframePosition().getY()).distance(mapPos) < 8 && node.getFloor().equals(map.getFloor()));
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
            MainTitle.setText("Brigham and Women's Hospital: Lower Level 2");
            reloadMap();
        });
        l1.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            map.setFloor("L1");
            floorBtn.setText("L1");
            MainTitle.setText("Brigham and Women's Hospital: Lower Level 1");
            reloadMap();
        });
        groundFloor.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            map.setFloor("0G");
            floorBtn.setText("G");
            MainTitle.setText("Brigham and Women's Hospital: Ground Floor");
            reloadMap();
        });
        floor1.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            map.setFloor("01");
            floorBtn.setText("1");
            MainTitle.setText("Brigham and Women's Hospital: Level 1");
            reloadMap();
        });
        floor2.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            map.setFloor("02");
            floorBtn.setText("2");
            MainTitle.setText("Brigham and Women's Hospital: Level 2");
            reloadMap();
        });
        floor3.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            map.setFloor("03");
            floorBtn.setText("3");
            MainTitle.setText("Brigham and Women's Hospital: Level 3");
            reloadMap();
        });


        floorNode.addAnimatedNode(floorBtn);
        floorNode.addAnimatedNode(l2);
        floorNode.addAnimatedNode(l1);
        floorNode.addAnimatedNode(groundFloor);
        floorNode.addAnimatedNode(floor1);
        floorNode.addAnimatedNode(floor2);
        floorNode.addAnimatedNode(floor3);


        // set the hamburger menu on top left accordingly
        if (PermissionSingleton.getInstance().getUserPrivilege().equals("Guest")) {
            setGuestMenu();
            loginBtn.setText("Login");
        } else if (PermissionSingleton.getInstance().getUserPrivilege().equals("Staff")) {
            setAdminMenu();
            loginBtn.setText(PermissionSingleton.getInstance().getCurrUser());
        } else if (PermissionSingleton.getInstance().isAdmin()) {
            setAdminMenu();
            loginBtn.setText(PermissionSingleton.getInstance().getCurrUser());
        } else {
            setGuestMenu();
            loginBtn.setText(PermissionSingleton.getInstance().getCurrUser());
        }


        directionsDrawer.setSidePane(directionsBox);
        adminDrawer.setSidePane(adminBox);

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
                    } else if (PermissionSingleton.getInstance().getUserPrivilege().equals("Staff")) {
                        setAdminMenu();
                    } else {
                        setGuestMenu();
                    }
                    loginDrawer.close();
                    loginUsername.setText("");
                    loginPassword.setText("");

                } else {
                    loginPassword.setText("");
                    shakePasswordField(loginPassword);
                }

            } else {
                loginDrawer.open();
            }
        });

        loginCancel.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            loginDrawer.close();
        });

        logoutCancel.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            loginDrawer.close();
        });


        searchLocation.setOnKeyTyped((KeyEvent e) -> {
            String input = searchLocation.getText();
            input = input.concat("" + e.getCharacter());
            autoComplete(input, searchList, "Node", "longName");
        });

        sourceLocation.setOnKeyTyped((KeyEvent e) -> {
            String input = sourceLocation.getText();
            input = input.concat("" + e.getCharacter());
            autoComplete(input, directionsList, "Node", "longName");
            sourceLocationActive = true;
        });

        destinationLocation.setOnKeyTyped((KeyEvent e) -> {
            String input = destinationLocation.getText();
            input = input.concat("" + e.getCharacter());
            autoComplete(input, directionsList, "Node", "longName");
            sourceLocationActive = false;
        });

        usernameSearch.setOnKeyTyped((KeyEvent e) -> {
            String input = usernameSearch.getText();
            input = input.concat("" + e.getCharacter());
            autoComplete(input, usernameList, "HUser", "username");
        });

        userTextField.setOnKeyTyped((KeyEvent e) -> {
            String input = userTextField.getText();
            input = input.concat("" + e.getCharacter());
            ArrayList<User> list = autoCompleteUserSearch(input);
            displayInUserTable(list);
        });

    }

    // will filter the given ListView for the given input String
    private void autoComplete(String input, ListView listView, String table, String field) {
        if (input.length() > 0) {
            String sql = "SELECT " + field + " FROM " + table;
            ResultSet resultSet = DatabaseSingleton.getInstance().getDbHandler().runQuery(sql);
            ArrayList<String> autoCompleteStrings = new ArrayList<>();

            try {
                while (resultSet.next()) {
                    String username = resultSet.getString(1);
                    if (username.toLowerCase().contains(input.toLowerCase())) {
                        autoCompleteStrings.add(username);
                    }
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            try {
                if (autoCompleteStrings.size() > 0) {
                    ObservableList<String> list = FXCollections.observableArrayList(autoCompleteStrings);
                    listView.setItems(list);
                    listView.setVisible(true);
                } else {
                    listView.setVisible(false);
                }
            } catch (Exception anyE) {
                anyE.printStackTrace();
            }
        } else {
            listView.setVisible(false);
        }
    }


    // will shake the password field back and forth
    private void shakePasswordField(JFXPasswordField passwordField) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.1), passwordField);
        translateTransition.setFromX(7);
        translateTransition.setToX(-7);
        translateTransition.setCycleCount(4);
        translateTransition.setAutoReverse(true);
        translateTransition.setOnFinished((translateEvent) -> {
            TranslateTransition tt = new TranslateTransition(Duration.seconds(0.05), passwordField);
            tt.setFromX(7);
            tt.setToX(0);
            tt.setCycleCount(0);
            tt.play();
        });
        translateTransition.play();
    }


    @FXML
    private void onHamburgerMenu() {
        if (adminDrawer.isHidden()) {
            adminDrawer.open();
            adminDrawer.toFront();
        }
    }

    @FXML
    private void onLogOutBtn() {
        PermissionSingleton.getInstance().logout();
        loginDrawer.close();
        adminDrawer.close();
        hamburger.setVisible(false);
        hamburgerD.setVisible(false);
        mapDrawController.unshowNodes();
        mapDrawController.unshowEdges();
        loginBtn.setText("Login");
    }

    @FXML
    private void onArrowEvent() {
        if (directionsDrawer.isHidden()) {
            directionsDrawer.open();
            directionsDrawer.toFront();
            sourceLocation.setText(searchLocation.getText());
        }

    }

    @FXML
    private void onCancelDirectionsEvent() {
        if (directionsDrawer.isShown()) {
            directionsDrawer.close();
            directionsDrawer.toBack();
            searchLocation.setText(sourceLocation.getText());
        }


    }

    @FXML
    private void setCancelMenuEvent() {
        if (adminDrawer.isShown()) {
            adminDrawer.close();
            adminDrawer.toBack();
        }
    }

    private void setGuestMenu() {
        hamburger.setVisible(false);
        hamburgerD.setVisible(false);
    }

    private void setAdminMenu() {
        hamburger.setVisible(true);
        hamburgerD.setVisible(true);
    }


    @FXML
    void setSourceSearch() {
        searchLocation.setText(searchList.getSelectionModel().getSelectedItem().toString());
        searchList.setVisible(false);
    }

    @FXML
    void setDirectionLocation() {
        String selection = directionsList.getSelectionModel().getSelectedItem().toString();
        if (sourceLocationActive) {
            sourceLocation.setText(selection);
        } else {
            destinationLocation.setText(selection);
        }
        directionsList.setVisible(false);
    }

    // Popup upon help request

    @FXML
    void onHelpPopup() {
        if (PermissionSingleton.getInstance().isAdmin()) {
            userInstructions.setVisible(false);
            adminInstructions.setVisible(true);
        } else {
            adminInstructions.setVisible(false);
            userInstructions.setVisible(true);
        }
        helpPane.setVisible(true);
    }

    @FXML
    void onCancelClose() {
        helpPane.setVisible(false);
    }

    //////////////////////////////
    //                          //
    //        Edit User         //
    //                          //
    //////////////////////////////

    @FXML
    private void onCancelEditUser() {
        editUserPane.setVisible(false);
        try {
            searchUserResultTable.getItems().clear();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onNewUserEvent() {

    }


    private ArrayList<User> autoCompleteUserSearch(String input) {
        ArrayList<User> autoCompleteUser = new ArrayList<>();
        if (input.length() > 0) {
            String sql = "SELECT * FROM HUser";
            try {
                ResultSet resultSet = DatabaseSingleton.getInstance().getDbHandler().runQuery(sql);
                while (resultSet.next()) {

                    String username = resultSet.getString(1);
                    String firstname = resultSet.getString(3);
                    String lastname = resultSet.getString(4);
                    String privilege = resultSet.getString(5);
                    String occupation = resultSet.getString(6);
                    User temp = new User(username, "dummy", firstname, lastname, privilege, occupation);
                    autoCompleteUser.add(temp);

                }
                resultSet.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }

        }
        return autoCompleteUser;
    }

    private void displayInUserTable(ArrayList<User> users) {
        if (users.size() < 1) {
            //TODO: indicate to user that there are no results
            return;
        }

        ObservableList<User> listUsers = FXCollections.observableArrayList(users);

        searchUserResultTable.setEditable(false);

        usernameCol.setCellValueFactory(new PropertyValueFactory<User, String>("uname"));
        firstNameUserCol.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        lastNameUserCol.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        privilegeCol.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        occupationCol.setCellValueFactory(new PropertyValueFactory<User, String>("occupation"));
        chooseCol.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        Callback<TableColumn<User, String>, TableCell<User, String>> cellFactory
                = //
                new Callback<TableColumn<User, String>, TableCell<User, String>>() {
                    @Override
                    public TableCell call(final TableColumn<User, String> param) {
                        final TableCell<User, String> cell = new TableCell<User, String>() {

                            JFXButton btn = new JFXButton("Select");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        User e = getTableView().getItems().get(getIndex());
                                        onSelectUser(e);

                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        chooseCol.setCellFactory(cellFactory);

        searchUserResultTable.setItems(listUsers);
    }


    public void onSelectUser(User s) {
        /*
        serviceRequestPopUp = ServiceRequestSingleton.getInstance().getPopUpRequest();
        typeLabel.setText("Type: " + serviceRequestPopUp.getType());
        idLabel.setText("Service Request #" + serviceRequestPopUp.getId());
        firstNameLabel.setText("First Name: " + serviceRequestPopUp.getFirstName());
        lastNameLabel.setText("Last Name: " + serviceRequestPopUp.getLastName());
        locationLabel.setText(serviceRequestPopUp.getLocation());
        statusLabel.setText(serviceRequestPopUp.getStatus());
        instructionsTextArea.setText(serviceRequestPopUp.getDescription());
        statusChange = false;
        newStatus = "no";
        instructionsTextArea.setEditable(false);

        statusBox.getItems().addAll("Incomplete", "In Progress", "Complete");

        if (serviceRequestPopUp.getStatus().equals("Complete")) {
            completedByLabel.setVisible(true);
            usernameLabel.setVisible(true);
            usernameLabel.setText(serviceRequestPopUp.getCompletedBy());
        }
        */
        editUserPane.setVisible(false);
        //editUserInfoPane.setVisible(true);
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
        adminDrawer.close();
        adminDrawer.toBack();
    }


    // Adjusting the map

    @FXML
    void on2D() {
        map.setIs2D(true);
        reloadMap();
        btn2D.setButtonType(JFXButton.ButtonType.RAISED);
        btn2D.setStyle("-fx-background-color:  #f2f5f7");
        btn3D.setButtonType(JFXButton.ButtonType.FLAT);
        btn3D.setStyle("-fx-background-color:  #b6b8b9");
    }

    @FXML
    void on3D() {
        map.setIs2D(false);
        reloadMap();
        btn2D.setButtonType(JFXButton.ButtonType.FLAT);
        btn2D.setStyle("-fx-background-color:  #b6b8b9");
        btn3D.setButtonType(JFXButton.ButtonType.RAISED);
        btn3D.setStyle("-fx-background-color:  #f2f5f7");
    }

    @FXML
    private void onSearchServiceRequest() {
        filter = "none";
        searchType = "none";

        filterType.getItems().addAll("Priority", "Status", "Type");

        String lastSearch = ServiceRequestSingleton.getInstance().getLastSearch();
        String lastFilter = ServiceRequestSingleton.getInstance().getLastFilter();
        if (lastSearch != null && lastFilter != null) {
            searchType = lastSearch;
            filter = lastFilter;
        }
        onSearch();
        searchPane.setVisible(true);
        adminDrawer.close();
        adminDrawer.toBack();
    }


    // Add location on map

    @FXML
    void onAddLocationConfirm() {
        String type = newNode_type.getSelectionModel().getSelectedItem();
        // TODO move into NodeBuilder

        if (!(type.equals("ELEV") || type.equals("STAI"))) {
            Node newNode = new NewNodeBuilder()
                    .setNodeType(type)
                    .setNumNodeType(map)
                    .setFloor(map.getFloor())
                    .setBuilding("New Building") // TODO set building
                    .setShortName(newNode_shortName.getText())
                    .setLongName(newNode_shortName.getText())
                    .setPosition(new Point2D(Double.parseDouble(newNode_x.getText()), Double.parseDouble(newNode_y.getText())))
                    .build();
            map.createNode(newNode);

        } else {
            if (newNode_shortName.getText().length() != 1
                    || !Character.isLetter(newNode_shortName.getText().charAt(0))
                    || map.getNodes(node -> node.getNodeID().contains(type + "00" + newNode_shortName.getText().charAt(0) + map.getFloor())).size() == 1) {
                addLocationPopup.setVisible(false);
                newNode_shortName.setText("");
                newNodeCircle.setVisible(false);
            }

            Node newNode = new NewNodeBuilder()
                    .setNodeType(type)
                    .setLinkChar(newNode_shortName.getText().charAt(0))
                    .setFloor(map.getFloor())
                    .setBuilding("New Building") // TODO set building
                    .setShortName(newNode_shortName.getText())
                    .setLongName(newNode_shortName.getText())
                    .setPosition(new Point2D(Double.parseDouble(newNode_x.getText()), Double.parseDouble(newNode_y.getText())))
                    .build();
            map.createNode(newNode);
            for (Node node : map.getNodes(node -> node.getNodeID().contains(type + "00" + newNode_shortName.getText().charAt(0)))) {
                if (node == newNode) {
                    continue;
                }
                map.addEdge(node, newNode);
            }
        }

        addLocationPopup.setVisible(false);
        newNode_shortName.setText("");
        newNodeCircle.setVisible(false);
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
/*
    @FXML
    void onGetDirections() {
        vbxMenu.setVisible(false);
        vbxDirections.setVisible(true);
    }

    public void onFindLocation() {
        vbxMenu.setVisible(false);
        vbxLocation.setVisible(true);
    }*/

    @FXML
    public void onMapEditor() {
        if (nodesShown) {
            mapDrawController.unshowNodes();
            mapDrawController.unshowEdges();
            nodesShown = false;
        } else {
            mapDrawController.showNodes();
            mapDrawController.showEdges();
            nodesShown = true;
        }
        adminDrawer.close();
        adminDrawer.toBack();
    }

    @FXML
    public void onEditUsers() {
        adminDrawer.close();
        adminDrawer.toBack();
        editUserPane.setVisible(true);
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


    //////////////////////////////
    //                          //
    //         Directions       //
    //                          //
    //////////////////////////////


    @FXML
    void onSearchLocation() {
        if (!searchLocation.getText().equals("")) {
            HashSet<Node> n = map.getNodes(node -> node.getLongName().equals(searchLocation.getText()));
            if (n.size() < 1) {
                return;
            }

            Node selectedNode = n.iterator().next();
            mapDrawController.selectNode(selectedNode);
            map.setFloor(selectedNode.getFloor());
            reloadMap();
        }
    }


    @FXML
    void switchLocations() {
        String temp = sourceLocation.getText();
        sourceLocation.setText(destinationLocation.getText());
        destinationLocation.setText(temp);
    }


    @FXML
    void onNavigate() {
        sourceLocation.getText();
        destinationLocation.getText();
        if (!sourceLocation.getText().equals("") && !destinationLocation.getText().equals("")) {
            HashSet<Node> sourceNodeSet = map.getNodes(node -> node.getLongName().equals(sourceLocation.getText()));
            if (sourceNodeSet.size() < 1) {
                return;
            }
            HashSet<Node> destinationNodeSet = map.getNodes(node -> node.getLongName().equals(destinationLocation.getText()));
            if (destinationNodeSet.size() < 1) {
                return;
            }

            Node sourceNode = sourceNodeSet.iterator().next();
            Node destinationNode = destinationNodeSet.iterator().next();

            mapDrawController.showPath(map.getPath(sourceNode, destinationNode));

            map.setFloor(sourceNode.getFloor());
            reloadMap();
        }
    }


    ////////////////////////////////////////////////////
    //                                                //
    //           Search Service Request Functions     //
    //                                                //
    ////////////////////////////////////////////////////


    @FXML
    void onFilterType() {
        searchType = "none";
        filter = "none";
        try {
            if (filterType.getSelectionModel().getSelectedItem().equals("Priority")) {
                searchType = "Priority";
                availableTypes.setItems(priority);
                availableTypes.setVisible(true);
            } else if (filterType.getSelectionModel().getSelectedItem().equals("Status")) {
                searchType = "Status";
                availableTypes.setItems(status);
                availableTypes.setVisible(true);
            } else if (filterType.getSelectionModel().getSelectedItem().equals("Type")) {
                searchType = "Type";
                availableTypes.setItems(type);
                availableTypes.setVisible(true);
            }
        } catch (NullPointerException e) {
            searchType = "none";
        }
    }

    @FXML
    void onAvailableTypes() {
        try {
            filter = availableTypes.getSelectionModel().getSelectedItem().toString();
        } catch (NullPointerException e) {
            filter = "none";
        }
    }

    @FXML
    void onSearch() {
        ArrayList<ServiceRequest> requests = new ArrayList<>();
        try {
            if (filter.equalsIgnoreCase("none")) {
                ResultSet all = ServiceRequestSingleton.getInstance().getRequests();
                requests = ServiceRequestSingleton.getInstance().resultSetToServiceRequest(all);
                all.close();
            } else {
                switch (searchType) {
                    case "Priority":
                        ResultSet rp = ServiceRequestSingleton.getInstance().getRequestsOfPriority(Integer.parseInt(filter));
                        requests = ServiceRequestSingleton.getInstance().resultSetToServiceRequest(rp);
                        rp.close();
                        break;

                    case "Status":
                        ResultSet rs = ServiceRequestSingleton.getInstance().getRequestsOfStatus(filter);
                        requests = ServiceRequestSingleton.getInstance().resultSetToServiceRequest(rs);
                        rs.close();
                        break;

                    case "Type":
                        ResultSet rt = ServiceRequestSingleton.getInstance().getRequestsOfType(filter);
                        requests = ServiceRequestSingleton.getInstance().resultSetToServiceRequest(rt);
                        rt.close();
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            searchResultTable.getItems().clear();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        //TODO: put result of search into table
        if (requests.size() < 1) {
            //TODO: indicate to user that there are no results
            return;
        } else {
            listRequests = FXCollections.observableArrayList(requests);
        }

        searchResultTable.setEditable(false);

        idNumberCol.setCellValueFactory(new PropertyValueFactory<ServiceRequest, Integer>("id"));
        requestTypeCol.setCellValueFactory(new PropertyValueFactory<ServiceRequest, String>("type"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<ServiceRequest, String>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<ServiceRequest, String>("lastName"));
        destinationCol.setCellValueFactory(new PropertyValueFactory<ServiceRequest, String>("location"));
        requestPriorityCol.setCellValueFactory(new PropertyValueFactory<ServiceRequest, Integer>("priority"));
        theStatusCol.setCellValueFactory(new PropertyValueFactory<ServiceRequest, String>("status"));
        btnsCol.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        Callback<TableColumn<ServiceRequest, String>, TableCell<ServiceRequest, String>> cellFactory
                = //
                new Callback<TableColumn<ServiceRequest, String>, TableCell<ServiceRequest, String>>() {
                    @Override
                    public TableCell call(final TableColumn<ServiceRequest, String> param) {
                        final TableCell<ServiceRequest, String> cell = new TableCell<ServiceRequest, String>() {

                            JFXButton btn = new JFXButton("Select");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        ServiceRequest s = getTableView().getItems().get(getIndex());
                                        onSelect(s);
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        btnsCol.setCellFactory(cellFactory);

        searchResultTable.setItems(listRequests);

        ServiceRequestSingleton.getInstance().setSearch(filter, searchType);
    }


    public void onSelect(ServiceRequest s) {
        ServiceRequestSingleton.getInstance().setPopUpRequest(s);
        serviceRequestPopUp = ServiceRequestSingleton.getInstance().getPopUpRequest();
        typeLabel.setText("Type: " + serviceRequestPopUp.getType());
        idLabel.setText("Service Request #" + serviceRequestPopUp.getId());
        firstNameLabel.setText("First Name: " + serviceRequestPopUp.getFirstName());
        lastNameLabel.setText("Last Name: " + serviceRequestPopUp.getLastName());
        locationLabel.setText(serviceRequestPopUp.getLocation());
        statusLabel.setText(serviceRequestPopUp.getStatus());
        instructionsTextArea.setText(serviceRequestPopUp.getDescription());
        statusChange = false;
        newStatus = "no";
        instructionsTextArea.setEditable(false);

        statusBox.getItems().addAll("Incomplete", "In Progress", "Complete");

        if (serviceRequestPopUp.getStatus().equals("Complete")) {
            completedByLabel.setVisible(true);
            usernameLabel.setVisible(true);
            usernameLabel.setText(serviceRequestPopUp.getCompletedBy());
        }
        searchPane.setVisible(false);
        editRequestPane.setVisible(true);
    }

    @FXML
    void onClear() {
        availableTypes.setVisible(false);
        availableTypes.valueProperty().set(null);
        filterType.valueProperty().set(null);
        searchType = "none";
        filter = "none";
        try {
            searchResultTable.getItems().clear();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        ServiceRequestSingleton.getInstance().setSearchNull();
    }

    @FXML
    void onCancelSearch() {
        searchPane.setVisible(false);
    }


    ////////////////////////////////////////
    //                                    //
    //       Edit Service Request         //
    //                                    //
    ////////////////////////////////////////

    public void onStatusBox() {
        try {
            if (statusBox.getSelectionModel().getSelectedItem().equals("Incomplete")) {
                newStatus = "Incomplete";
                statusChange = true;
            } else if (statusBox.getSelectionModel().getSelectedItem().equals("In Progress")) {
                newStatus = "In Progress";
                statusChange = true;
            } else if (statusBox.getSelectionModel().getSelectedItem().equals("Complete")) {
                newStatus = "Complete";
                statusChange = true;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void onCancelEdit() {
        editRequestPane.setVisible(false);
        searchPane.setVisible(true);
    }

    public void onSubmitEdit() {
        if (statusChange && newStatus.compareTo(serviceRequestPopUp.getStatus()) != 0) {
            serviceRequestPopUp.setStatus(newStatus);
            if (newStatus.equals("Complete")) {
                serviceRequestPopUp.setCompletedBy(PermissionSingleton.getInstance().getCurrUser());
                ServiceRequestSingleton.getInstance().updateCompletedBy(serviceRequestPopUp);
            }
            ServiceRequestSingleton.getInstance().updateStatus(serviceRequestPopUp);

        }
        editRequestPane.setVisible(false);
        searchPane.setVisible(true);
    }
}
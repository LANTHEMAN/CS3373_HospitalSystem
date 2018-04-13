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
import edu.wpi.cs3733d18.teamF.graph.*;
import edu.wpi.cs3733d18.teamF.qr.qrConverter;
import edu.wpi.cs3733d18.teamF.sr.*;
import edu.wpi.cs3733d18.teamF.voice.VoiceLauncher;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
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

    private boolean draggingNode;
    private Node heldNode;

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
    private ConcurrentLinkedQueue<String> commands = new ConcurrentLinkedQueue<>();
    private PaneVoiceController paneVoiceController;
    private Voice voice;
    private VoiceManager voiceManager = VoiceManager.getInstance();
    @FXML
    private HBox algorithmsBox;
    @FXML
    private JFXButton aStar;
    @FXML
    private JFXButton depthFirst;
    @FXML
    private JFXButton breathFirst;
    ////////////////////////////////////////////////////
    //                                                //
    //           Search Service Request Variables     //
    //                                                //
    ////////////////////////////////////////////////////
    private String searchType;
    private String filter;
    private ServiceRequest serviceRequestPopUp;
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
    private Node selectedNodeEnd = null;
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
    @FXML
    private JFXTextArea txtDirections;
    @FXML
    private Pane qrImage;
    @FXML
    private JFXButton mapEditorBtn;
    @FXML
    private JFXButton editUsersBtn;
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
    //           User Pop Up              //
    //                                    //
    ////////////////////////////////////////

    @FXML
    private Label userLabel;
    @FXML
    private JFXCheckBox languageCheck;
    @FXML
    private JFXCheckBox religiousCheck;
    @FXML
    private JFXCheckBox securityCheck;
    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXTextField passwordField;
    @FXML
    private JFXTextField fnameField;
    @FXML
    private JFXTextField lnameField;
    @FXML
    private JFXTextField occupationField;
    @FXML
    private JFXComboBox privilegeCombo;
    @FXML
    private AnchorPane newUserPane;
    String privilegeChoice;
    User editedUser;
    boolean newUser;
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
                if (command.contains("HEY KIOSK") || command.contains("HELLO KIOSK")) {
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
                    } else if (command.contains("DIRECTIONS") || command.contains("WHERE")) {
                        if (command.contains("BATHROOM")) {
                            Node src = map.findNodeClosestTo(selectedNodeStart.getPosition().getX(), selectedNodeStart.getPosition().getY(), map.is2D(), node -> node.getFloor().equals(map.getFloor()));
                            Path path = map.getPath(src, map.findNodeClosestTo(src, node -> node.getNodeType().equals("REST")));
                            mapDrawController.showPath(path);
                            displayTextDirections(path);
                            voice.speak("Here is the route to the nearest bathroom");
                            displayTextDirections(map.getPath(src, map.findNodeClosestTo(src, node -> node.getNodeType().equals("REST"))));

                        } else if (command.contains("EXIT")) {
                            Path path = map.getPath
                                    (map.findNodeClosestTo(selectedNodeStart.getPosition().getX(), selectedNodeStart.getPosition().getY(), true)
                                            , map.findNodeClosestTo(selectedNodeStart.getPosition().getX(), selectedNodeStart.getPosition().getY(), true, node -> node.getNodeType().equals("EXIT")));
                            mapDrawController.showPath(path);
                            displayTextDirections(path);
                            voice.speak("Here is the route to the nearest exit");
                            displayTextDirections(path);

                        } else if (command.contains("NEUROSCIENCE")) {
                            Path path = map.getPath
                                    (map.findNodeClosestTo(selectedNodeStart.getPosition().getX(), selectedNodeStart.getPosition().getY(), true)
                                            , map.findNodeClosestTo(selectedNodeStart.getPosition().getX(), selectedNodeStart.getPosition().getY(), true, node -> node.getLongName().contains("Neuroscience")));
                            mapDrawController.showPath(path);
                            displayTextDirections(path);
                            voice.speak("Here is the route to neuroscience");
                            displayTextDirections(path);

                        } else if (command.contains("ORTHOPEDICS") || command.contains("RHEMUTOLOGY")) {
                            Path path = map.getPath
                                    (map.findNodeClosestTo(selectedNodeStart.getPosition().getX(), selectedNodeStart.getPosition().getY(), true)
                                            , map.findNodeClosestTo(selectedNodeStart.getPosition().getX(), selectedNodeStart.getPosition().getY(), true, node -> node.getLongName().contains("Orthopedics")));
                            mapDrawController.showPath(path);
                            displayTextDirections(path);
                            voice.speak("Here is the route to Orthopedics and Rhemutology");
                            displayTextDirections(path);

                        } else if (command.contains("PARKING") && command.contains("GARAGE")) {
                            Path path = map.getPath
                                    (map.findNodeClosestTo(selectedNodeStart.getPosition().getX(), selectedNodeStart.getPosition().getY(), true)
                                            , map.findNodeClosestTo(selectedNodeStart.getPosition().getX(), selectedNodeStart.getPosition().getY(), true, node -> node.getLongName().contains("Parking") &&
                                                    node.getLongName().contains("Garage")));
                            mapDrawController.showPath(path);
                            displayTextDirections(path);
                            for (Node n : path.getNodes()) {
                                System.out.println("n.getPosition() = " + n.getPosition());
                            }
                            voice.speak("Here is the route to the parking garage");
                            displayTextDirections(path);

                        } else if (command.contains("ELEVATOR")) {
                            Path path = map.getPath
                                    (map.findNodeClosestTo(selectedNodeStart.getPosition().getX(), selectedNodeStart.getPosition().getY(), true)
                                            , map.findNodeClosestTo(selectedNodeStart.getPosition().getX(), selectedNodeStart.getPosition().getY(), true, node -> node.getNodeType().equals("ELEV")));
                            mapDrawController.showPath(path);
                            displayTextDirections(path);
                            voice.speak("Here is the route to the nearest elevator");
                            displayTextDirections(path);

                        } else if (command.contains("DENTIST") || command.contains("DENTISTRY") || command.contains("ORAL")) {
                            Path path = map.getPath
                                    (map.findNodeClosestTo(selectedNodeStart.getPosition().getX(), selectedNodeStart.getPosition().getY(), true)
                                            , map.findNodeClosestTo(selectedNodeStart.getPosition().getX(), selectedNodeStart.getPosition().getY(), true, node -> node.getLongName().contains("Dentistry")));
                            mapDrawController.showPath(path);
                            displayTextDirections(path);
                            voice.speak("Here is the route to Dentistry and Oral Medicine");
                            displayTextDirections(path);

                        } else if (command.contains("PLASTIC SURGERY")) {
                            Path path = map.getPath
                                    (map.findNodeClosestTo(selectedNodeStart.getPosition().getX(), selectedNodeStart.getPosition().getY(), true)
                                            , map.findNodeClosestTo(selectedNodeStart.getPosition().getX(), selectedNodeStart.getPosition().getY(), true, node -> node.getLongName().contains("Plastic Surgery")));
                            mapDrawController.showPath(path);
                            displayTextDirections(path);
                            voice.speak("Here is the route to Plastic Surgery");
                            displayTextDirections(path);

                        } else if (command.contains("RADIOLOGY")) {
                            Path path = map.getPath
                                    (map.findNodeClosestTo(selectedNodeStart.getPosition().getX(), selectedNodeStart.getPosition().getY(), true)
                                            , map.findNodeClosestTo(selectedNodeStart.getPosition().getX(), selectedNodeStart.getPosition().getY(), true, node -> node.getLongName().contains("Radiation")));
                            mapDrawController.showPath(path);
                            displayTextDirections(path);
                            voice.speak("Here is the route to Radiology");
                            displayTextDirections(path);

                        } else if (command.contains("NUCLEAR")) {
                            Path path = map.getPath
                                    (map.findNodeClosestTo(selectedNodeStart.getPosition().getX(), selectedNodeStart.getPosition().getY(), true)
                                            , map.findNodeClosestTo(selectedNodeStart.getPosition().getX(), selectedNodeStart.getPosition().getY(), true, node -> node.getLongName().contains("Nuclear")));
                            mapDrawController.showPath(path);
                            displayTextDirections(path);
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
                    } else if (command.contains("WEATHER")) {
                        YahooWeatherService service = null;
                        try {
                            service = new YahooWeatherService();
                        } catch (JAXBException e) {
                            e.printStackTrace();
                        }
                        Channel channel = null;
                        try {
                            channel = service.getForecast("2523945", DegreeUnit.FAHRENHEIT);
                        } catch (JAXBException | IOException e) {
                            e.printStackTrace();
                        }
                        voice.speak(String.format("The temperature is %d degrees fahrenheit", channel.getItem().getCondition().getTemp()));
                        voice.speak(channel.getAtmosphere().toString());
                    } else if (command.contains("RAP")) {
                        voice.speak("Boots and Cats and Boots and Cats and Boots and Cats and Boots and Cats and Boots" +
                                "and Cats and Boots and Cats and Boots and Cats and Boots and Cats and Boots and Cats and Boots");
                    }
                }
            }
        }
    }));

    @FXML
    private JFXTextField usernameSearch;
    @FXML
    private JFXListView usernameList;
    @FXML
    private JFXCheckBox completeCheck;
    @FXML
    private JFXNodesList serviceRequestList;
    @FXML
    private JFXButton newServiceRequest;


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

        selectedNodeStart = map.findNodeClosestTo(1850, 1035, true, node -> node.getFloor().equals(map.getFloor()));
        sourceLocation.setText(selectedNodeStart.getLongName());

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
                selectedNodeEnd = null;
                ctrlHeld = false;
            }
        });

        mapContainer.setOnMouseReleased(e -> {
            if (!PermissionSingleton.getInstance().isAdmin() || !ctrlHeld) {
                return;
            }

            draggingNode = false;

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

                if (node == selectedNodeEnd) {
                    selectedNodeEnd = null;
                    return;
                }

                if (e.getButton() == MouseButton.PRIMARY) {
                    map.addEdge(node, selectedNodeEnd);
                }

                if (e.getButton() == MouseButton.SECONDARY) {
                    map.removeEdge(node, selectedNodeEnd);
                }
                selectedNodeEnd = null;
            } else {
                selectedNodeEnd = null;
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
                nodes.add(map.findNodeClosestTo(mapPos.getX(), mapPos.getY(), map.is2D(), node -> node.getFloor().equals(map.getFloor())));
            }
            if (nodes.size() > 0 && e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 1) {
                if (!nodesShown) {

                    Node src = map.findNodeClosestTo(mapPos.getX(), mapPos.getY(), map.is2D(), node -> node.getFloor().equals(map.getFloor()));
                    if ((map.is2D() ? mapPos.distance(src.getPosition()) : mapPos.distance(src.getWireframePosition())) < 200) {
                        Path path = map.getPath(map.findNodeClosestTo(selectedNodeStart.getPosition().getX(), selectedNodeStart.getPosition().getY(), true), src);
                        mapDrawController.showPath(path);
                        displayTextDirections(path);
                    } else {
                        return;
                    }
                }

                Node node = map.findNodeClosestTo(mapPos.getX(), mapPos.getY(), map.is2D(), node1 -> node1.getFloor().equals(map.getFloor()));
                if(nodesShown) {
                    mapDrawController.selectNode(node);
                }
                selectedNodeEnd = node;

                searchLocation.setText(node.getLongName());
                destinationLocation.setText(node.getLongName());

                if (PermissionSingleton.getInstance().isAdmin()) {
                    gpaneNodeInfo.setVisible(true);
                } else {
                    gpaneNodeInfo.setVisible(false);
                }

                modNode_x.setText(String.valueOf(node.getPosition().getX()));
                modNode_y.setText(String.valueOf(node.getPosition().getY()));
                modNode_shortName.setText(node.getShortName());
                modNode_longName.setText(node.getLongName());
                // TODO implement change building, change type

                modifyNode = node;

            }

            if (e.getButton() == MouseButton.SECONDARY && e.getClickCount() == 1) {
                if (nodes.size() > 0 && !nodesShown) {
                    selectedNodeStart = nodes.iterator().next();
                    Path path = map.getPath(selectedNodeStart, selectedNodeEnd);
                    if (path.getNodes().size() < 2) {
                        if (map.getNeighbors(selectedNodeStart).size() > 0) {
                            path = map.getPath(selectedNodeStart, map.getNeighbors(selectedNodeStart).iterator().next());
                        }
                    }
                    mapDrawController.showPath(path);
                    displayTextDirections(path);

                    sourceLocation.setText(selectedNodeStart.getLongName());
                }
            }

            // remove a node
            if (e.getButton() == MouseButton.SECONDARY && e.getClickCount() == 2) {
                if (!map.is2D()) {
                    return;
                }
                if (nodes.size() > 0) {
                    // TODO get closest node
                    //selectedNodeStart = map.findNodeClosestTo(1850, 1035, true, node -> node.getFloor().equals(map.getFloor()));
                    //sourceLocation.setText(selectedNodeStart.getLongName());
                    mapDrawController.unshowPath();
                    map.removeNode(selectedNodeEnd);
                    selectedNodeEnd = null;
                }
                selectedNodeEnd = null;
            }
            // create a new node
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                if (!map.is2D() || !PermissionSingleton.getInstance().isAdmin()) {
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
                    System.out.println("Picked Up Node");
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

            if (PermissionSingleton.getInstance().isAdmin() && nodesShown) {
                if (draggingNode) {
                    heldNode.setPosition(mapPos);
                    //System.out.println("Holding Node");
                }
            }
        });


        floorBtn.setText("2");
        l2.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            map.setFloor("L2");
            floorBtn.setText("L2");
            MainTitle.setText("Brigham and Women's Hospital: Lower Level 2");
            floorNode.animateList(false);
            reloadMap();
        });
        l1.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            map.setFloor("L1");
            floorBtn.setText("L1");
            MainTitle.setText("Brigham and Women's Hospital: Lower Level 1");
            floorNode.animateList(false);
            reloadMap();
        });
        groundFloor.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            map.setFloor("0G");
            floorBtn.setText("G");
            MainTitle.setText("Brigham and Women's Hospital: Ground Floor");
            floorNode.animateList(false);
            reloadMap();
        });
        floor1.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            map.setFloor("01");
            floorBtn.setText("1");
            MainTitle.setText("Brigham and Women's Hospital: Level 1");
            floorNode.animateList(false);
            reloadMap();
        });
        floor2.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            map.setFloor("02");
            floorBtn.setText("2");
            MainTitle.setText("Brigham and Women's Hospital: Level 2");
            floorNode.animateList(false);
            reloadMap();
        });
        floor3.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            map.setFloor("03");
            floorBtn.setText("3");
            MainTitle.setText("Brigham and Women's Hospital: Level 3");
            floorNode.animateList(false);
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
        privilegeCombo.getItems().addAll(privilegeOptions);

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
                    if (PermissionSingleton.getInstance().getUserPrivilege().equals("Admin")) {
                        setAdminMenu();
                    } else if (PermissionSingleton.getInstance().getUserPrivilege().equals("Staff")) {
                        setStaffMenu();
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

        serviceRequestList.addAnimatedNode(newServiceRequest);

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


    private void displayTextDirections(Path route) {

        List<String> directions = route.makeTextDirections();

        StringBuilder sb = new StringBuilder();
        for (String text : directions) {

            if (sb.length() > 0) {
                sb.append("\n");
            }
            sb.append(text);
        }
        txtDirections.setText(sb.toString());
        qrConverter qr = new qrConverter(sb.toString());
        qrImage.getChildren().add(qr.getQrView());
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
        onCancelDirectionsEvent();
        setCancelMenuEvent();
        serviceRequestList.animateList(false);
        serviceRequestList.animateList(false);
        setGuestMenu();
        if (algorithmsBox.isVisible()){
            algorithmsBox.setVisible(false);
        }
        mapDrawController.unshowNodes();
        mapDrawController.unshowEdges();
        loginBtn.setText("Login");

    }

    @FXML
    private void onArrowEvent() {
        if (directionsDrawer.isHidden()) {
            directionsDrawer.open();
            directionsDrawer.toFront();
            destinationLocation.setText(searchLocation.getText());
        }

    }

    @FXML
    private void onCancelDirectionsEvent() {
        if (directionsDrawer.isShown()) {
            directionsDrawer.close();
            directionsDrawer.toBack();
            searchLocation.setText(destinationLocation.getText());
        }


    }

    @FXML
    private void setCancelMenuEvent() {
        if (adminDrawer.isShown()) {
            adminDrawer.close();
            adminDrawer.toBack();
        }
        if(serviceRequestList.isExpanded()){
            serviceRequestList.animateList(false);
        }
    }

    @FXML
    private JFXButton LI;
    @FXML
    private JFXButton RS;
    @FXML
    private JFXButton SR;

    private void setGuestMenu() {
        hamburger.setVisible(false);
        hamburgerD.setVisible(false);
        //serviceRequestList.getChildren().clear();
        serviceRequestList.setRotate(0);
    }

    private void setAdminMenu() {
        hamburger.setVisible(true);
        hamburgerD.setVisible(true);
        mapEditorBtn.setVisible(true);
        editUsersBtn.setVisible(true);
        algorithmsBox.setVisible(true);
        if(serviceRequestList.getChildren().contains(LI)){
            serviceRequestList.getChildren().remove(LI);
        }
        if(serviceRequestList.getChildren().contains(RS)){
            serviceRequestList.getChildren().remove(RS);
        }
        if(serviceRequestList.getChildren().contains(SR)){
            serviceRequestList.getChildren().remove(SR);
        }
        serviceRequestList.addAnimatedNode(LI);
        serviceRequestList.addAnimatedNode(RS);
        serviceRequestList.addAnimatedNode(SR);
        serviceRequestList.setRotate(-90);
    }

    private void setStaffMenu() {
        hamburger.setVisible(true);
        hamburgerD.setVisible(true);
        mapEditorBtn.setVisible(false);
        editUsersBtn.setVisible(false);
        if(serviceRequestList.getChildren().contains(LI)){
            serviceRequestList.getChildren().remove(LI);
        }
        if(serviceRequestList.getChildren().contains(RS)){
            serviceRequestList.getChildren().remove(RS);
        }
        if(serviceRequestList.getChildren().contains(SR)){
            serviceRequestList.getChildren().remove(SR);
        }
        if(ServiceRequestSingleton.getInstance().isInTable(PermissionSingleton.getInstance().getCurrUser(), "LanguageInterpreter")){
            serviceRequestList.addAnimatedNode(LI);
        }
        if(ServiceRequestSingleton.getInstance().isInTable(PermissionSingleton.getInstance().getCurrUser(), "ReligiousServices")){
            serviceRequestList.addAnimatedNode(RS);
        }
        if(ServiceRequestSingleton.getInstance().isInTable(PermissionSingleton.getInstance().getCurrUser(), "SecurityRequest")){
            serviceRequestList.addAnimatedNode(SR);
        }
        serviceRequestList.setRotate(-90);
    }

    @FXML
    private void onAStar(){
        MapSingleton.getInstance().getMap().setPathSelector(new AStar());
        aStar.setStyle("-fx-background-color: #303030");
        breathFirst.setStyle("-fx-background-color: #616161");
        depthFirst.setStyle("-fx-background-color: #616161");
    }

    @FXML
    private void onBreathFirst(){
        MapSingleton.getInstance().getMap().setPathSelector(new BreathSearch());
        aStar.setStyle("-fx-background-color: #616161");
        breathFirst.setStyle("-fx-background-color: #303030");
        depthFirst.setStyle("-fx-background-color: #616161");
    }

    @FXML
    private void onDepthFirst(){
        MapSingleton.getInstance().getMap().setPathSelector(new DepthSearch());
        aStar.setStyle("-fx-background-color: #616161");
        breathFirst.setStyle("-fx-background-color: #616161");
        depthFirst.setStyle("-fx-background-color: #303030");
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

    @FXML
    private void onLanguageInterpreter(){
        onCancelDirectionsEvent();
        setCancelMenuEvent();
        languageInterpreterPane.setVisible(true);
        serviceRequestList.animateList(false);
    }
    @FXML
    private void onReligiousServices(){
        onCancelDirectionsEvent();
        setCancelMenuEvent();
        religiousServicesPane.setVisible(true);
        serviceRequestList.animateList(false);
    }
    @FXML
    private void onSecurityRequest(){
        onCancelDirectionsEvent();
        setCancelMenuEvent();
        securityPane.setVisible(true);
        serviceRequestList.animateList(false);
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
        userLabel.setText("New User");
        usernameField.setEditable(true);
        newUser = true;
        usernameField.clear();
        passwordField.clear();
        fnameField.clear();
        lnameField.clear();
        occupationField.clear();
        languageCheck.setSelected(false);
        religiousCheck.setSelected(false);
        securityCheck.setSelected(false);
        privilegeCombo.getSelectionModel().clearSelection();
        editUserPane.setVisible(false);
        newUserPane.setVisible(true);
    }


    private ArrayList<User> autoCompleteUserSearch(String input) {
        ArrayList<User> autoCompleteUser = new ArrayList<>();
        if (input.length() > 0) {
            String sql = "SELECT * FROM HUser";
            try {
                ResultSet resultSet = DatabaseSingleton.getInstance().getDbHandler().runQuery(sql);
                while (resultSet.next()) {

                    String username = resultSet.getString(1);
                    String password = resultSet.getString(2);
                    String firstname = resultSet.getString(3);
                    String lastname = resultSet.getString(4);
                    String privilege = resultSet.getString(5);
                    String occupation = resultSet.getString(6);
                    User temp = new User(username, password, firstname, lastname, privilege, occupation);
                    String searchString = username+password+firstname+lastname+privilege+occupation;
                    if(searchString.toLowerCase().contains(input.toLowerCase())) {
                        autoCompleteUser.add(temp);
                    }

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
        privilegeCol.setCellValueFactory(new PropertyValueFactory<User, String>("privilege"));
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

    private final ObservableList<String> privilegeOptions = FXCollections.observableArrayList(
            "Staff",
            "Admin");

    public void onSelectUser(User e) {
        usernameField.setEditable(false);
        userLabel.setText("Edit User");
        editedUser = e;
        newUser = false;
        usernameField.setText(e.getUname());
        passwordField.setText(e.getPsword());
        fnameField.setText(e.getFirstName());
        lnameField.setText(e.getLastName());
        occupationField.setText(e.getOccupation());
        privilegeCombo.getSelectionModel().select(e.getPrivilege());
        if(ServiceRequestSingleton.getInstance().isInTable(e.getUname(), "LanguageInterpreter")){
            languageCheck.setSelected(true);
        }else{
            languageCheck.setSelected(false);
        }
        if(ServiceRequestSingleton.getInstance().isInTable(e.getUname(), "ReligiousServices")){
            religiousCheck.setSelected(true);
        }else{
            religiousCheck.setSelected(false);
        }
        if(ServiceRequestSingleton.getInstance().isInTable(e.getUname(), "SecurityRequest")){
            securityCheck.setSelected(true);
        }else{
            securityCheck.setSelected(false);
        }

        editUserPane.setVisible(false);
        newUserPane.setVisible(true);
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

    private final ObservableList<String> filterOptions = FXCollections.observableArrayList(
            "Priority",
            "Status",
            "Type");

    @FXML
    private void onSearchServiceRequest() {
        filter = "none";
        searchType = "none";

        filterType.getItems().addAll(filterOptions);

        String lastSearch = ServiceRequestSingleton.getInstance().getLastSearch();
        String lastFilter = ServiceRequestSingleton.getInstance().getLastFilter();
        if (lastSearch != null && lastFilter != null) {
            searchType = lastSearch;
            filter = lastFilter;
        }
        onSearch();
        searchPane.setVisible(true);
        onCancelDirectionsEvent();
        setCancelMenuEvent();
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
            mapDrawController.unshowPath();
            nodesShown = true;
        }
        onCancelDirectionsEvent();
        setCancelMenuEvent();
    }

    @FXML
    public void onEditUsers() {
        onCancelDirectionsEvent();
        setCancelMenuEvent();
        userTextField.clear();
        searchUserResultTable.getItems().clear();
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
            selectedNodeEnd = selectedNode;
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

        Node tempNode = selectedNodeStart;
        selectedNodeStart = selectedNodeEnd;
        selectedNodeEnd = tempNode;

        mapDrawController.showPath(map.getPath(selectedNodeStart, selectedNodeEnd));
        displayTextDirections(map.getPath(selectedNodeStart, selectedNodeEnd));

        map.setFloor(selectedNodeStart.getFloor());
        reloadMap();
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

            selectedNodeStart = sourceNode;
            selectedNodeEnd = destinationNode;

            mapDrawController.showPath(map.getPath(sourceNode, destinationNode));
            displayTextDirections(map.getPath(sourceNode, destinationNode));

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
        serviceRequestPopUp = s;
        typeLabel.setText("Type: " + s.getType());
        idLabel.setText("Service Request #" + s.getId());
        firstNameLabel.setText("First Name: " + s.getFirstName());
        lastNameLabel.setText("Last Name: " + s.getLastName());
        locationLabel.setText(s.getLocation());
        statusLabel.setText(s.getStatus());
        instructionsTextArea.setText(s.getDescription());
        instructionsTextArea.setEditable(false);
        if(s.getStatus().equalsIgnoreCase("Complete")){
            completeCheck.setSelected(true);
        }else{
            completeCheck.setSelected(false);
        }

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

    public void onCancelEdit() {
        editRequestPane.setVisible(false);
        searchPane.setVisible(true);
    }

    public void onSubmitEdit() {
        if (completeCheck.isSelected() && !serviceRequestPopUp.getStatus().equalsIgnoreCase("Complete")) {
            serviceRequestPopUp.setStatus("Complete");
            serviceRequestPopUp.setCompletedBy(PermissionSingleton.getInstance().getCurrUser());
            ServiceRequestSingleton.getInstance().updateCompletedBy(serviceRequestPopUp);
            ServiceRequestSingleton.getInstance().updateStatus(serviceRequestPopUp);
        }
        if (usernameSearch.getText() != null && !usernameSearch.getText().trim().isEmpty()) {
            ServiceRequestSingleton.getInstance().assignTo(usernameSearch.getText(), serviceRequestPopUp);
        }
        usernameSearch.setText("");
        editRequestPane.setVisible(false);
        searchPane.setVisible(true);
        onSearch();

    }

    ////////////////////////////////////////
    //                                    //
    //       New/Edit USer                //
    //                                    //
    ////////////////////////////////////////

    @FXML
    private void onSubmitUser(){
        String username;
        if (newUser) {
            username = usernameField.getText();
        }else{
            username = editedUser.getUname();
        }
        String password = passwordField.getText();
        String firstName = fnameField.getText();
        String lastName = lnameField.getText();
        String occupation = occupationField.getText();
        boolean languageServices = languageCheck.isSelected();
        boolean religiousServices = religiousCheck.isSelected();
        boolean securityRequest = securityCheck.isSelected();

        User temp = new User(username, password, firstName, lastName, privilegeChoice, occupation);
        if(newUser){
            PermissionSingleton.getInstance().addUser(temp);
        }else{
            PermissionSingleton.getInstance().updateUser(temp);
        }

        if(ServiceRequestSingleton.getInstance().isInTable(username, "LanguageInterpreter")){
            if(!languageServices){
                ServiceRequestSingleton.getInstance().removeUsernameLanguageInterpreter(username);
            }
        }else{
            if(languageServices){
                ServiceRequestSingleton.getInstance().addUsernameLanguageInterpreter(username);
            }
        }

        if(ServiceRequestSingleton.getInstance().isInTable(username, "ReligiousServices")){
            if(!religiousServices){
                ServiceRequestSingleton.getInstance().removeUsernameReligiousServices(username);
            }
        }else{
            if(religiousServices){
                ServiceRequestSingleton.getInstance().addUsernameReligiousServices(username);
            }
        }

        if(ServiceRequestSingleton.getInstance().isInTable(username, "SecurityRequest")){
            if(!securityRequest){
                ServiceRequestSingleton.getInstance().removeUsernameSecurityRequest(username);
            }
        }else{
            if(securityRequest){
                ServiceRequestSingleton.getInstance().addUsernameSecurityRequest(username);
            }
        }
        newUserPane.setVisible(false);
        onEditUsers();
    }

    @FXML
    private void onPrivilegeBox(){
        try {
            privilegeChoice = privilegeCombo.getSelectionModel().getSelectedItem().toString();
        } catch (NullPointerException e) {
            privilegeChoice = "Guest";
        }
    }

    @FXML
    public void onCancelUser(){
        newUserPane.setVisible(false);
        editUserPane.setVisible(true);
        onEditUsers();
    }


    ////////////////////////////////////////
    //                                    //
    //       Language Interpreter         //
    //                                    //
    ////////////////////////////////////////

    @FXML
    TextField languageField;

    @FXML
    TextField firstNameLanguage;

    @FXML
    TextField lastNameLanguage;

    @FXML
    TextField destinationLanguage;

    @FXML
    TextArea instructionsLanguage;

    @FXML
    Label languageRequiredLI;

    @FXML
    Label firstNameRequiredLI;

    @FXML
    Label lastNameRequiredLI;

    @FXML
    Label locationRequiredLI;
    @FXML
    private AnchorPane languageInterpreterPane;


    @FXML
    void onCancelLI() {
        languageInterpreterPane.setVisible(false);
    }

    @FXML
    void onSubmitLI() {
        int requiredFieldsEmpty = 0;
        String l;
        String first_name;
        String last_name;
        String location;
        String description;
        if (languageField.getText() == null || languageField.getText().trim().isEmpty()) {
            languageRequiredLI.setVisible(true);
            requiredFieldsEmpty++;
        }
        if (firstNameLanguage.getText() == null || firstNameLanguage.getText().trim().isEmpty()) {
            firstNameRequiredLI.setVisible(true);
            requiredFieldsEmpty++;
        }
        if (lastNameLanguage.getText() == null || lastNameLanguage.getText().trim().isEmpty()) {
            lastNameRequiredLI.setVisible(true);
            requiredFieldsEmpty++;
        }
        if (destinationLanguage.getText() == null || destinationLanguage.getText().trim().isEmpty()) {
            locationRequiredLI.setVisible(true);
            requiredFieldsEmpty++;
        }
        if (requiredFieldsEmpty > 0) {
            return;
        }

        if (instructionsLanguage.getText() == null || instructionsLanguage.getText().trim().isEmpty()) {
            description = "N/A";
        } else {
            description = instructionsLanguage.getText();
        }
        l = languageField.getText();
        first_name = firstNameLanguage.getText();
        last_name = lastNameLanguage.getText();
        location = destinationLanguage.getText();
        String new_description = l + "/////" + description;
        ServiceRequest request = new LanguageInterpreter(first_name, last_name, location, new_description, "Incomplete", 1, l);
        ServiceRequestSingleton.getInstance().sendServiceRequest(request);
        ServiceRequestSingleton.getInstance().addServiceRequest(request);
        languageInterpreterPane.setVisible(false);

    }


    @FXML
    TextField religionField;

    @FXML
    TextField firstNameRS;

    @FXML
    TextField lastNameRS;

    @FXML
    TextField destinationRS;

    @FXML
    TextArea instructionsRS;

    @FXML
    Label religionRequiredRS;

    @FXML
    Label firstNameRequiredRS;

    @FXML
    Label lastNameRequiredRS;

    @FXML
    Label locationRequiredRS;
    @FXML
    private AnchorPane religiousServicesPane;


    @FXML
    void onCancelRS() {
        religiousServicesPane.setVisible(false);
    }

    @FXML
    void onSubmitRS() {
        int requiredFieldsEmpty = 0;
        String r;
        String first_name;
        String last_name;
        String location;
        String description;
        if (religionField.getText() == null || religionField.getText().trim().isEmpty()) {
            religionRequiredRS.setVisible(true);
            requiredFieldsEmpty++;
        }
        if (firstNameRS.getText() == null || firstNameRS.getText().trim().isEmpty()) {
            firstNameRequiredRS.setVisible(true);
            requiredFieldsEmpty++;
        }
        if (lastNameRS.getText() == null || lastNameRS.getText().trim().isEmpty()) {
            lastNameRequiredRS.setVisible(true);
            requiredFieldsEmpty++;
        }
        if (destinationRS.getText() == null || destinationRS.getText().trim().isEmpty()) {
            locationRequiredRS.setVisible(true);
            requiredFieldsEmpty++;
        }
        if (requiredFieldsEmpty > 0) {
            return;
        }

        if (instructionsRS.getText() == null || instructionsRS.getText().trim().isEmpty()) {
            description = "N/A";
        } else {
            description = instructionsRS.getText();
        }
        r = religionField.getText();
        first_name = firstNameRS.getText();
        last_name = lastNameRS.getText();
        location = destinationRS.getText();
        String new_description = r + "/////" + description + "\n";
        ServiceRequest request = new ReligiousServices(first_name, last_name, location, new_description, "Incomplete", 1, r);
        ServiceRequestSingleton.getInstance().sendServiceRequest(request);
        ServiceRequestSingleton.getInstance().addServiceRequest(request);
        religiousServicesPane.setVisible(false);
    }


    ////////////////////////////////////////
    //                                    //
    //       Security Services            //
    //                                    //
    ////////////////////////////////////////
    @FXML
    private JFXTextArea securityTextArea;
    @FXML
    private JFXTextField securityLocationField;
    @FXML
    private ToggleGroup securityToogle;
    @FXML
    private Label securityLocationRequired;
    @FXML
    private VBox securityPane;

    @FXML
    private void onCancelSecurity(){
        securityPane.setVisible(false);
    }

    @FXML
    private void onSubmitSecurity(){
        if (securityLocationField.getText() == null || securityLocationField.getText().trim().isEmpty()) {
            securityLocationRequired.setVisible(true);
            return;
        }
        String location = securityLocationField.getText();
        String description = securityTextArea.getText();
        String status = "Incomplete";
        RadioButton selected = (RadioButton) securityToogle.getSelectedToggle();
        int priority = Integer.parseInt(selected.getText());
        SecurityRequest sec = new SecurityRequest(location, description, status, priority);

        ServiceRequestSingleton.getInstance().sendServiceRequest(sec);
        ServiceRequestSingleton.getInstance().addServiceRequest(sec);
        securityPane.setVisible(false);
    }
}
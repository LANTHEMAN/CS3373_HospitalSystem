package edu.wpi.cs3733d18.teamF.controller.page;

import com.github.sarxos.webcam.Webcam;
import com.jfoenix.controls.*;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.javascript.object.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import edu.wpi.cs3733d18.teamF.controller.*;
import edu.wpi.cs3733d18.teamF.controller.page.element.about.AboutElement;
import edu.wpi.cs3733d18.teamF.controller.page.element.mapView.MapMementoSingleton;
import edu.wpi.cs3733d18.teamF.controller.page.element.mapView.MapState;
import edu.wpi.cs3733d18.teamF.controller.page.element.mapView.MapViewElement;
import edu.wpi.cs3733d18.teamF.controller.page.element.mapView.MapViewListener;
import edu.wpi.cs3733d18.teamF.controller.page.element.screensaver.Screensaver;
import edu.wpi.cs3733d18.teamF.db.DatabaseWrapper;
import edu.wpi.cs3733d18.teamF.face.FaceLauncher;
import edu.wpi.cs3733d18.teamF.gfx.PaneVoiceController;
import edu.wpi.cs3733d18.teamF.gfx.impl.radial.GenericRadial;
import edu.wpi.cs3733d18.teamF.graph.Map;
import edu.wpi.cs3733d18.teamF.graph.*;
import edu.wpi.cs3733d18.teamF.graph.pathfinding.*;
import edu.wpi.cs3733d18.teamF.qr.qrConverter;
import edu.wpi.cs3733d18.teamF.sr.ServiceRequestSingleton;
import edu.wpi.cs3733d18.teamF.sr.ServiceRequests;
import edu.wpi.cs3733d18.teamF.voice.VoiceCommandVerification;
import edu.wpi.cs3733d18.teamF.voice.VoiceLauncher;
import javafx.animation.Animation;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.Pair;

import javax.imageio.ImageIO;
import javax.xml.ws.Service;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

import static edu.wpi.cs3733d18.teamF.db.DatabaseWrapper.autoCompleteUserSearch;

public class HomeController implements SwitchableController, Observer, MapViewListener {
    private static MapState savedState;
    private final ObservableList<String> privilegeOptions = FXCollections.observableArrayList("Staff", "Admin");
    @FXML
    public TableColumn chooseCol;
    @FXML
    public TableColumn<User, String> usernameCol, firstNameUserCol, lastNameUserCol, privilegeCol, occupationCol;
    MapViewElement mapViewElement;
    AboutElement aboutElement;
    Screensaver screensaver;
    @FXML
    AnchorPane mapElementPane;
    @FXML
    AnchorPane aboutElementPane;
    /////////////////////////////////
    //                             //
    //     Facial Recognition      //
    //                             //
    /////////////////////////////////
    FaceLauncher launcher = new FaceLauncher();
    private PaneSwitcher switcher;
    private ObservableResourceFactory resFactory = new ObservableResourceFactory();
    ///////////////////////////////
    //                           //
    //           Voice           //
    //                           //
    ///////////////////////////////
    private PaneVoiceController paneVoiceController;
    @FXML
    private Pane voicePane;
    /////////////////////////////////
    //       Search Services       //
    /////////////////////////////////
    private String privilegeChoice;
    private User editedUser;
    private boolean newUser;
    //////////////////////////////////////////
    //                                      //
    //           Search Algorithm           //
    //                                      //
    //////////////////////////////////////////
    @FXML
    private VBox algorithmsBox;
    @FXML
    private JFXButton aStar, depthFirst, breathFirst, dijkstra, bestFirst;
    /////////////////////////////
    //                         //
    //         Language        //
    //                         //
    /////////////////////////////
    @FXML
    private JFXNodesList languageNode;
    @FXML
    private JFXButton languageBtn, english, french, spanish, chinese;
    /////////////////////////
    //                     //
    //         Map         //
    //                     //
    /////////////////////////
    private Map map;
    @FXML
    private Text MainTitle;
    @FXML
    private JFXButton addNodeBtn, remNodeBtn, addEdgeBtn, remEdgeBtn, modifyBtn, dragBtn, panBtn;
    /////////////////////////
    //                     //
    //         Map         //
    //                     //
    /////////////////////////
    @FXML
    private HBox floorTraversal;
    ////////////////////////////////
    //                            //
    //         Map Builder        //
    //                            //
    ////////////////////////////////
    @FXML
    private JFXNodesList floorNode;
    @FXML
    private JFXButton floorBtn, l2, l1, groundFloor, floor1, floor2, floor3;
    @FXML
    private JFXButton btn2D, btn3D;
    /////////////////////////////////
    //      Modify Node Panel      //
    /////////////////////////////////
    @FXML
    private GridPane gpaneNodeInfo;
    @FXML
    private TextField modNode_shortName, modNode_longName, modNode_x, modNode_y;
    @FXML
    private ComboBox modNode_type = new ComboBox<>(FXCollections.observableArrayList(NodeBuilder.getNodeTypes()));
    @FXML
    private ComboBox modNode_building = new ComboBox<>(FXCollections.observableArrayList(NodeBuilder.getBuildings()));
    ///////////////////////////////
    //      New Node Window      //
    ///////////////////////////////
    @FXML
    private VBox addLocationPopup;
    @FXML
    private TextField newNode_x, newNode_y, newNode_shortName;
    @FXML
    private ComboBox<String> newNode_type = new ComboBox<>(FXCollections.observableArrayList(NodeBuilder.getNodeTypes()));
    ///////////////////////////////
    //                           //
    //           Login           //
    //                           //
    ///////////////////////////////
    @FXML
    private JFXDrawer loginDrawer;
    @FXML
    private VBox loginBox, logoutBox;
    @FXML
    private JFXButton loginBtn;
    @FXML
    private JFXTextField loginUsername;
    @FXML
    private JFXPasswordField loginPassword;
    @FXML
    private FontAwesomeIconView loginCancel, logoutCancel;
    ///////////////////////////////////
    //                               //
    //       Search Location         //
    //                               //
    ///////////////////////////////////
    @FXML
    private HBox searchBar;
    @FXML
    private JFXTextField searchLocation, sourceLocation, destinationLocation;
    @FXML
    private JFXListView searchList, directionsList;
    private boolean sourceLocationActive = false;
    /////////////////////////////////
    //                             //
    //          Hamburger          //
    //                             //
    /////////////////////////////////
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXDrawer menuDrawer, adminDrawer, guestDrawer;
    @FXML
    private GridPane menu;
    @FXML
    private VBox adminBox, guestBox;
    @FXML
    private JFXButton mapEditorBtn, editUsersBtn;
    /////////////////////////////
    //      Directions Box     //
    /////////////////////////////
    @FXML
    private VBox directionsBox;
    @FXML
    private JFXDrawer directionsDrawer;
    @FXML
    private JFXHamburger hamburgerD;
    @FXML
    private FontAwesomeIconView imgDistance, imgTime;
    @FXML
    private Text txtDistance, txtTime;
    @FXML
    private TextFlow txtDirections;
    @FXML
    private Pane qrImage;
    @FXML
    private FontAwesomeIconView elevatorBan, stairBan;
    //////////////////////////////
    //                          //
    //        Edit User         //
    //                          //
    //////////////////////////////
    @FXML
    private AnchorPane editUserPane, newUserPane;
    @FXML
    private JFXTextField userTextField;
    @FXML
    private TableView<User> searchUserResultTable;
    @FXML
    private Label userLabel;
    @FXML
    private JFXCheckBox languageCheck, religiousCheck, securityCheck, maintenanceCheck;
    @FXML
    private JFXTextField usernameField, passwordField, fnameField, lnameField, occupationField;
    @FXML
    private JFXComboBox privilegeCombo;
    /////////////////////////////////
    //                             //
    //           Help Pane         //
    //                             //
    /////////////////////////////////
    @FXML
    private AnchorPane helpPane;
    /////////////////////////////////
    //                             //
    //           Date/Time         //
    //                             //
    /////////////////////////////////
    @FXML
    private Label time;
    @FXML
    private Label date;
    /////////////////////////////////
    //                             //
    //           Map Editor        //
    //                             //
    /////////////////////////////////
    @FXML
    private JFXDrawer mapEditorDrawer;
    @FXML
    private AnchorPane mapEditorBtns;
    private MapState state;
    @FXML
    private AnchorPane screensaverPane;
    //////////////////////////////////
    //                              //
    //           Google Maps        //
    //                              //
    //////////////////////////////////
    @FXML
    private GoogleMapView googleMapView;
    private GoogleMap gmap;
    private boolean isGoogleMapViewEnabled = false;

    /////////////////////////////////
    //                             //
    //           Emergency         //
    //                             //
    /////////////////////////////////
    @FXML
    private JFXButton emergencyBtn;
    @FXML
    private GridPane leftGPane;


    ///////////////////////
    //                   //
    //       Inbox       //
    //                   //
    ///////////////////////
    @FXML
    private Label inboxNum;

    ////////////////////////////////////
    //                                //
    //       Screensaver Timeout      //
    //                                //
    ////////////////////////////////////
    @FXML
    private JFXSlider sliderTimeout;
    @FXML
    private FontAwesomeIconView userIDCancel;
    @FXML
    private FontAwesomeIconView userIDSubmit;
    @FXML
    private ImageView userIDView;
    @FXML
    private JFXTextField faceIDField;
    @FXML
    private JFXCheckBox allFloors;

    // uhg
    @FXML
    private GridPane rootPane;
    private GenericRadial radialMenu;
    ///////////////////////
    //                   //
    //       Inbox       //
    //                   //
    ///////////////////////
    @FXML
    private VBox inboxRequests;
    @FXML
    private JFXTextField inboxSearch;
    @FXML
    private JFXComboBox inboxSort;

    /**
     * Constructor for this class
     *
     * @param switcher allows the class to switch panes and get access to the scene
     */
    @Override
    public void initialize(PaneSwitcher switcher) {
        algorithmsBox.setVisible(false);
        adminDrawer.setDisable(true);
        guestDrawer.setDisable(true);
        menuDrawer.setDisable(true);
        directionsDrawer.setDisable(true);
        mapEditorDrawer.setDisable(true);
        loginDrawer.setDisable(true);
        // initialize fundamentals
        this.switcher = switcher;
        map = MapSingleton.getInstance().getMap();
        resetFloorButtonBorders();

        switch (switcher.resFac.getResources().getLocale().getCountry()) {
            case "FR":
                setButtonBackgroundColor(french, "#436282");
                break;
            case "ES":
                setButtonBackgroundColor(spanish, "#436282");
                break;
            case "CN":
                setButtonBackgroundColor(chinese, "#436282");
                break;
            default: // case "US"
                setButtonBackgroundColor(english, "#436282");
                break;
        }

        // init mapView
        Pair<MapViewElement, Pane> mapElementInfo = switcher.loadElement("mapView.fxml");
        mapViewElement = mapElementInfo.getKey();
        mapViewElement.initialize(this, map, switcher, mapElementPane);

        changeFloor("01");

        // google maps
        googleMapView.addMapInializedListener(this::configureMap);
        setGoogleMapViewEnabled(false);

        // init about element
        Pair<AboutElement, Pane> aboutElementInfo = switcher.loadElement("about.fxml");
        aboutElement = aboutElementInfo.getKey();
        aboutElement.initialize(aboutElementPane);
        aboutElement.hideElement();

        //init memento singleton
        MapMementoSingleton.getInstance().init(mapViewElement);

        //init screensaver
        Pair<Screensaver, Pane> screensaverInfo = switcher.loadElement("screensaver.fxml");
        screensaver = screensaverInfo.getKey();
        screensaver.initialize(screensaverPane, switcher.getScene());
        screensaver.hideElement();

        // init voice overlay
        paneVoiceController = new PaneVoiceController(voicePane);

        VoiceCommandVerification voice = new VoiceCommandVerification();
        voice.addObserver(this);
        VoiceLauncher.getInstance().addObserver(voice);

        mapViewElement.update3DPathDisplay(false);
        allFloors.setSelected(false);


        /// TODO RADIAL MENU
        /// TODO RADIAL MENU
        radialMenu = new GenericRadial(Arrays.asList(
                "9.png"
                , "9.png"
                , "9.png"
                //, "3.png"
                //, "4.png"
        ));

        // rootPane.getChildren().add(radialMenu);

        /// TODO RADIAL MENU
        /// TODO RADIAL MENU


        // set up mod node panel
        modNode_type.getItems().addAll(NodeBuilder.getNodeTypes());
        modNode_building.getItems().addAll(NodeBuilder.getBuildings());

        // set up new node panel
        newNode_type.getItems().addAll(NodeBuilder.getNodeTypes());
        newNode_type.getSelectionModel().selectFirst();


        l2.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            changeFloor("L2");
        });
        l1.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            changeFloor("L1");
        });
        groundFloor.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            changeFloor("0G");
        });
        floor1.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            changeFloor("01");
        });
        floor2.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            changeFloor("02");
        });
        floor3.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            changeFloor("03");
        });

        floorNode.addAnimatedNode(floorBtn);
        floorNode.addAnimatedNode(floor3);
        floorNode.addAnimatedNode(floor2);
        floorNode.addAnimatedNode(floor1);
        floorNode.addAnimatedNode(groundFloor);
        floorNode.addAnimatedNode(l1);
        floorNode.addAnimatedNode(l2);


        languageNode.addAnimatedNode(languageBtn);
        languageNode.addAnimatedNode(english);
        languageNode.addAnimatedNode(french);
        languageNode.addAnimatedNode(spanish);
        languageNode.addAnimatedNode(chinese);
        languageNode.setRotate(180);

        english.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            MapMementoSingleton.getInstance().saveState();
            onEnglish();
            MapMementoSingleton.getInstance().returnToLastState();
        });
        french.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            MapMementoSingleton.getInstance().saveState();
            onFrench();
            MapMementoSingleton.getInstance().returnToLastState();
        });
        spanish.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            MapMementoSingleton.getInstance().saveState();
            onSpanish();
            MapMementoSingleton.getInstance().returnToLastState();
        });
        chinese.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            MapMementoSingleton.getInstance().saveState();
            onChinese();
            MapMementoSingleton.getInstance().returnToLastState();
        });


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
        //adminDrawer.setSidePane(menu);
        menuDrawer.setSidePane(menu);
        //guestDrawer.setSidePane(guestBox);
        privilegeCombo.getItems().addAll(privilegeOptions);
        mapEditorDrawer.setSidePane(mapEditorBtns);


        // login
        loginBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {

            // set text in login button
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
                // if the drawer is out and the password and username match a user
                if (PermissionSingleton.getInstance().login(loginUsername.getText(), loginPassword.getText())) {
                    setLoggedIn();
                } else {
                    loginPassword.setText("");
                    shakePasswordField(loginPassword);
                }
            } else {
                if (PermissionSingleton.getInstance().getUserPrivilege().equals("Guest")) {
                    if (PermissionSingleton.getInstance().forceLogin(launcher.getEmployeeName(launcher.getCameraFaceID()))) {
                        setLoggedIn();
                    } else {
                        loginDrawer.open();
                        loginDrawer.setDisable(false);
                    }
                } else {
                    loginDrawer.open();
                    loginDrawer.setDisable(false);
                }
            }
        });

        loginCancel.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            loginDrawer.close();
            loginDrawer.setDisable(true);
        });

        logoutCancel.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            loginDrawer.close();
            loginDrawer.setDisable(true);
        });


        searchLocation.setOnKeyTyped((KeyEvent e) -> {
            String input = searchLocation.getText();
            input = input.concat("" + e.getCharacter());
            DatabaseWrapper.filterHallAutoComplete(input, searchList);
        });

        sourceLocation.setOnKeyTyped((KeyEvent e) -> {
            String input = sourceLocation.getText();
            input = input.concat("" + e.getCharacter());
            DatabaseWrapper.filterHallAutoComplete(input, directionsList);
            sourceLocationActive = true;
        });

        destinationLocation.setOnKeyTyped((KeyEvent e) -> {
            String input = destinationLocation.getText();
            input = input.concat("" + e.getCharacter());
            DatabaseWrapper.filterHallAutoComplete(input, directionsList);
            sourceLocationActive = false;
        });

        userTextField.setOnKeyTyped((KeyEvent e) -> {
            String input = userTextField.getText();
            input = input.concat("" + e.getCharacter());
            ArrayList<User> list = autoCompleteUserSearch(input);
            displayInUserTable(list);
        });

        userIDCancel.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            userIDView.setVisible(false);
            userIDSubmit.setVisible(false);
            userIDCancel.setVisible(false);
        });

        userIDSubmit.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            userIDView.setVisible(false);
            userIDSubmit.setVisible(false);
            userIDCancel.setVisible(false);
            faceIDField.setText(launcher.addFaceToList(usernameField.getText()));
        });


        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            Calendar cal = Calendar.getInstance();
            int second = cal.get(Calendar.SECOND);
            int minute = cal.get(Calendar.MINUTE);
            int hour = cal.get(Calendar.HOUR) % 12 + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH) % 12 + 1;
            int year = cal.get(Calendar.YEAR);
            time.setText(hour + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second));
            date.setText(month + "/" + day + "/" + year);
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }


    /////////////////////////////////////
    //                                 //
    //          Button Colors          //
    //                                 //
    /////////////////////////////////////

    public void onCameraClicked() throws IOException {
        if (!usernameField.getText().equals("")) {
            try {
                Webcam webcam = Webcam.getDefault(5000);
                webcam.open();
                BufferedImage bufferedImage = webcam.getImage();
                ImageIO.write(bufferedImage, "PNG", new File("curFace.png"));
                webcam.close();

                userIDView.setVisible(true);
                userIDCancel.setVisible(true);
                userIDSubmit.setVisible(true);

                Image imageFX = SwingFXUtils.toFXImage(bufferedImage, null);
                userIDView.setImage(imageFX);
            } catch (TimeoutException e) {
                System.out.println("Failed to load camera");
            }
        }
        //TODO inform the user they need to enter a username first LOL
    }

    public void setLoggedIn() {
        mapViewElement.getMapDrawController().unshowPath();
        loginBtn.setText(PermissionSingleton.getInstance().getCurrUser());

        if (PermissionSingleton.getInstance().getUserPrivilege().equals("Admin")) {
            setAdminMenu();
            inboxNum.setText(Integer.toString(ServiceRequestSingleton.getInstance().numMessagesInInbox(PermissionSingleton.getInstance().getCurrUser())));
        } else if (PermissionSingleton.getInstance().getUserPrivilege().equals("Staff")) {
            setStaffMenu();
            inboxNum.setText(Integer.toString(ServiceRequestSingleton.getInstance().numMessagesInInbox(PermissionSingleton.getInstance().getCurrUser())));
        } else {
            setGuestMenu();
        }

        loginDrawer.close();
        loginDrawer.setDisable(true);
        loginUsername.setText("");
        loginPassword.setText("");
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(o instanceof VoiceCommandVerification)) {
            return;
        }

        if (arg instanceof Node) {
            Node voiceSelectedEnd = (Node) arg;
            Path path = mapViewElement.changePathDestination(voiceSelectedEnd);
            displayTextDirections(path);
            onFloorRefresh();
        } else if (arg instanceof HashSet) {
            HashSet<Node> potentialDestinations = (HashSet<Node>) arg;
            Node voiceSelectedEnd = map.findNodeClosestTo(mapViewElement.getSelectedNodeStart(), potentialDestinations);
            Path path = mapViewElement.changePathDestination(voiceSelectedEnd);
            displayTextDirections(path);
            onFloorRefresh();
        } else if (arg instanceof String) {
            String cmd = (String) arg;
            if (cmd.equalsIgnoreCase("Help")) {
                // popup the help menu
                onHelpPopup();
            } else if (cmd.equalsIgnoreCase("Activate")) {
                // got a string saying that the activation command has been said
                paneVoiceController.setVisibility(true);

            }else if(cmd.equalsIgnoreCase("Fire")){

                triggerEmergency();
            } else {
                if (cmd.equalsIgnoreCase("DisableElevators")) {
                    elevatorBan.setVisible(true);
                    mapViewElement.changePathDestination(mapViewElement.getSelectedNodeEnd());
                } else if (cmd.equalsIgnoreCase("DisableStairs")) {
                    stairBan.setVisible(true);
                    mapViewElement.changePathDestination(mapViewElement.getSelectedNodeEnd());
                } else if (cmd.equalsIgnoreCase("EnableElevators")) {
                    elevatorBan.setVisible(false);
                    mapViewElement.changePathDestination(mapViewElement.getSelectedNodeEnd());
                } else if (cmd.equalsIgnoreCase("EnableStairs")) {
                    stairBan.setVisible(false);
                    mapViewElement.changePathDestination(mapViewElement.getSelectedNodeEnd());
                }
                onFloorRefresh();
            }
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
    void triggerEmergency() {
        Rectangle rectangle = new Rectangle();
        rectangle.setX(0);
        rectangle.setY(0);
        rectangle.setWidth(voicePane.getWidth());
        rectangle.setHeight(voicePane.getHeight() * 2);
        rectangle.setMouseTransparent(true);
        rectangle.setFill(Color.RED);
        rectangle.setOpacity(0.3);
        FillTransition ft = new FillTransition(Duration.millis(1500), rectangle, Color.RED, Color.BLUE);
        ft.setCycleCount(4);
        ft.setAutoReverse(true);
        leftGPane.add(rectangle, 0, 0);

        ft.play();
        HashSet<Node> nodes = map.getNodes(node -> node.getNodeType().equals("EXIT") && !node.getLongName().contains("Ambulance"));
        Node selectedEnd = map.findNodeClosestTo(mapViewElement.getSelectedNodeStart(), nodes);
        Path path = mapViewElement.changePathDestination(selectedEnd);
        displayTextDirections(path);
        ft.setOnFinished((ActionEvent) -> {
            leftGPane.getChildren().removeAll(rectangle);
        });
    }

    @FXML
    public void onLogOutBtn() {
        mapViewElement.setViewMode(MapViewElement.ViewMode.VIEW);
        PermissionSingleton.getInstance().logout();
        loginDrawer.close();
        loginDrawer.setDisable(true);
        gpaneNodeInfo.setVisible(false);
        onCancelDirectionsEvent();
        setCancelMenuEvent();
        if (mapEditorDrawer.isShown()) {
            mapEditorDrawer.close();
            mapEditorDrawer.setDisable(true);
        }
        setGuestMenu();
        if (algorithmsBox.isVisible()) {
            algorithmsBox.setVisible(false);
        }
        emergencyBtn.setVisible(false);
        loginBtn.setText("Login");
    }

    public void changeFloorButtons(Path path) {
        resetFloorButtonBorders();
        //greyOutFloorButtons();
        String firstFloor = path.getNodes().get(0).getFloor();
        String lastFloor = path.getNodes().get(0).getFloor();
        setButtonBorderColor(getFloorButton(firstFloor), "GREEN");
        for (int i = 0; i < path.getNodes().size(); i++) {
            String currFloor = path.getNodes().get(i).getFloor();
            if (!currFloor.equals(lastFloor)) {
                if (!lastFloor.equals(firstFloor)) {
                    setButtonBorderColor(getFloorButton(lastFloor), "#606060");
                }
                lastFloor = currFloor;
            }
        }
        setButtonBorderColor(getFloorButton(lastFloor), "RED");
    }

    private JFXButton getFloorButton(String floor) {
        switch (floor) {
            case "03":
                return floor3;
            case "02":
                return floor2;
            case "01":
                return floor1;
            case "0G":
                return groundFloor;
            case "L1":
                return l1;
            default: // case "L2"
                return l2;
        }
    }


    /////////////////////////////////
    //                             //
    //          Hamburger          //
    //                             //
    /////////////////////////////////

    public void resetFloorButtonBorders() {
        setAllFloorButtonBorders("#042E58");
    }

    private void setAllFloorButtonBorders(String borderColor) {
        setButtonBorderColor(l2, borderColor);
        setButtonBorderColor(l1, borderColor);
        setButtonBorderColor(groundFloor, borderColor);
        setButtonBorderColor(floor1, borderColor);
        setButtonBorderColor(floor2, borderColor);
        setButtonBorderColor(floor3, borderColor);
    }

    private void resetFloorButtonBackgrounds() {
        setButtonBackgroundColor(l2, "#042E58");
        setButtonBackgroundColor(l1, "#042E58");
        setButtonBackgroundColor(groundFloor, "#042E58");
        setButtonBackgroundColor(floor1, "#042E58");
        setButtonBackgroundColor(floor2, "#042E58");
        setButtonBackgroundColor(floor3, "#042E58");
    }

    private void resetEditorButtonBackgrounds() {
        setButtonBackgroundColor(addNodeBtn, "#042E58");
        setButtonBackgroundColor(remNodeBtn, "#042E58");
        setButtonBackgroundColor(addEdgeBtn, "#042E58");
        setButtonBackgroundColor(remEdgeBtn, "#042E58");
        setButtonBackgroundColor(modifyBtn, "#042E58");
        setButtonBackgroundColor(dragBtn, "#042E58");
        setButtonBackgroundColor(panBtn, "#042E58");
    }

    private String modifyStyle(String style, String feature, String replace) {
        int index = style.indexOf(feature);
        if (index != -1) {
            String section = style.substring(index);
            String start = style.substring(0, index + feature.length());
            String end = section.substring(section.indexOf(";"));
            style = start + replace + end;
        } else {
            style = style + " " + feature + replace + ";";
        }
        return style;
    }

    private void setButtonBorderColor(JFXButton btn, String borderColor) {
        btn.setStyle(modifyStyle(btn.getStyle(), "-fx-border-color: ", borderColor));
    }

    private void setButtonBackgroundColor(JFXButton btn, String backgroundColor) {
        btn.setStyle(modifyStyle(btn.getStyle(), "-fx-background-color: ", backgroundColor));
    }

    /////////////////////////////
    //                         //
    //       Directions        //
    //                         //
    /////////////////////////////

    private void highlightFloorTraversal(String currFloor) {
        for (javafx.scene.Node node : floorTraversal.getChildren()) {
            if (node instanceof JFXButton) {
                JFXButton btn = (JFXButton) node;
                if (btn.getText().equals(currFloor)) {
                    setButtonBackgroundColor(btn, "#436282");
                } else {
                    setButtonBackgroundColor(btn, "#042E58");
                }
            }
        }
    }

    @FXML
    private void onHamburgerMenu() {
        if (menuDrawer.isHidden()) {
            menuDrawer.setDisable(false);
            menuDrawer.open();
            menuDrawer.toFront();
        }
        inboxNum.setText(Integer.toString(ServiceRequestSingleton.getInstance().numMessagesInInbox(PermissionSingleton.getInstance().getCurrUser())));
    }

    @FXML
    private void onArrowEvent() {
        if (directionsDrawer.isHidden()) {
            directionsDrawer.setDisable(false);
            directionsDrawer.open();
            directionsDrawer.toFront();
            destinationLocation.setText(searchLocation.getText());
        }
    }

    @FXML
    private void setCancelMenuEvent() {
        /*if (adminDrawer.isShown()) {
            adminDrawer.close();
            adminDrawer.toBack();
            adminDrawer.setDisable(true);
        } else if (guestDrawer.isShown()) {
            guestDrawer.close();
            guestDrawer.toBack();
            guestDrawer.setDisable(true);
        }*/
        if (menuDrawer.isShown()) {
            menuDrawer.close();
            menuDrawer.toBack();
            menuDrawer.setDisable(true);
        }
    }

    private void setGuestMenu() {
        adminBox.setVisible(false);
        guestBox.setVisible(true);
    }

    private void setAdminMenu() {
        mapEditorBtn.setVisible(true);
        editUsersBtn.setVisible(true);
        guestBox.setVisible(false);
        adminBox.setVisible(true);
        emergencyBtn.setVisible(true);
    }

    private void setStaffMenu() {
        mapEditorBtn.setVisible(false);
        editUsersBtn.setVisible(false);
        guestBox.setVisible(false);
        adminBox.setVisible(true);
    }

    @FXML
    void onSearchLocation() {
        if (!searchLocation.getText().equals("")) {
            HashSet<Node> n = map.getNodes(node -> node.getLongName().equals(searchLocation.getText()));
            if (n.size() < 1) {
                return;
            }

            Node selectedNode = n.iterator().next();
            mapViewElement.setSelectedNodeEnd(selectedNode);
            mapViewElement.changePathDestination(mapViewElement.getSelectedNodeEnd());
            map.setFloor(selectedNode.getFloor());
            onFloorRefresh();
        }
    }

    @FXML
    void switchLocations() {
        String temp = sourceLocation.getText();
        sourceLocation.setText(destinationLocation.getText());
        destinationLocation.setText(temp);

        Path newPath = mapViewElement.swapSrcAndDst();
        displayTextDirections(newPath);
        resetFloorButtonBorders();
        resetFloorButtonBackgrounds();
        changeFloorButtons(newPath);
        onPathsChanged(newPath.separateIntoFloors());

        map.setFloor(mapViewElement.getSelectedNodeStart().getFloor());
        onFloorRefresh();
    }

    @FXML
    void flipElevators() {
        if (elevatorBan.isVisible()) {
            elevatorBan.setVisible(false);
            map.enableElevators();
        } else {
            elevatorBan.setVisible(true);
            map.disableElevators();
        }
        onFloorRefresh();
    }


    //////////////////////////////////////////
    //                                      //
    //           Search Algorithm           //
    //                                      //
    //////////////////////////////////////////

    @FXML
    void flipStairs() {
        if (stairBan.isVisible()) {
            stairBan.setVisible(false);
            map.enableStairs();
        } else {
            stairBan.setVisible(true);
            map.disableStairs();
        }
        onFloorRefresh();
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

    private void displayTextDirections(Path route) {

        List<String> directions = route.makeTextDirections();

        txtDirections.getChildren().clear();

        if (directions.isEmpty()) {
            imgDistance.setVisible(false);
            imgTime.setVisible(false);
            txtDistance.setVisible(false);
            txtTime.setVisible(false);
        } else {
            imgDistance.setVisible(true);
            imgTime.setVisible(true);
            txtDistance.setVisible(true);
            txtTime.setVisible(true);
        }

        StringBuilder sb = new StringBuilder();
        int feet = 0;

        for (String text : directions) {

            if (sb.length() > 0) {
                sb.append("\n");
            }
            sb.append(text);

            if (text.contains("Level")) {
                switch (text) {
                    case "Level 03":
                        text = "Level 3";
                        break;
                    case "Level 02":
                        text = "Level 2";
                        break;
                    case "Level 01":
                        text = "Level 1";
                        break;
                    case "Level 0G":
                        text = "Ground Level";
                        break;
                    case "Level L1":
                        text = "Lower Level 1";
                        break;
                    default:
                        text = "Lower Level 2";
                        break;
                }
                Text direction = new Text("                " + text + "\n ");
                direction.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                direction.setFill(Color.WHITE);
                txtDirections.getChildren().add(direction);
                continue;
            } else if (text.toLowerCase().contains("straight")) {
                FontAwesomeIconView arrow = new FontAwesomeIconView(FontAwesomeIcon.LONG_ARROW_UP);
                arrow.setGlyphSize(15);
                arrow.setScaleX(1.5);
                arrow.setFill(Color.WHITE);
                txtDirections.getChildren().add(arrow);
            } else if (text.toLowerCase().contains("left")) {
                FontAwesomeIconView arrow = new FontAwesomeIconView(FontAwesomeIcon.REPLY);
                arrow.setGlyphSize(15);
                arrow.setFill(Color.WHITE);
                txtDirections.getChildren().add(arrow);
            } else if (text.toLowerCase().contains("right")) {
                FontAwesomeIconView arrow = new FontAwesomeIconView(FontAwesomeIcon.SHARE);
                arrow.setGlyphSize(15);
                arrow.setFill(Color.WHITE);
                txtDirections.getChildren().add(arrow);
            } else if (text.toLowerCase().contains("take elevator up")) {
                ImageView elevator = new ImageView(new Image("edu/wpi/cs3733d18/teamF/up-elevator.png", 20, 20, true, true));
                txtDirections.getChildren().add(elevator);
            } else if (text.toLowerCase().contains("take elevator down")) {
                ImageView elevator = new ImageView(new Image("edu/wpi/cs3733d18/teamF/down-elevator.png", 20, 20, true, false));
                txtDirections.getChildren().add(elevator);
            } else if (text.toLowerCase().contains("take stairs up")) {
                ImageView stairs = new ImageView(new Image("edu/wpi/cs3733d18/teamF/up-stairs.png", 20, 20, true, true));
                txtDirections.getChildren().add(stairs);
            } else if (text.toLowerCase().contains("take stairs down")) {
                ImageView stairs = new ImageView(new Image("edu/wpi/cs3733d18/teamF/down-stairs.png", 20, 20, true, true));
                txtDirections.getChildren().add(stairs);
            } else if (text.toLowerCase().contains("begin")) {
                ImageView pin = new ImageView(new Image("edu/wpi/cs3733d18/teamF/start-icon.png", 20, 20, true, true));
                txtDirections.getChildren().add(pin);
            } else if (text.toLowerCase().contains("arrive")) {
                ImageView pin = new ImageView(new Image("edu/wpi/cs3733d18/teamF/end-icon.png", 20, 20, true, true));
                txtDirections.getChildren().add(pin);
            } else {
                ImageView eevee = new ImageView(new Image("edu/wpi/cs3733d18/teamF/Eevee.png", 20, 20, true, true));
                txtDirections.getChildren().add(eevee);
            }

            Text direction = new Text(" " + text + "\n ");
            direction.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            direction.setFill(Color.WHITE);
            txtDirections.getChildren().add(direction);

            feet += findDistance(direction.getText());
        }

        txtDistance.setText(feet + " feet");
        double time = ((int) (10 * feet / 246.6)) / 10.0;
        txtTime.setText(time + " minutes"); // average walking speed of 4.11 feet per second
        qrConverter qr = new qrConverter(sb.toString());
        qrImage.getChildren().add(qr.getQrView());
    }

    private int findDistance(String direction) {
        if (direction.contains("feet")) {
            String number = direction.substring(0, direction.indexOf("feet") - 1);
            int i;
            for (i = number.length() - 1; i >= 0; i--) {
                if (number.charAt(i) == ' ') {
                    break;
                }
            }
            number = number.substring(i + 1);
            return Integer.parseInt(number);
        } else {
            return 0;
        }
    }

    @FXML
    private void onCancelDirectionsEvent() {
        if (directionsDrawer.isShown()) {
            directionsDrawer.close();
            directionsDrawer.toBack();
            directionsDrawer.setDisable(true);
            searchLocation.setText(destinationLocation.getText());
        }
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

            Path newPath = mapViewElement.changePath(sourceNode, destinationNode);

            displayTextDirections(newPath);
            resetFloorButtonBorders();
            changeFloorButtons(newPath);

            map.setFloor(sourceNode.getFloor());
            onFloorRefresh();
        }
    }


    ///////////////////////////
    //                       //
    //       Help Pane       //
    //                       //
    ///////////////////////////

    @FXML
    private void onChangeAlgorithm() {
        algorithmsBox.setVisible(true);
    }

    @FXML
    private void onCloseAlgorithm() {
        algorithmsBox.setVisible(false);
    }

    ////////////////////////////
    //                        //
    //       About Pane       //
    //                        //
    ////////////////////////////

    @FXML
    private void onAStar() {
        MapSingleton.getInstance().getMap().setPathSelector(new AStar());
        aStar.setStyle("-fx-background-color: #303030");
        breathFirst.setStyle("-fx-background-color: #616161");
        depthFirst.setStyle("-fx-background-color: #616161");
        dijkstra.setStyle("-fx-background-color: #616161");
        bestFirst.setStyle("-fx-background-color: #616161");
    }

    //////////////////////////////////////////////
    //                                          //
    //     Search Service Request Functions     //
    //                                          //
    //////////////////////////////////////////////

    @FXML
    private void onBreathFirst() {
        MapSingleton.getInstance().getMap().setPathSelector(new BreathSearch());
        aStar.setStyle("-fx-background-color: #616161");
        breathFirst.setStyle("-fx-background-color: #303030");
        depthFirst.setStyle("-fx-background-color: #616161");
        dijkstra.setStyle("-fx-background-color: #616161");
        bestFirst.setStyle("-fx-background-color: #616161");
    }


    /////////////////////////////
    //                         //
    //       Languages         //
    //                         //
    /////////////////////////////

    @FXML
    private void onDepthFirst() {
        MapSingleton.getInstance().getMap().setPathSelector(new DepthSearch());
        aStar.setStyle("-fx-background-color: #616161");
        breathFirst.setStyle("-fx-background-color: #616161");
        depthFirst.setStyle("-fx-background-color: #303030");
        dijkstra.setStyle("-fx-background-color: #616161");
        bestFirst.setStyle("-fx-background-color: #616161");
    }

    @FXML
    private void onDijkstra() {
        MapSingleton.getInstance().getMap().setPathSelector(new Dijkstra());
        aStar.setStyle("-fx-background-color: #616161");
        breathFirst.setStyle("-fx-background-color: #616161");
        depthFirst.setStyle("-fx-background-color: #616161");
        dijkstra.setStyle("-fx-background-color: #303030");
        bestFirst.setStyle("-fx-background-color: #616161");
    }

    @FXML
    private void onBestFirst() {
        MapSingleton.getInstance().getMap().setPathSelector(new BestFirst());
        aStar.setStyle("-fx-background-color: #616161");
        breathFirst.setStyle("-fx-background-color: #616161");
        depthFirst.setStyle("-fx-background-color: #616161");
        dijkstra.setStyle("-fx-background-color: #616161");
        bestFirst.setStyle("-fx-background-color: #303030");
    }


    /////////////////////
    //                 //
    //       Map       //
    //                 //
    /////////////////////

    @FXML
    void onHelpPopup() {
        helpPane.setVisible(true);
    }

    @FXML
    void onCancelClose() {
        helpPane.setVisible(false);
    }

    @FXML
    void onAboutPopup() {
        aboutElement.showElement();
    }


    //////////////////////////////
    //                          //
    //       Map Builder        //
    //                          //
    //////////////////////////////

    private void onEnglish() {
        switcher.switchResource(ResourceBundle.getBundle("LanguageBundle", new Locale("en", "US")),
                Screens.Home);
    }

    private void onFrench() {
        switcher.switchResource(ResourceBundle.getBundle("LanguageBundle", new Locale("fr", "FR")),
                Screens.Home);
    }

    private void onSpanish() {
        switcher.switchResource(ResourceBundle.getBundle("LanguageBundle", new Locale("es", "ES")),
                Screens.Home);
    }

    private void onChinese() {
        switcher.switchResource(ResourceBundle.getBundle("LanguageBundle", new Locale("zh", "CN"), new UTF8Control()),
                Screens.Home);
    }

    @FXML
    void on2D() {
        map.setIs2D(true);
        onFloorRefresh();
        btn2D.setButtonType(JFXButton.ButtonType.RAISED);
        btn2D.setStyle("-fx-background-color:  #f2f5f7");
        btn3D.setButtonType(JFXButton.ButtonType.FLAT);
        btn3D.setStyle("-fx-background-color:  #b6b8b9");
    }

    @FXML
    void on3D() {
        map.setIs2D(false);
        onFloorRefresh();
        btn2D.setButtonType(JFXButton.ButtonType.FLAT);
        btn2D.setStyle("-fx-background-color:  #b6b8b9");
        btn3D.setButtonType(JFXButton.ButtonType.RAISED);
        btn3D.setStyle("-fx-background-color:  #f2f5f7");
    }


    ////////////////////////////////
    //                            //
    //       New/Edit User        //
    //                            //
    ////////////////////////////////

    @FXML
    public void onTimeoutChanged() {
        screensaver.changeTimeout((int) (sliderTimeout.getValue() * 1000));
    }

    public void changeFloor(String floor) {
        resetFloorButtonBackgrounds();

        map.setFloor(floor);
        JFXButton floorBtn = getFloorButton(floor);
        setButtonBackgroundColor(floorBtn, "#436282");

        highlightFloorTraversal(floorBtn.getText());

        switch (floor) {
            case "03":
                MainTitle.setText("Brigham and Women's Hospital: Level 3");
                break;
            case "02":
                MainTitle.setText("Brigham and Women's Hospital: Level 2");
                break;
            case "01":
                MainTitle.setText("Brigham and Women's Hospital: Level 1");
                break;
            case "0G":
                MainTitle.setText("Brigham and Women's Hospital: Ground Floor");
                break;
            case "L1":
                MainTitle.setText("Brigham and Women's Hospital: Lower Level 1");
                break;
            case "L2":
                MainTitle.setText("Brigham and Women's Hospital: Lower Level 2");
                break;
        }
    }

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
    }

    @FXML
    void onAddLocationCancel() {
        addLocationPopup.setVisible(false);
        newNode_shortName.setText("");
    }

    @FXML
    void onModificationCancel() {
        gpaneNodeInfo.setVisible(false);
    }

    @FXML
    void onNodeModify() {
        Node node = mapViewElement.getModifyNode();

        if (map.is2D()) {
            node.setPosition(new Point2D(Double.parseDouble(modNode_x.getText())
                    , Double.parseDouble(modNode_y.getText())));
        } else {
            node.setWireframePosition(new Point2D(Double.parseDouble(modNode_x.getText())
                    , Double.parseDouble(modNode_y.getText())));
        }
        node.setShortName(modNode_shortName.getText());
        node.setLongName(modNode_longName.getText());

        String newType = modNode_type.getSelectionModel().getSelectedItem().toString();
        node.setBuilding(modNode_building.getSelectionModel().getSelectedItem().toString());

        if (node.getNodeType().equals("ELEV") || node.getNodeType().equals("STAI")
                || newType.equals("ELEV") || newType.equals("STAI")) {
            return;
        }

        int typeCount = 0;
        try {
            typeCount = map.getNodes()
                    .stream()
                    .filter(n -> n.getNodeType().equals(newType))
                    .map(n -> n.getNodeID().substring(5, 8))
                    .map(Integer::parseInt)
                    .max(Integer::compare)
                    .get();
            typeCount++;
        } catch (Exception e) {
        }
        node.setNodeType(newType, typeCount);
    }

    @FXML
    public void onMapEditor() {
        onCancelDirectionsEvent();
        setCancelMenuEvent();
        mapViewElement.toggleEditorMode();
        if (mapEditorDrawer.isShown()) {
            mapEditorDrawer.close();
            mapEditorDrawer.setDisable(true);
        } else {
            mapEditorDrawer.open();
            mapEditorDrawer.setDisable(false);
        }
    }

    @FXML
    private void onCancelEditUser() {
        editUserPane.setVisible(false);
        try {
            searchUserResultTable.getItems().clear();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    ////////////////////////////////////////
    //                                    //
    //       Service Request              //
    //                                    //
    ////////////////////////////////////////

    @FXML
    private void onNewUserEvent() {
        userLabel.setText("New User");
        usernameField.setEditable(true);
        newUser = true;
        usernameField.clear();
        passwordField.clear();
        fnameField.clear();
        lnameField.clear();
        faceIDField.clear();
        occupationField.clear();
        languageCheck.setSelected(false);
        religiousCheck.setSelected(false);
        securityCheck.setSelected(false);
        maintenanceCheck.setSelected(false);
        privilegeCombo.getSelectionModel().clearSelection();
        editUserPane.setVisible(false);
        newUserPane.setVisible(true);
    }

    private void displayInUserTable(ArrayList<User> users) {
        if (users.size() < 1) {
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

        Callback<TableColumn<User, String>, TableCell<User, String>> cellFactory =
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

    public void onSelectUser(User e) {
        usernameField.setEditable(false);
        userLabel.setText("Edit User");
        editedUser = e;
        newUser = false;
        usernameField.setText(e.getUname());
        passwordField.clear();
        fnameField.setText(e.getFirstName());
        lnameField.setText(e.getLastName());
        faceIDField.setText(e.getFaceID());
        occupationField.setText(e.getOccupation());
        privilegeCombo.getSelectionModel().select(e.getPrivilege());


        if (ServiceRequestSingleton.getInstance().isInTable(e.getUname(), "LanguageInterpreter")) {
            languageCheck.setSelected(true);
        } else {
            languageCheck.setSelected(false);
        }
        if (ServiceRequestSingleton.getInstance().isInTable(e.getUname(), "ReligiousServices")) {
            religiousCheck.setSelected(true);
        } else {
            religiousCheck.setSelected(false);
        }
        if (ServiceRequestSingleton.getInstance().isInTable(e.getUname(), "SecurityRequest")) {
            securityCheck.setSelected(true);
        } else {
            securityCheck.setSelected(false);
        }
        if (ServiceRequestSingleton.getInstance().isInTable(e.getUname(), "MaintenanceRequest")) {
            maintenanceCheck.setSelected(true);
        } else {
            maintenanceCheck.setSelected(false);
        }

        editUserPane.setVisible(false);
        newUserPane.setVisible(true);
    }

    @FXML
    public void onEditUsers() {
        onCancelDirectionsEvent();
        setCancelMenuEvent();
        userTextField.clear();
        searchUserResultTable.getItems().clear();
        displayInUserTable(DatabaseWrapper.allUsers());
        editUserPane.setVisible(true);
    }

    @FXML
    private void onSubmitUser() {
        String username;
        String password;
        String firstName = fnameField.getText();
        String lastName = lnameField.getText();
        String occupation = occupationField.getText();
        String faceID = faceIDField.getText();
        boolean languageServices = languageCheck.isSelected();
        boolean religiousServices = religiousCheck.isSelected();
        boolean securityRequest = securityCheck.isSelected();
        boolean maintenanceRequest = maintenanceCheck.isSelected();

        User temp;

        if (newUser) {
            username = usernameField.getText();
            password = passwordField.getText();
            temp = new User(username, password, firstName, lastName, privilegeChoice, occupation, faceID);
            PermissionSingleton.getInstance().addUser(temp);
        } else {
            username = editedUser.getUname();
            if (passwordField.getText().equals("")) {
                password = editedUser.getPsword();
                temp = new User(username, password, firstName, lastName, privilegeChoice, occupation, faceID, true);
            } else {
                password = passwordField.getText();
                temp = new User(username, password, firstName, lastName, privilegeChoice, occupation, faceID);
            }
            PermissionSingleton.getInstance().updateUser(temp);
        }


        if (languageServices && !ServiceRequestSingleton.getInstance().isInTable(username, "LanguageInterpreter")) {
            ServiceRequestSingleton.getInstance().addUsernameLanguageInterpreter(username);
        } else if (!languageServices && ServiceRequestSingleton.getInstance().isInTable(username, "LanguageInterpreter")) {
            ServiceRequestSingleton.getInstance().removeUsernameLanguageInterpreter(username);
        }

        if (religiousServices && !ServiceRequestSingleton.getInstance().isInTable(username, "ReligiousServices")) {
            ServiceRequestSingleton.getInstance().addUsernameReligiousServices(username);
        } else if (!religiousServices && ServiceRequestSingleton.getInstance().isInTable(username, "ReligiousServices")) {
            ServiceRequestSingleton.getInstance().removeUsernameReligiousServices(username);
        }

        if (securityRequest && !ServiceRequestSingleton.getInstance().isInTable(username, "SecurityRequest")) {
            ServiceRequestSingleton.getInstance().addUsernameSecurityRequest(username);
        } else if (!securityRequest && ServiceRequestSingleton.getInstance().isInTable(username, "SecurityRequest")) {
            ServiceRequestSingleton.getInstance().removeUsernameSecurityRequest(username);
        }

        if (maintenanceRequest && !ServiceRequestSingleton.getInstance().isInTable(username, "MaintenanceRequest")) {
            ServiceRequestSingleton.getInstance().addUsernameMaintenanceRequest(username);
        } else if (!maintenanceRequest && ServiceRequestSingleton.getInstance().isInTable(username, "MaintenanceRequest")) {
            ServiceRequestSingleton.getInstance().removeUsernameMaintenanceRequest(username);
        }

        newUserPane.setVisible(false);
        onEditUsers();
    }

    @FXML
    private void onPrivilegeBox() {
        try {
            privilegeChoice = privilegeCombo.getSelectionModel().getSelectedItem().toString();
        } catch (NullPointerException e) {
            privilegeChoice = "Guest";
        }
    }

    @FXML
    public void onCancelUser() {
        newUserPane.setVisible(false);
        editUserPane.setVisible(true);
        onEditUsers();
    }

    @FXML
    private void onServiceRequest() {
        switcher.popupSR(Screens.MainPage);
    }

    @Override
    public void onNewPathSelected(Path path) {
        displayTextDirections(path);
        resetFloorButtonBorders();
        changeFloorButtons(path);
        floorNode.animateList(true);

        String hallFreeStart = mapViewElement.getSelectedNodeStart().getLongName();
        if (mapViewElement.getSelectedNodeStart().getNodeType().equals("HALL")) {
            hallFreeStart = "Hallway";
        }

        String hallFreeEnd = mapViewElement.getSelectedNodeEnd().getLongName();
        if (mapViewElement.getSelectedNodeEnd().getNodeType().equals("HALL")) {
            hallFreeEnd = "Hallway";
        }
        sourceLocation.setText(hallFreeStart);
        searchLocation.setText(hallFreeEnd);
        destinationLocation.setText(hallFreeEnd);
    }

    @Override
    public void onNewDestinationNode(Node node) {
        String hallFree = node.getLongName();
        if (node.getNodeType().equals("HALL")) {
            hallFree = "Hallway";
        }
        searchLocation.setText(hallFree);
        destinationLocation.setText(hallFree);
    }

    @Override
    public void onUpdateModifyNodePane(boolean isHidden, boolean is2D, Node modifiedNode) {
        if (isHidden) {
            gpaneNodeInfo.setVisible(false);
            return;
        }
        gpaneNodeInfo.setVisible(true);

        if (modifiedNode == null) {
            return;
        }
        if (map.is2D()) {
            modNode_x.setText(String.valueOf(modifiedNode.getPosition().getX()));
            modNode_y.setText(String.valueOf(modifiedNode.getPosition().getY()));
        } else {
            modNode_x.setText(String.valueOf(modifiedNode.getWireframePosition().getX()));
            modNode_y.setText(String.valueOf(modifiedNode.getWireframePosition().getY()));
        }
        modNode_shortName.setText(modifiedNode.getShortName());
        modNode_longName.setText(modifiedNode.getLongName());

        modNode_type.getSelectionModel().select(modifiedNode.getNodeType());
        modNode_building.getSelectionModel().select(modifiedNode.getBuilding());


        if (modifiedNode.getNodeType().equals("ELEV")
                || modifiedNode.getNodeType().equals("STAI")) {
            // do not allow type modification
            modNode_type.setDisable(true);
        } else {
            modNode_type.setDisable(false);
        }
    }

    @Override
    public void onNewNodePopup(Point2D sceneLocation, Point2D nodeLocation) {
        addLocationPopup.setTranslateX(sceneLocation.getX() - 90);
        addLocationPopup.setTranslateY(sceneLocation.getY() - 400);
        newNode_x.setEditable(false);
        newNode_y.setEditable(false);
        newNode_x.setText("" + (int) nodeLocation.getX());
        newNode_y.setText("" + (int) nodeLocation.getY());
        addLocationPopup.setVisible(true);
    }

    @Override
    public void onHideNewNodePopup() {
        addLocationPopup.setVisible(false);
    }

    @Override
    public void onFloorRefresh() {
        mapViewElement.changePathDestination(mapViewElement.getSelectedNodeEnd());
        Path newPath = mapViewElement.getMapDrawController().getDrawnPath();
        displayTextDirections(newPath);
        resetFloorButtonBorders();
        resetFloorButtonBackgrounds();
        changeFloorButtons(newPath);
        changeFloor(map.getFloor());
    }

    @FXML
    private void onAddNode() {
        resetEditorButtonBackgrounds();
        setButtonBackgroundColor(addNodeBtn, "#436282");
        mapViewElement.setEditMode(MapViewElement.EditMode.ADDNODE);
    }

    @FXML
    private void onRemoveNode() {
        resetEditorButtonBackgrounds();
        setButtonBackgroundColor(remNodeBtn, "#436282");
        mapViewElement.setEditMode(MapViewElement.EditMode.REMNODE);
    }

    @FXML
    private void onAddEdge() {
        resetEditorButtonBackgrounds();
        setButtonBackgroundColor(addEdgeBtn, "#436282");
        mapViewElement.setEditMode(MapViewElement.EditMode.ADDEDGE);
    }

    @FXML
    private void onRemoveEdge() {
        resetEditorButtonBackgrounds();
        setButtonBackgroundColor(remEdgeBtn, "#436282");
        mapViewElement.setEditMode(MapViewElement.EditMode.REMEDGE);
    }

    @FXML
    private void onModify() {
        resetEditorButtonBackgrounds();
        setButtonBackgroundColor(modifyBtn, "#436282");
        mapViewElement.setEditMode(MapViewElement.EditMode.EDITNODE);
    }

    @FXML
    private void onDragNode() {
        resetEditorButtonBackgrounds();
        setButtonBackgroundColor(dragBtn, "#436282");
        mapViewElement.setEditMode(MapViewElement.EditMode.MOVENODE);
    }

    @FXML
    private void onPan() {
        resetEditorButtonBackgrounds();
        setButtonBackgroundColor(panBtn, "#436282");
        mapViewElement.setEditMode(MapViewElement.EditMode.PAN);
    }

    @Override
    public void onRefresh() {
        MapMementoSingleton.getInstance().returnToLastState();
    }

    @Override
    public void onPathsChanged(ArrayList<Path> floorPaths) {
        clearFloorTraversal();
        for (int i = 0; i < floorPaths.size(); i++) {
            String borderColor;
            if (i == 0) {
                borderColor = "GREEN";
            } else if (i == floorPaths.size() - 1) {
                borderColor = "RED";
            } else {
                borderColor = "#606060";
            }

            String floor = floorPaths.get(i).getNodes().get(0).getFloor();
            String finalFloor = floor;

            if (floor.charAt(0) == '0') {
                floor = floor.substring(1);
            }
            JFXButton currFloor = new JFXButton(floor);
            currFloor.setFont(Font.font("Arial", FontWeight.BOLD, 26));
            currFloor.setTextFill(Color.WHITE);
            currFloor.setPrefWidth(80);
            currFloor.setPrefHeight(80);
            currFloor.setStyle(String.format("-fx-background-color:  #042E58;" +
                    "-fx-background-radius: 100;" +
                    "-fx-border-radius: 100;" +
                    "-fx-border-width: 5px;" +
                    "-fx-border-color: %s", borderColor));

            int finalI = i;
            currFloor.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                changeFloor(finalFloor);
                mapViewElement.zoomToPath(finalI);
            });

            floorTraversal.getChildren().add(currFloor);
        }
        // highlight the current floor
        String floor = map.getFloor();
        if (floor.charAt(0) == '0') {
            floor = floor.substring(1);
        }
        highlightFloorTraversal(floor);
    }


    //////////////////////////////
    //                          //
    //       Google Maps        //
    //                          //
    //////////////////////////////

    public void clearFloorTraversal() {
        floorTraversal.getChildren().clear();
    }

    @Override
    public void onFloorRefreshButtons() {
        resetFloorButtonBorders();
        resetFloorButtonBackgrounds();
    }

    @FXML
    private void onChangeKioskLocation() {
        mapViewElement.updateHomeLocation();
    }

    @FXML
    public void onGoogleMaps() {
        toggleGoogleMap();
    }

    public void toggleGoogleMap() {
        if (isGoogleMapViewEnabled) {
            switcher.switchTo(Screens.Home);
            MapMementoSingleton.getInstance().returnToLastState();
            return;
        } else {
            MapMementoSingleton.getInstance().saveState();
            setGoogleMapViewEnabled(true);
        }

    }

    public void setGoogleMapViewEnabled(boolean enabled) {
        googleMapView.setDisable(!enabled);
        isGoogleMapViewEnabled = enabled;
        if (enabled) {
            configureMap();
        }
        googleMapView.setVisible(enabled);
        googleMapView.setMouseTransparent(!enabled);
        googleMapView.setPickOnBounds(enabled);
    }

    protected void configureMap() {
        MapOptions mapOptions = new MapOptions();
        mapOptions.center(new LatLong(42.336012, -71.106989))
                .mapType(MapTypeIdEnum.HYBRID)
                .mapTypeControl(false)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(18);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLong(42.336012, -71.106989))
                .visible(Boolean.TRUE)
                .title("Brigham and Women's Hospital");

        gmap = googleMapView.createMap(mapOptions, false);

        Marker marker = new Marker(markerOptions);
        gmap.addMarker(marker);
    }

    @FXML
    private void onInbox() {

    }

    @FXML
    private void onExitInbox() {
        inboxNum.setText(Integer.toString(ServiceRequestSingleton.getInstance().numMessagesInInbox(PermissionSingleton.getInstance().getCurrUser())));
    }

    private void createMessage(ServiceRequests request) {
        Pane pane = new Pane();
        pane.setPrefSize(200, 100);
        pane.setStyle("-fx-background-color: WHITE; -fx-background-radius: 30");
        FontAwesomeIconView iconType = new FontAwesomeIconView();
        pane.getChildren().add(iconType);
        switch (request.getType()) {
            case "Language Interpreter":
                iconType.setGlyphName("LANGUAGE");
                break;
            case "Religious Services":
                iconType.setGlyphName("BOOK");
                break;
            case "Security Request":
                iconType.setGlyphName("SHIELD");
                break;
            case "Maintenance Request":
                iconType.setGlyphName("WRENCH");
                break;
        }
        iconType.setGlyphSize(15);
        iconType.setLayoutX(23);
        iconType.setLayoutY(28);

        Label requestType = new Label();
        requestType.setText(request.getType());
        pane.getChildren().add(requestType);
        requestType.setLayoutX(68);
        requestType.setLayoutY(14);

        Label requestID = new Label();
        requestID.setText(Integer.toString(request.getId()));
        pane.getChildren().add(requestID);
        requestType.setLayoutX(170);
        requestType.setLayoutY(14);

        Label requestPriority = new Label();
        requestPriority.setText(Integer.toString(request.getPriority()));
        pane.getChildren().add(requestPriority);
        requestType.setLayoutX(21);
        requestType.setLayoutY(50);

        Label requestLocation = new Label();
        requestLocation.setText(request.getLocation());
        pane.getChildren().add(requestLocation);
        requestType.setLayoutX(84);
        requestType.setLayoutY(50);

        pane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            onSelectInboxMessage(request);
        });
    }

    public void onSelectInboxMessage(ServiceRequests request) {

    }


    public void resetHomeController(){
        if(floorNode.isExpanded()){
            floorNode.animateList();
        }
        if(languageNode.isExpanded()){
            languageNode.animateList();
        }
        if(!MapSingleton.getInstance().getMap().is2D()){
            MapSingleton.getInstance().getMap().setIs2D(true);
        }
        onLogOutBtn();
    }

    @FXML
    public void onAllFloors() {
        mapViewElement.update3DPathDisplay(allFloors.isSelected());
    }
}
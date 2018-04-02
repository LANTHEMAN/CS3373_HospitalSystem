package com.github.CS3733_D18_Team_F_Project_0.controller.page;

import com.github.CS3733_D18_Team_F_Project_0.Map;
import com.github.CS3733_D18_Team_F_Project_0.MapSingleton;
import com.github.CS3733_D18_Team_F_Project_0.controller.PaneSwitcher;
import com.github.CS3733_D18_Team_F_Project_0.controller.Screens;
import com.github.CS3733_D18_Team_F_Project_0.controller.SwitchableController;
import com.github.CS3733_D18_Team_F_Project_0.controller.UTF8Control;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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

    @Override
    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;
        map = MapSingleton.getInstance().getMap();

        /*
        Pane root = switcher.getPane(Screens.Home);
        root.getChildren().add(new Button("Hello World"));
        */

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

        ivMap.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // only adds one location at a time
                if (addLocationPopup.isVisible() == false) {
                    addLocationPopup.setTranslateX(mouseEvent.getX() + 10);
                    addLocationPopup.setTranslateY(mouseEvent.getY() - 180);
                    txtXPos.setText("" + (int) mouseEvent.getX());
                    txtYPos.setText("" + (int) mouseEvent.getY());
                    addLocationPopup.setVisible(true);
                }
            }
        });
    }


    // Popup upon login

    @FXML
    void onLoginPopup() {
        switcher.popup(Screens.Login);
    }


    // Menus on right

    @FXML
    void onDirectionsSwitch() {
    }

    @FXML
    void onFloorSwitch() {
        switcher.switchTo(Screens.Floor);
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


        // Zooming in and out

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

    /*@FXML
    void onLocationSelection(ActionEvent e) {
        if (e.getSource().equals(locations)){
            String output = (String) locations.getValue();
            System.out.println(output);
            String output2 = (String) locations.getPromptText();
            System.out.println(output2);
        }
    }*/

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


    // Add Location

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
        // find directions with this location
        // TODO need to set field of txtfield first!!!!! (like putting in register before pass)
        onDirectionsSwitch();
    }
}
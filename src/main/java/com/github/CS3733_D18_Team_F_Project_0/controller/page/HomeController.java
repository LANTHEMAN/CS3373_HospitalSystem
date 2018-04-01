package com.github.CS3733_D18_Team_F_Project_0.controller.page;

import com.github.CS3733_D18_Team_F_Project_0.Map;
import com.github.CS3733_D18_Team_F_Project_0.MapSingleton;
import com.github.CS3733_D18_Team_F_Project_0.controller.PaneSwitcher;
import com.github.CS3733_D18_Team_F_Project_0.controller.Screens;
import com.github.CS3733_D18_Team_F_Project_0.controller.SwitchableController;
import com.github.CS3733_D18_Team_F_Project_0.controller.UTF8Control;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.Locale;
import java.util.ResourceBundle;

public class HomeController implements SwitchableController {

    private PaneSwitcher switcher;
    private Map map;

    @FXML
    public Button DirectionsSwitch;
    @FXML
    public ComboBox locations;
    @FXML
    private ImageView ivMap;
    @FXML
    private VBox vbxMenu;
    @FXML
    private VBox vbxLocation;

    @Override
    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;

        locations.getItems().clear();
        locations.getItems().addAll(
                "All",
                "Option 1",
                "Option 2",
                "Option 3");
        locations.getSelectionModel().selectFirst();

        map = MapSingleton.getInstance().getMap();
    }

    @FXML
    void onLoginPopup() {
        switcher.popup(Screens.Login);
    }


    @FXML
    void onNavigationSwitch() {
    }

    @FXML
    void onDirectionsSwitch() {

    }

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
    void onFloorSwitch() {
        switcher.switchTo(Screens.Floor);
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

    /*@FXML
    void onLocationSelection(ActionEvent e) {
        if (e.getSource().equals(locations)){
            String output = (String) locations.getValue();
            System.out.println(output);
            String output2 = (String) locations.getPromptText();
            System.out.println(output2);
        }
    }*/

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
    void onServiceRequest() {
        switcher.switchTo(Screens.ServiceRequest);
    }

    @FXML
    void onAddLocationConfirm(){

    }
    @FXML
    void onAddLocationCancel(){

    }

}
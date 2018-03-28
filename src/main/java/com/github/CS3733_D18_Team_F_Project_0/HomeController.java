package com.github.CS3733_D18_Team_F_Project_0;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;

import java.awt.event.ActionEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import java.io.IOException;

public class HomeController implements SwitchableController {

    private PaneSwitcher switcher;

    @FXML
    private ImageView ivMap;

    @FXML
    public Button DirectionsSwitch;

    @FXML
    public ComboBox locations;

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
    }

    @FXML
    void onLoginSwitch() {
        switcher.switchTo(Screens.Login);
        /*
        // popup pane attempt
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        //dialog.initOwner(primaryStage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text("This is a Dialog"));
        Scene dialogScene = new Scene(Screens.Login.fxmlFile);
        dialog.setScene(dialogScene);
        dialog.show();
        */
    }

    @FXML
    void onNavigationSwitch() {
        switcher.switchTo(Screens.Example);
    }

    @FXML
    void onDirectionsSwitch() {
        
    }

    @FXML
    void onEnglish(){
        switcher.switchResource(ResourceBundle.getBundle("LanguageBundle", new Locale("en", "US")),
                Screens.Home);
    }

    @FXML
    void onFrench(){
        switcher.switchResource(ResourceBundle.getBundle("LanguageBundle", new Locale("fr", "FR")),
                Screens.Home);
    }

    @FXML
    void onSpanish(){
        switcher.switchResource(ResourceBundle.getBundle("LanguageBundle", new Locale("es", "ES")),
                Screens.Home);
    }

    @FXML
    void onChinese(){
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

}

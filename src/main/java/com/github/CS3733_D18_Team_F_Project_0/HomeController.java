package com.github.CS3733_D18_Team_F_Project_0;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;

import java.io.IOException;

public class HomeController implements SwitchableController {

    private PaneSwitcher switcher;

    @FXML
    private ImageView ivMap;

    @FXML
    private ImageView ivZoomIn;

    @FXML
    private ImageView ivZoomOut;

    @FXML
    public Button DirectionsSwitch;

    @Override
    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;
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

}

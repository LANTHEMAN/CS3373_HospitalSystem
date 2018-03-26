package com.github.CS3733_D18_Team_F_Project_0;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController implements SwitchableController {

    private PaneSwitcher switcher;

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

}

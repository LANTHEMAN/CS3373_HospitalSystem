package edu.wpi.cs3733d18.teamF.controller.page.element.screensaver;

import edu.wpi.cs3733d18.teamF.controller.page.PageElement;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class Screensaver extends PageElement{

    @FXML
    AnchorPane root;

    public void initialize(AnchorPane sourcePane) {
        initElement(sourcePane, root);
    }

    public void start(){
        showElement();
        root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

    }

    public void stop(){
        hideElement();
    }

}
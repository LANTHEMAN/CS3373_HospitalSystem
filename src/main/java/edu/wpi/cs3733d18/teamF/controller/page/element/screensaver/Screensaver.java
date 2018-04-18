package edu.wpi.cs3733d18.teamF.controller.page.element.screensaver;

import edu.wpi.cs3733d18.teamF.controller.page.PageElement;
import edu.wpi.cs3733d18.teamF.gfx.impl.ScreensaverDrawer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;


public class Screensaver extends PageElement{

    @FXML
    AnchorPane root;

    ScreensaverDrawer screensaverDrawer;

    public void initialize(AnchorPane sourcePane) {
        initElement(sourcePane, root);

        root.addEventHandler(Event.ANY, e -> {
            stop();
        });

        screensaverDrawer = new ScreensaverDrawer();
    }

    public void start(){
        showElement();
        root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        screensaverDrawer.draw(root);
    }

    public void stop(){
        root.getChildren().clear();
        hideElement();
    }

}
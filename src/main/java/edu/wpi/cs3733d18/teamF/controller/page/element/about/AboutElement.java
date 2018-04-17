package edu.wpi.cs3733d18.teamF.controller.page.element.about;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import edu.wpi.cs3733d18.teamF.controller.page.PageElement;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class AboutElement extends PageElement {
    @FXML
    AnchorPane root;

    @FXML
    FontAwesomeIconView closeButton;

    public void initialize(AnchorPane sourcePane) {
        initElement(sourcePane, root);

        closeButton.setOnMouseClicked(e -> {
            hideElement();
        });
    }

}

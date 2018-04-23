package edu.wpi.cs3733d18.teamF.controller.page.element.about;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import edu.wpi.cs3733d18.teamF.controller.page.PageElement;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class AboutElement extends PageElement {
    @FXML
    AnchorPane root;

    @FXML
    FontAwesomeIconView closeButton;

    @FXML
    JFXButton creditsButton;

    @FXML
    FontAwesomeIconView closeCredits;

    @FXML
    AnchorPane creditsPage;

    public void initialize(AnchorPane sourcePane) {
        initElement(sourcePane, root);
        closeButton.setOnMouseClicked(e -> hideElement());
        creditsButton.setOnMouseClicked(e -> creditsPage.setVisible(true));
        closeCredits.setOnMouseClicked(e-> hideElement());
    }

    public void showElement() {
        super.showElement();
        creditsPage.setVisible(false);
    }
}
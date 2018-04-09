package edu.wpi.cs3733d18.teamF.controller.page;

import edu.wpi.cs3733d18.teamF.controller.PaneSwitcher;
import edu.wpi.cs3733d18.teamF.controller.PermissionSingleton;
import edu.wpi.cs3733d18.teamF.controller.Screens;
import edu.wpi.cs3733d18.teamF.controller.SwitchableController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class HelpController implements SwitchableController {

    private PaneSwitcher switcher;

    @FXML
    private GridPane userInstructions;
    @FXML
    private GridPane adminInstructions;

    @Override
    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;
        if (PermissionSingleton.getInstance().isAdmin()) {
            userInstructions.setVisible(false);
            adminInstructions.setVisible(true);
        } else {
            adminInstructions.setVisible(false);
            userInstructions.setVisible(true);
        }
    }

    @FXML
    void onCancelClose() {
        switcher.closePopup(Screens.Home);
    }
}

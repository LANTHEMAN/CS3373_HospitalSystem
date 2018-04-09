package com.github.CS3733_D18_Team_F_Project_0.controller.page;

import com.github.CS3733_D18_Team_F_Project_0.controller.PaneSwitcher;
import com.github.CS3733_D18_Team_F_Project_0.controller.PermissionSingleton;
import com.github.CS3733_D18_Team_F_Project_0.controller.Screens;
import com.github.CS3733_D18_Team_F_Project_0.controller.SwitchableController;
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

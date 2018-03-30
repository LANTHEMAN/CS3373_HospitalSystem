package com.github.CS3733_D18_Team_F_Project_0.controller.page;

import com.github.CS3733_D18_Team_F_Project_0.controller.PaneSwitcher;
import com.github.CS3733_D18_Team_F_Project_0.controller.Screens;
import com.github.CS3733_D18_Team_F_Project_0.controller.SwitchableController;
import javafx.fxml.FXML;

public class LoginController implements SwitchableController {

    private PaneSwitcher switcher;


    @Override
    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;
    }

    @FXML
    void onLoginClose() {
        switcher.closePopup(Screens.Home);
    }

    @FXML
    void onCancelClose() {
        switcher.closePopup(Screens.Home);
    }

}

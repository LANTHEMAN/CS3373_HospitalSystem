package com.github.CS3733_D18_Team_F_Project_0;

import javafx.fxml.FXML;

public class LoginController implements SwitchableController {

    private PaneSwitcher switcher;

    @Override
    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;
    }

    @FXML
    void onLoginSwitch() {
        switcher.switchTo(Screens.Home);
    }

    @FXML
    void onCancelSwitch() {
        switcher.switchTo(Screens.Home);
    }

}

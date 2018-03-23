package com.github.CS3733_D18_Team_F_Project_0;

import javafx.fxml.FXML;

public class ExampleController implements SwitchableController {
    private PaneSwitcher switcher;

    @Override
    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;
    }

    @FXML
    void onBackSwitch() {
        switcher.switchTo(Screens.Home);
    }
}

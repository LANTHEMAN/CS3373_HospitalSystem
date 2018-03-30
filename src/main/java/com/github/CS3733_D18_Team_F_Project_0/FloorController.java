package com.github.CS3733_D18_Team_F_Project_0;

import javafx.fxml.FXML;

public class FloorController implements SwitchableController {

    private PaneSwitcher switcher;


    @Override
    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;
    }


    @FXML
    void onCancelSwitch() {
        switcher.switchTo(Screens.Home);
    }

}

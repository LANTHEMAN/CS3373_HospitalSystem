package com.github.CS3733_D18_Team_F_Project_0.controller.page;

import com.github.CS3733_D18_Team_F_Project_0.controller.PaneSwitcher;
import com.github.CS3733_D18_Team_F_Project_0.controller.Screens;
import com.github.CS3733_D18_Team_F_Project_0.controller.SwitchableController;
import javafx.fxml.FXML;

public class SideMenuController implements SwitchableController {
    private PaneSwitcher switcher;

    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;
    }

    public void setSwitcher(PaneSwitcher switcher){
        this.switcher = switcher;
    }



}

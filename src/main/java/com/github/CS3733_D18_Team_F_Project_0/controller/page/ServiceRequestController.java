package com.github.CS3733_D18_Team_F_Project_0.controller.page;

import com.github.CS3733_D18_Team_F_Project_0.controller.PaneSwitcher;
import com.github.CS3733_D18_Team_F_Project_0.controller.Screens;
import com.github.CS3733_D18_Team_F_Project_0.controller.SwitchableController;
import javafx.fxml.FXML;

import java.awt.*;

public class ServiceRequestController implements SwitchableController {
    private PaneSwitcher switcher;


    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;
    }

    @FXML
    void onCancelSwitch() {
        switcher.switchTo(Screens.Home);
    }

    @FXML
    void onReligiousButtonSwitch(){
        switcher.switchTo(Screens.ReligiousServices);
    }
    @FXML
    void onLanguageInterpreterButtonSwitch(){
        switcher.switchTo(Screens.LanguageInterpreter);
    }
    @FXML
    void onSearchButtonSwitch(){
        switcher.switchTo(Screens.SearchServiceRequests);
    }
}


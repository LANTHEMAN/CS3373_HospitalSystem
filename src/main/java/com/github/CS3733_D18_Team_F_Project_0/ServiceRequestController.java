package com.github.CS3733_D18_Team_F_Project_0;

import javafx.fxml.FXML;

import java.awt.*;

public class ServiceRequestController implements SwitchableController {
    private PaneSwitcher switcher;

    @FXML
    public Button religiousServices;

    @FXML
    public Button languageInterpreter;

    @FXML
    public Button cancel;


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
        switcher.switchTo(Screens.LanguageInterpretor);
    }
}


package com.github.CS3733_D18_Team_F_Project_0.controller.page;

import com.github.CS3733_D18_Team_F_Project_0.controller.PaneSwitcher;
import com.github.CS3733_D18_Team_F_Project_0.controller.PermissionSingleton;
import com.github.CS3733_D18_Team_F_Project_0.controller.Screens;
import com.github.CS3733_D18_Team_F_Project_0.controller.SwitchableController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ServiceRequestController implements SwitchableController {
    private PaneSwitcher switcher;

    @FXML
    public Button searchBtn;



    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;

        if(PermissionSingleton.getInstance().isAdmin()){
            searchBtn.setVisible(true);
        }
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


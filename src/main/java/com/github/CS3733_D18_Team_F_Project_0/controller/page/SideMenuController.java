package com.github.CS3733_D18_Team_F_Project_0.controller.page;

import com.github.CS3733_D18_Team_F_Project_0.controller.PaneSwitcher;
import com.github.CS3733_D18_Team_F_Project_0.controller.Screens;
import com.github.CS3733_D18_Team_F_Project_0.controller.SwitchableController;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class SideMenuController implements SwitchableController {
    private PaneSwitcher switcher;

    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;
    }

    @FXML
    public void onServiceRequest(){
        switcher.switchTo(Screens.ServiceRequest);
    }

    @FXML
    public void onFindLocation(){
    }

    @FXML
    public void onGetDirections(){

    }

}

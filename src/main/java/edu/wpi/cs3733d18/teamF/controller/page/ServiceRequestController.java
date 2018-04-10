package edu.wpi.cs3733d18.teamF.controller.page;

import edu.wpi.cs3733d18.teamF.controller.PaneSwitcher;
import edu.wpi.cs3733d18.teamF.controller.PermissionSingleton;
import edu.wpi.cs3733d18.teamF.controller.Screens;
import edu.wpi.cs3733d18.teamF.controller.SwitchableController;
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
    @FXML
    void onSecurityButtonSwitch() { switcher.switchTo(Screens.SecurityRequest);}
}


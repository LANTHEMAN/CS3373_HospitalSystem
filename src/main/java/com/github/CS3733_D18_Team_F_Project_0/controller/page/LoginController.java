package com.github.CS3733_D18_Team_F_Project_0.controller.page;

import com.github.CS3733_D18_Team_F_Project_0.controller.PaneSwitcher;
import com.github.CS3733_D18_Team_F_Project_0.controller.PermissionSingleton;
import com.github.CS3733_D18_Team_F_Project_0.controller.Screens;
import com.github.CS3733_D18_Team_F_Project_0.controller.SwitchableController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginController implements SwitchableController {

    private PaneSwitcher switcher;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private Text error;

    @Override
    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;
    }

    @FXML
    void onLoginClose() {
        boolean test = PermissionSingleton.getInstance().login(username.getText(),password.getText());
        if(test){
            System.out.println("Login Successful");
            switcher.closePopup(Screens.Home);
        }
        else{
            System.out.println("Login Unsuccessful");
            error.setText("Incorrect username/password");
        }
    }

    @FXML
    void onCancelClose() {
        switcher.closePopup(Screens.Home);
    }

}

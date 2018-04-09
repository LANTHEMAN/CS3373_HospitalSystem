package edu.wpi.cs3733d18.teamF.controller.page;

import edu.wpi.cs3733d18.teamF.controller.PaneSwitcher;
import edu.wpi.cs3733d18.teamF.controller.PermissionSingleton;
import edu.wpi.cs3733d18.teamF.controller.Screens;
import edu.wpi.cs3733d18.teamF.controller.SwitchableController;
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

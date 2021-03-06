package edu.wpi.cs3733d18.teamF.controller.page;

import edu.wpi.cs3733d18.teamF.controller.ErrorSingleton;
import edu.wpi.cs3733d18.teamF.controller.PaneSwitcher;
import edu.wpi.cs3733d18.teamF.controller.SwitchableController;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.Label;

public class ErrorController implements SwitchableController{

    private PaneSwitcher switcher;
    @FXML
    public Text text1;

    @FXML
    public Label text2;


    @Override
    public void initialize(PaneSwitcher switcher) {
        if(ErrorSingleton.getInstance().getError()!= null) {
            text2.setText(ErrorSingleton.getInstance().getError().getMessage());
        }
        this.switcher = switcher;
    }

}

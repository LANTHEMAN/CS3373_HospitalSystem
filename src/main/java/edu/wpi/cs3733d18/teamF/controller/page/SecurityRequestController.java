package edu.wpi.cs3733d18.teamF.controller.page;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733d18.teamF.controller.PaneSwitcher;
import edu.wpi.cs3733d18.teamF.controller.SwitchableController;
import edu.wpi.cs3733d18.teamF.sr.SecurityRequest;
import edu.wpi.cs3733d18.teamF.sr.ServiceRequestSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;

public class SecurityRequestController implements SwitchableController {
    private PaneSwitcher switcher;

    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;
    }

    @FXML
    private JFXTextArea securityTextArea;
    @FXML
    private JFXTextField securityLocationField;
    @FXML
    private ToggleGroup securityToogle;
    @FXML
    private Label securityLocationRequired;

    @FXML
    private void onCancelSecurity(){

    }

    @FXML
    private void onSubmitSecurity(){
        if (securityLocationField.getText() == null || securityLocationField.getText().trim().isEmpty()) {
            securityLocationRequired.setVisible(true);
            return;
        }
        String location = securityLocationField.getText();
        String description = securityTextArea.getText();
        String status = "Incomplete";
        int priority = Integer.parseInt(securityToogle.getSelectedToggle().getUserData().toString());

        SecurityRequest sec = new SecurityRequest(location, description, status, priority);

        ServiceRequestSingleton.getInstance().sendServiceRequest(sec);
        ServiceRequestSingleton.getInstance().addServiceRequest(sec);
        //make pane invisible
    }
}

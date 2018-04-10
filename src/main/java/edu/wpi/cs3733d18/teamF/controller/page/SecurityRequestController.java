package edu.wpi.cs3733d18.teamF.controller.page;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733d18.teamF.controller.PaneSwitcher;
import edu.wpi.cs3733d18.teamF.controller.Screens;
import edu.wpi.cs3733d18.teamF.controller.SwitchableController;
import edu.wpi.cs3733d18.teamF.sr.LanguageInterpreter;
import edu.wpi.cs3733d18.teamF.sr.ServiceRequest;
import edu.wpi.cs3733d18.teamF.sr.ServiceRequestSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SecurityRequestController implements SwitchableController {
    private PaneSwitcher switcher;


    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;
    }

    @FXML
    RadioButton one;

    @FXML
    RadioButton two;

    @FXML
    RadioButton three;

    @FXML
    RadioButton four;

    @FXML
    RadioButton five;

    @FXML
    RadioButton six;

    @FXML
    RadioButton seven;

    @FXML
    JFXTextArea location;

    @FXML
    JFXTextArea specialInstructions;

    @FXML
    JFXButton cancel;

    @FXML
    JFXButton submitRequest;

/*

    @FXML
    void onCancel() {
        switcher.switchTo(Screens.ServiceRequest);
    }

    @FXML
    void onSubmit() {
        //TODO: create form from text fields and send to database
        int requiredFieldsEmpty = 0;
        String l;
        String first_name;
        String last_name;
        String location;
        String description;
        if (language.getText() == null || language.getText().trim().isEmpty()) {
            languageRequired.setVisible(true);
            requiredFieldsEmpty++;
        }
        if (firstName.getText() == null || firstName.getText().trim().isEmpty()) {
            firstNameRequired.setVisible(true);
            requiredFieldsEmpty++;
        }
        if (lastName.getText() == null || lastName.getText().trim().isEmpty()) {
            lastNameRequired.setVisible(true);
            requiredFieldsEmpty++;
        }
        if (destination.getText() == null || destination.getText().trim().isEmpty()) {
            locationRequired.setVisible(true);
            requiredFieldsEmpty++;
        }
        if (requiredFieldsEmpty > 0) {
            return;
        }

        if (instructions.getText() == null || instructions.getText().trim().isEmpty()) {
            description = "N/A";
        } else {
            description = instructions.getText();
        }
        l = language.getText();
        first_name = firstName.getText();
        last_name = lastName.getText();
        location = destination.getText();
        String new_description = l + "/////" + description;
        //System.out.print(new_description);
        ServiceRequest request = new LanguageInterpreter(first_name, last_name, location, new_description, "Incomplete", 1, l);
        ServiceRequestSingleton.getInstance().sendServiceRequest(request);
        ServiceRequestSingleton.getInstance().addServiceRequest(request);
        switcher.switchTo(Screens.ServiceRequest);
        */
}

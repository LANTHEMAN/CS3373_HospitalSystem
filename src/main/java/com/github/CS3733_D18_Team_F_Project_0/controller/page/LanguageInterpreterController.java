package com.github.CS3733_D18_Team_F_Project_0.controller.page;

import com.github.CS3733_D18_Team_F_Project_0.controller.PaneSwitcher;
import com.github.CS3733_D18_Team_F_Project_0.controller.Screens;
import com.github.CS3733_D18_Team_F_Project_0.controller.SwitchableController;
import com.github.CS3733_D18_Team_F_Project_0.sr.LanguageInterpreter;
import com.github.CS3733_D18_Team_F_Project_0.sr.ServiceRequest;
import com.github.CS3733_D18_Team_F_Project_0.sr.ServiceRequestSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;


public class LanguageInterpreterController implements SwitchableController {
    private PaneSwitcher switcher;


    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;
    }

    @FXML
    TextField language;

    @FXML
    TextField firstName;

    @FXML
    TextField lastName;

    @FXML
    TextField destination;

    @FXML
    TextArea instructions;

    @FXML
    Label languageRequired;

    @FXML
    Label firstNameRequired;

    @FXML
    Label lastNameRequired;

    @FXML
    Label locationRequired;


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

    }
}



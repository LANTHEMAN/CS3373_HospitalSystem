package com.github.CS3733_D18_Team_F_Project_0.controller.page;

import com.github.CS3733_D18_Team_F_Project_0.controller.PaneSwitcher;
import com.github.CS3733_D18_Team_F_Project_0.controller.Screens;
import com.github.CS3733_D18_Team_F_Project_0.controller.SwitchableController;
import com.github.CS3733_D18_Team_F_Project_0.sr.ReligiousServices;
import com.github.CS3733_D18_Team_F_Project_0.sr.ServiceRequestSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ReligiousServicesController implements SwitchableController {
    private PaneSwitcher switcher;


    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;
    }


    @FXML
    TextField religion;

    @FXML
    TextField firstName;

    @FXML
    TextField lastName;

    @FXML
    TextField location;

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
        String r;
        String first_name;
        String last_name;
        String destination;
        String description;
        if (religion.getText() == null || religion.getText().trim().isEmpty()) {
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
        if (location.getText() == null || location.getText().trim().isEmpty()) {
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
        r = religion.getText();
        first_name = firstName.getText();
        last_name = lastName.getText();
        destination = location.getText();
        String new_description = r + "/////" + description + "\n";
        System.out.print(new_description);
        ReligiousServices request = new ReligiousServices(first_name, last_name, destination, new_description, 0, r);
        //ServiceRequestSingleton.getInstance().sendServiceRequest(request);
        ServiceRequestSingleton.getInstance().addServiceRequest(request);
        switcher.switchTo(Screens.ServiceRequest);

    }
}


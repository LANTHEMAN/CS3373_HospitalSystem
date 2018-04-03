package com.github.CS3733_D18_Team_F_Project_0.controller.page;

import com.github.CS3733_D18_Team_F_Project_0.controller.PaneSwitcher;
import com.github.CS3733_D18_Team_F_Project_0.controller.Screens;
import com.github.CS3733_D18_Team_F_Project_0.controller.SwitchableController;
import com.github.CS3733_D18_Team_F_Project_0.sr.ServiceRequest;
import com.github.CS3733_D18_Team_F_Project_0.sr.ServiceRequestSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ServiceRequestPopUpController implements SwitchableController {
    private PaneSwitcher switcher;
    ServiceRequest s;
    boolean statusChange;
    String newStatus;


    @FXML
    public Label typeLabel;
    @FXML
    public Label idLabel;
    @FXML
    public Label firstNameLabel;
    @FXML
    public Label lastNameLabel;
    @FXML
    public Label locationLabel;
    @FXML
    public Label statusLabel;
    @FXML
    public TextArea instructionsTextArea;
    @FXML
    public ComboBox statusBox;


    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;
        s = ServiceRequestSingleton.getInstance().getPopUpRequest();
        typeLabel.setText("Type: "+s.getType());
        idLabel.setText("Service Request #"+s.getId());
        firstNameLabel.setText("First Name: "+s.getFirstName());
        lastNameLabel.setText("Last Name: "+s.getLastName());
        locationLabel.setText(s.getLocation());
        statusLabel.setText(s.getStatus());
        instructionsTextArea.setText(s.getDescription());
        statusChange = false;
        newStatus = "no";
        instructionsTextArea.setEditable(false);

        statusBox.getItems().addAll("Incomplete", "In Progress", "Complete");
    }

    public void onStatusBox(){
        try {
            if (statusBox.getSelectionModel().getSelectedItem().equals("Incomplete")) {
                newStatus = "Incomplete";
                statusChange = true;
            } else if (statusBox.getSelectionModel().getSelectedItem().equals("In Progress")) {
                newStatus = "In Progress";
                statusChange = true;
            } else if (statusBox.getSelectionModel().getSelectedItem().equals("Complete")) {
                newStatus = "Complete";
                statusChange = true;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void onCancel(){
        switcher.closePopup(Screens.SearchServiceRequests);
    }

    public void onSubmit(){
        if(statusChange && newStatus.compareTo(s.getStatus()) != 0){
            s.setStatus(newStatus);
            ServiceRequestSingleton.getInstance().updateStatus(s);
        }
        switcher.closePopup(Screens.SearchServiceRequests);
    }




}

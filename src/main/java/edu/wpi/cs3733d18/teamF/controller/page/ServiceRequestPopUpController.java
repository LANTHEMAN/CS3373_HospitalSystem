package edu.wpi.cs3733d18.teamF.controller.page;

import edu.wpi.cs3733d18.teamF.controller.PaneSwitcher;
import edu.wpi.cs3733d18.teamF.controller.PermissionSingleton;
import edu.wpi.cs3733d18.teamF.controller.Screens;
import edu.wpi.cs3733d18.teamF.controller.SwitchableController;
import edu.wpi.cs3733d18.teamF.sr.ServiceRequest;
import edu.wpi.cs3733d18.teamF.sr.ServiceRequestSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ServiceRequestPopUpController implements SwitchableController {
    private PaneSwitcher switcher;
    ServiceRequest serviceRequestPopUp;
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
    @FXML
    public Label completedByLabel;
    @FXML
    public Label usernameLabel;

    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;
        serviceRequestPopUp = ServiceRequestSingleton.getInstance().getPopUpRequest();
        typeLabel.setText("Type: "+serviceRequestPopUp.getType());
        idLabel.setText("Service Request #"+serviceRequestPopUp.getId());
        firstNameLabel.setText("First Name: "+serviceRequestPopUp.getFirstName());
        lastNameLabel.setText("Last Name: "+serviceRequestPopUp.getLastName());
        locationLabel.setText(serviceRequestPopUp.getLocation());
        statusLabel.setText(serviceRequestPopUp.getStatus());
        instructionsTextArea.setText(serviceRequestPopUp.getDescription());
        statusChange = false;
        newStatus = "no";
        instructionsTextArea.setEditable(false);

        statusBox.getItems().addAll("Incomplete", "In Progress", "Complete");

        if(serviceRequestPopUp.getStatus().equals("Complete")){
            completedByLabel.setVisible(true);
            usernameLabel.setVisible(true);
            usernameLabel.setText(serviceRequestPopUp.getCompletedBy());
        }
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

    public void onCancelEdit(){
        switcher.closePopup(Screens.SearchServiceRequests);
    }

    public void onSubmitEdit(){
        if(statusChange && newStatus.compareTo(serviceRequestPopUp.getStatus()) != 0){
            serviceRequestPopUp.setStatus(newStatus);
            if(newStatus.equals("Complete")){
                serviceRequestPopUp.setCompletedBy(PermissionSingleton.getInstance().getCurrUser());
                ServiceRequestSingleton.getInstance().updateCompletedBy(serviceRequestPopUp);
            }
            ServiceRequestSingleton.getInstance().updateStatus(serviceRequestPopUp);

        }
        switcher.closePopup(Screens.SearchServiceRequests);
    }




}

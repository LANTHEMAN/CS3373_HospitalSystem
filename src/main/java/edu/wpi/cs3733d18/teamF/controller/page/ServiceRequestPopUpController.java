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
    @FXML
    public Label completedByLabel;
    @FXML
    public Label usernameLabel;

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

        if(s.getStatus().equals("Complete")){
            completedByLabel.setVisible(true);
            usernameLabel.setVisible(true);
            usernameLabel.setText(s.getCompletedBy());
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

    public void onCancel(){
        switcher.closePopup(Screens.SearchServiceRequests);
    }

    public void onSubmit(){
        if(statusChange && newStatus.compareTo(s.getStatus()) != 0){
            s.setStatus(newStatus);
            if(newStatus.equals("Complete")){
                s.setCompletedBy(PermissionSingleton.getInstance().getCurrUser());
                ServiceRequestSingleton.getInstance().updateCompletedBy(s);
            }
            ServiceRequestSingleton.getInstance().updateStatus(s);

        }
        switcher.closePopup(Screens.SearchServiceRequests);
    }




}

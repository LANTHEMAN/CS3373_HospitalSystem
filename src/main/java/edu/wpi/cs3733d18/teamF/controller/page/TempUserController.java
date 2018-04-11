package edu.wpi.cs3733d18.teamF.controller.page;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733d18.teamF.controller.PaneSwitcher;
import edu.wpi.cs3733d18.teamF.controller.PermissionSingleton;
import edu.wpi.cs3733d18.teamF.controller.SwitchableController;
import edu.wpi.cs3733d18.teamF.controller.User;
import edu.wpi.cs3733d18.teamF.sr.ServiceRequestSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;


public class TempUserController implements SwitchableController {

    @FXML
    private Label userLabel;
    @FXML
    private JFXCheckBox languageCheck;
    @FXML
    private JFXCheckBox religiousCheck;
    @FXML
    private JFXCheckBox securityCheck;
    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXTextField passwordField;
    @FXML
    private JFXTextField fnameField;
    @FXML
    private JFXTextField lnameField;
    @FXML
    private JFXTextField occupationField;
    @FXML
    private JFXComboBox privilegeCombo;
    @FXML
    private AnchorPane newUserPane;
    String privilegeChoice;
    User editedUser;
    boolean newUser;


    public void initialize(PaneSwitcher switcher){
        privilegeCombo.getItems().addAll("Admin", "Staff");

        if(newUser){
            userLabel.setText("New User");
        }else{
            userLabel.setText("Edit User");
        }
    }

    @FXML
    private void onSubmitUser(){
        String username = editedUser.getUname();
        String password = editedUser.getPsword();
        String firstName = fnameField.getText();
        String lastName = lnameField.getText();
        String occupation = occupationField.getText();
        boolean languageServices = languageCheck.isSelected();
        boolean religiousServices = religiousCheck.isSelected();
        boolean securityRequest = securityCheck.isSelected();

        User temp = new User(username, password, firstName, lastName, privilegeChoice, occupation);
        if(newUser){
            PermissionSingleton.getInstance().addUser(temp);
        }else{
            PermissionSingleton.getInstance().removeUser(temp);
            PermissionSingleton.getInstance().addUser(temp);
        }

        if(ServiceRequestSingleton.getInstance().isInTable(username, "LanguageInterpreter")){
            if(!languageServices){
                ServiceRequestSingleton.getInstance().removeUsernameLanguageInterpreter(username);
            }
        }else{
            if(languageServices){
                ServiceRequestSingleton.getInstance().addUsernameLanguageInterpreter(username);
            }
        }

        if(ServiceRequestSingleton.getInstance().isInTable(username, "ReligiousServices")){
            if(!religiousServices){
                ServiceRequestSingleton.getInstance().removeUsernameReligiousServices(username);
            }
        }else{
            if(religiousServices){
                ServiceRequestSingleton.getInstance().addUsernameReligiousServices(username);
            }
        }

        if(ServiceRequestSingleton.getInstance().isInTable(username, "SecurityRequest")){
            if(!securityRequest){
                ServiceRequestSingleton.getInstance().removeUsernameSecurityRequest(username);
            }
        }else{
            if(securityRequest){
                ServiceRequestSingleton.getInstance().addUsernameSecurityRequest(username);
            }
        }
        newUserPane.setVisible(false);
    }

    @FXML
    private void onPrivilegeBox(){
        try {
            privilegeChoice = privilegeCombo.getSelectionModel().getSelectedItem().toString();
        } catch (NullPointerException e) {
            privilegeChoice = "Guest";
        }
    }

    @FXML
    public void onCancelUser(){
        newUserPane.setVisible(false);
    }
}

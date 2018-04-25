package edu.wpi.cs3733d18.teamF.controller.page.element;

import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import edu.wpi.cs3733d18.teamF.controller.PaneSwitcher;
import edu.wpi.cs3733d18.teamF.controller.PermissionSingleton;
import edu.wpi.cs3733d18.teamF.controller.Screens;
import edu.wpi.cs3733d18.teamF.controller.SwitchableController;
import edu.wpi.cs3733d18.teamF.graph.MapSingleton;
import edu.wpi.cs3733d18.teamF.sr.ServiceRequestSingleton;
import edu.wpi.cs3733d18.teamF.sr.ServiceRequests;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Collections;

public class InboxController implements SwitchableController {
    PaneSwitcher switcher;
    private boolean decreasingOrder = true;
    private ServiceRequests selectedRequest = null;


    @FXML
    private JFXListView inboxRequests;
    @FXML
    FontAwesomeIconView searchFilters;
    @FXML
    private JFXTextField inboxSearch;
    @FXML
    private JFXCheckBox completeCheck;
    @FXML
    private JFXComboBox inboxPrioritySort, inboxStatusSort, inboxAllSort;
    @FXML
    private Label typeLabel, fullNameLabel, idLabel, locationLabel, statusLabel, completedByLabel, usernameLabel;
    @FXML
    private TextArea instructionsTextArea;
    @FXML
    private AnchorPane editRequestPane;

    private final ObservableList<String> priority = FXCollections.observableArrayList("0", "1", "2", "3", "4", "5");
    private final ObservableList<String> status = FXCollections.observableArrayList("Incomplete", "In Progress", "Complete");
    private final ObservableList<String> type = FXCollections.observableArrayList("Language Interpreter", "Religious Services", "Security Request", "Maintenance Request");


    public void initialize(PaneSwitcher switcher){
        this.switcher=switcher;
        setInbox();

        inboxPrioritySort.getItems().addAll(priority);
        inboxStatusSort.getItems().addAll(status);
        inboxAllSort.getItems().addAll(type);

        searchFilters.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
           onSearchFilters();
        });


    }
    @FXML
    private void onExitInbox(){
        switcher.closePopup(Screens.Inbox);
    }

    private JFXButton createMessage(ServiceRequests request){
        JFXButton button = new JFXButton();
        button.setStyle("-fx-background-color:  WHITE");
        Pane pane = new Pane();
        pane.setPrefSize(200, 100);
        pane.setMaxSize(200, 100);
        pane.setMinSize(200, 100);
        //pane.setStyle("-fx-background-radius: 40");
        FontAwesomeIconView iconType = new FontAwesomeIconView();
        pane.getChildren().add(iconType);
        switch(request.getType()){
            case "Language Interpreter":
                iconType.setGlyphName("LANGUAGE");
                break;
            case "Religious Services":
                iconType.setGlyphName("BOOK");
                break;
            case "Security Request":
                iconType.setGlyphName("SHIELD");
                break;
            case "Maintenance Request":
                iconType.setGlyphName("WRENCH");
                break;
        }
        iconType.setGlyphSize(15);
        iconType.setLayoutX(8);
        iconType.setLayoutY(28);

        Label requestType = new Label();
        requestType.setText(request.getType());
        pane.getChildren().add(requestType);
        requestType.setLayoutX(30);
        requestType.setLayoutY(14);
        requestType.setAlignment(Pos.CENTER_LEFT);

        Label requestPriority = new Label();
        requestPriority.setText(Integer.toString(request.getPriority()));
        pane.getChildren().add(requestPriority);
        requestPriority.setLayoutX(8);
        requestPriority.setLayoutY(50);

        Label requestLocation = new Label();
        requestLocation.setText(request.getLocation());
        pane.getChildren().add(requestLocation);
        requestLocation.setLayoutX(30);
        requestLocation.setLayoutY(50);
        requestLocation.setAlignment(Pos.CENTER_LEFT);

        button.setGraphic(pane);

        button.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            onSelectInboxMessage(request);
        });

        return  button;

    }

    public void onSelectInboxMessage(ServiceRequests s){
        selectedRequest = s;
        typeLabel.setText("Type: " + s.getType());
        if (s.getType().equals("Language Interpreter") || s.getType().equals("Religious Services")) {
            fullNameLabel.setText(s.getFirstName() + " " + s.getLastName());
        } else {
            fullNameLabel.setText("N/A");
        }
        idLabel.setText("Service Request #" + s.getId());
        locationLabel.setText(s.getLocation());
        statusLabel.setText(s.getStatus());
        instructionsTextArea.setText(s.getDescription());
        instructionsTextArea.setEditable(false);
        if (s.getStatus().equalsIgnoreCase("Complete")) {
            completeCheck.setSelected(true);
        } else {
            completeCheck.setSelected(false);
        }

        if (s.getStatus().equals("Complete")) {
            completedByLabel.setVisible(true);
            usernameLabel.setVisible(true);
            usernameLabel.setText(s.getCompletedBy());
        }
        editRequestPane.setVisible(true);
    }

    @FXML
    public void onCompleteCheck(){
        String previousStatus = selectedRequest.getStatus();
        if (completeCheck.isSelected() && !selectedRequest.getStatus().equalsIgnoreCase("Complete")) {
            selectedRequest.setStatus("Complete");
            selectedRequest.setCompletedBy(PermissionSingleton.getInstance().getCurrUser());
            ServiceRequestSingleton.getInstance().updateCompletedBy(selectedRequest);
            ServiceRequestSingleton.getInstance().updateStatus(selectedRequest);
            usernameLabel.setText(PermissionSingleton.getInstance().getCurrUser());
            usernameLabel.setVisible(true);
            completedByLabel.setVisible(true);
            statusLabel.setText("Complete");
        }else{
            selectedRequest.setStatus("Incomplete");
            selectedRequest.setCompletedBy(null);
            ServiceRequestSingleton.getInstance().updateStatus(selectedRequest);
            ServiceRequestSingleton.getInstance().updateCompletedBy(selectedRequest);
            usernameLabel.setText("");
            usernameLabel.setVisible(false);
            completedByLabel.setVisible(false);
            statusLabel.setText("In Progress");
        }
        if (selectedRequest.getStatus().equals("Complete") && !previousStatus.equals("Complete") && selectedRequest.getType().equals("Maintenance Request")) {
            MapSingleton.getInstance().getMap().enableNode(selectedRequest.getLocation());
        }else if(previousStatus.equals("Complete") && selectedRequest.getStatus().equals("In Progress") && selectedRequest.getType().equals("Maintenance Request")){
            MapSingleton.getInstance().getMap().disableNode(selectedRequest.getLocation());

        }
    }

    public void setInbox(){
        inboxRequests.getItems().clear();
        ArrayList<ServiceRequests> inbox = ServiceRequestSingleton.getInstance().getInbox(PermissionSingleton.getInstance().getCurrUser());

        ArrayList<JFXButton> inboxBtns = new ArrayList<>();
        for(ServiceRequests request: inbox){
            inboxBtns.add(createMessage(request));
        }
        ObservableList<JFXButton> allRequests = FXCollections.observableArrayList(inboxBtns);
        if(decreasingOrder) {
            Collections.reverse(allRequests);
        }
        inboxRequests.setItems(allRequests);
    }

    @FXML
    public void onClearFilters(){
        inboxPrioritySort.getSelectionModel().clearSelection();
        inboxStatusSort.getSelectionModel().clearSelection();
        inboxAllSort.getSelectionModel().clearSelection();
        setInbox();
    }

    public void sortRequests(){
        if(decreasingOrder){
            decreasingOrder = false;
        }else{
            decreasingOrder = true;
        }

        if(inboxStatusSort.getSelectionModel().isEmpty() && inboxPrioritySort.getSelectionModel().isEmpty() && inboxAllSort.getSelectionModel().isEmpty()){
            setInbox();
        }else{
            onSearchFilters();
        }

    }

    public void onSearchFilters(){
        int priority = -1;
        String status = null;
        String type = null;
        int numFilters = 0;

        if(!inboxPrioritySort.getSelectionModel().isEmpty()){
            priority = Integer.parseInt(inboxPrioritySort.getSelectionModel().getSelectedItem().toString());
            numFilters++;
        }
        if (!inboxStatusSort.getSelectionModel().isEmpty()){
            status = inboxStatusSort.getSelectionModel().getSelectedItem().toString();
            numFilters++;
        }
        if(!inboxAllSort.getSelectionModel().isEmpty()){
            type = inboxAllSort.getSelectionModel().getSelectedItem().toString();
            numFilters++;
        }

        if(numFilters == 0){
            return;
        }

        ArrayList<ServiceRequests> requests = ServiceRequestSingleton.getInstance().multiFilterSearch(PermissionSingleton.getInstance().getCurrUser(), priority, status, type);
        ArrayList<JFXButton> filteredRequestBtns = new ArrayList<>();
        for(ServiceRequests req: requests){
            filteredRequestBtns.add(createMessage(req));
        }
        inboxRequests.getItems().clear();
        if(decreasingOrder) {
            Collections.reverse(filteredRequestBtns);
        }
        inboxRequests.getItems().addAll(filteredRequestBtns);
    }
}

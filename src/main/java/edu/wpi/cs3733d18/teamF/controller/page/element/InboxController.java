package edu.wpi.cs3733d18.teamF.controller.page.element;

import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import edu.wpi.cs3733d18.teamF.controller.PaneSwitcher;
import edu.wpi.cs3733d18.teamF.controller.PermissionSingleton;
import edu.wpi.cs3733d18.teamF.controller.Screens;
import edu.wpi.cs3733d18.teamF.controller.SwitchableController;
import edu.wpi.cs3733d18.teamF.sr.ServiceRequestSingleton;
import edu.wpi.cs3733d18.teamF.sr.ServiceRequests;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class InboxController implements SwitchableController {
    PaneSwitcher switcher;


    @FXML
    private JFXListView inboxRequests;
    @FXML
    private JFXTextField inboxSearch;
    @FXML
    private JFXComboBox inboxSort;

    public void initialize(PaneSwitcher switcher){
        this.switcher=switcher;

        //ArrayList<ServiceRequests> inbox = ServiceRequestSingleton.getInstance().getInbox(PermissionSingleton.getInstance().getCurrUser());
        ArrayList<ServiceRequests> inbox = ServiceRequestSingleton.getInstance().getListOfRequests();

        ArrayList<JFXButton> inboxBtns = new ArrayList<>();
        for(ServiceRequests request: inbox){
            inboxBtns.add(createMessage(request));
        }
        ObservableList<JFXButton> allRequests = FXCollections.observableArrayList(inboxBtns);

        inboxRequests.setItems(allRequests);

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

    public void onSelectInboxMessage(ServiceRequests request){

    }
}

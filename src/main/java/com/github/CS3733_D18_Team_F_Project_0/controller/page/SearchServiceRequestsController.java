package com.github.CS3733_D18_Team_F_Project_0.controller.page;

import com.github.CS3733_D18_Team_F_Project_0.controller.PaneSwitcher;
import com.github.CS3733_D18_Team_F_Project_0.controller.Screens;
import com.github.CS3733_D18_Team_F_Project_0.controller.SwitchableController;
import com.github.CS3733_D18_Team_F_Project_0.sr.ServiceRequest;
import com.github.CS3733_D18_Team_F_Project_0.sr.ServiceRequestSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.ResultSet;
import java.util.ArrayList;

public class SearchServiceRequestsController implements SwitchableController {
    private PaneSwitcher switcher;
    String searchType;
    String filter;
    private ObservableList<ServiceRequest> listRequests;


    @FXML
    public ComboBox filterType;
    @FXML
    public ComboBox availableTypes;

    @FXML
    public TableView<ServiceRequest> searchResultTable;
    @FXML
    public TableColumn<ServiceRequest, Button> btns;
    @FXML
    public TableColumn<ServiceRequest, Integer> idNumber;
    @FXML
    public TableColumn<ServiceRequest, String> requestType;
    @FXML
    public TableColumn<ServiceRequest, String> first_name;
    @FXML
    public TableColumn<ServiceRequest, String> last_name;
    @FXML
    public TableColumn<ServiceRequest, String> destination;
    @FXML
    public TableColumn<ServiceRequest, Integer> requestPriority;
    @FXML
    public TableColumn<ServiceRequest, String> theStatus;

    private final ObservableList<String> priority = FXCollections.observableArrayList(
            "1",
            "2",
            "3",
            "4",
            "5");
    private final ObservableList<String> status = FXCollections.observableArrayList(
            "Incomplete",
            "In Progress",
            "Complete");
    private final ObservableList<String> type = FXCollections.observableArrayList(
            "Language Interpreter",
            "Religious Services");

    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;
        filter = "none";

        filterType.getItems().addAll("Priority", "Status", "Type");
    }

    @FXML
    void onFilterType() {
        searchType = "none";
        filter = "none";
        try {
            if (filterType.getSelectionModel().getSelectedItem().equals("Priority")) {
                availableTypes.setItems(priority);
                availableTypes.setVisible(true);
            } else if (filterType.getSelectionModel().getSelectedItem().equals("Status")) {
                availableTypes.setItems(status);
                availableTypes.setVisible(true);
            } else if (filterType.getSelectionModel().getSelectedItem().equals("Type")) {
                availableTypes.setItems(type);
                availableTypes.setVisible(true);
            }
        } catch(NullPointerException e){
            searchType = "none";
        }
    }

    @FXML
    void onAvailableTypes() {
        try{
            filter = availableTypes.getSelectionModel().getSelectedItem().toString();
        }catch(NullPointerException e){
            filter = "none";
        }
    }

    @FXML
    void onSearch(){
        ArrayList<ServiceRequest> requests = new ArrayList<>();
        if(filter.equalsIgnoreCase("none")){
            ResultSet all = ServiceRequestSingleton.getInstance().getRequests();
            requests = ServiceRequestSingleton.getInstance().resultSetToServiceRequest(all);
        }else {
            switch (searchType) {
                case "Priority":
                    ResultSet rp = ServiceRequestSingleton.getInstance().getRequestsOfPriority(Integer.parseInt(filter));
                    requests = ServiceRequestSingleton.getInstance().resultSetToServiceRequest(rp);
                    break;

                case "Status":
                    ResultSet rs = ServiceRequestSingleton.getInstance().getRequestsOfStatus(filter);
                    requests = ServiceRequestSingleton.getInstance().resultSetToServiceRequest(rs);
                    break;

                case "Type":
                    ResultSet rt = ServiceRequestSingleton.getInstance().getRequestsOfType(filter);
                    requests = ServiceRequestSingleton.getInstance().resultSetToServiceRequest(rt);
                    break;
            }
        }

        //TODO: put result of search into table
        if(requests.size() < 1){
            return;
        }
        for(ServiceRequest i: requests){
            listRequests.add(i);
        }

        searchResultTable.getItems().setAll(listRequests);
    }

    @FXML
    void onClear(){
        availableTypes.setVisible(false);
        availableTypes.valueProperty().set(null);
        filterType.valueProperty().set(null);
        searchType = "none";
        filter = "none";
    }

    @FXML
    void onCancel(){
        switcher.switchTo(Screens.ServiceRequest);
    }

}

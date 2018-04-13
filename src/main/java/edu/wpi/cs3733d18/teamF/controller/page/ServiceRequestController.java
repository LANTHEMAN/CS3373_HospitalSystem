package edu.wpi.cs3733d18.teamF.controller.page;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733d18.teamF.controller.PermissionSingleton;
import edu.wpi.cs3733d18.teamF.controller.page.HomeController;
import edu.wpi.cs3733d18.teamF.sr.ServiceRequest;
import edu.wpi.cs3733d18.teamF.sr.ServiceRequestSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServiceRequestController{

    ////////////////////////////////////////////////////
    //                                                //
    //           Search Service Request Variables     //
    //                                                //
    ////////////////////////////////////////////////////

    private  final ObservableList<String> filterOptions = FXCollections.observableArrayList(
            "Priority",
            "Status",
            "Type");

    private final ObservableList<String> priority = FXCollections.observableArrayList(
            "0",
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



    private HomeController homeController;
    private ComboBox filterType;
    private AnchorPane searchPane;
    private ComboBox availableTypes;
    private TableView searchResultTable;

    private String searchType;
    private String filter;
    private ServiceRequest serviceRequestPopUp;
    private ObservableList<ServiceRequest> listRequests;

    public ServiceRequestController(HomeController homeController, ComboBox filterType, AnchorPane searchPane, ComboBox availableTypes, TableView searchResultTable) {
        this.homeController = homeController;
        this.filterType = filterType;
        this.searchPane = searchPane;
        this.availableTypes = availableTypes;
        this.searchResultTable = searchResultTable;
    }

    ////////////////////////////////////////////////////
    //                                                //
    //           Search Service Request Functions     //
    //                                                //
    ////////////////////////////////////////////////////

    @FXML
    private void onSearchServiceRequest() {
        filter = "none";
        searchType = "none";

        filterType.getItems().addAll(filterOptions);

        String lastSearch = ServiceRequestSingleton.getInstance().getLastSearch();
        String lastFilter = ServiceRequestSingleton.getInstance().getLastFilter();
        if (lastSearch != null && lastFilter != null) {
            searchType = lastSearch;
            filter = lastFilter;
        }
        onSearch();
        searchPane.setVisible(true);
        homeController.onCancelDirectionsEvent();
        homeController.setCancelMenuEvent();
    }

    @FXML
    void onFilterType() {
        searchType = "none";
        filter = "none";
        try {
            if (filterType.getSelectionModel().getSelectedItem().equals("Priority")) {
                searchType = "Priority";
                availableTypes.setItems(priority);
                availableTypes.setVisible(true);
            } else if (filterType.getSelectionModel().getSelectedItem().equals("Status")) {
                searchType = "Status";
                availableTypes.setItems(status);
                availableTypes.setVisible(true);
            } else if (filterType.getSelectionModel().getSelectedItem().equals("Type")) {
                searchType = "Type";
                availableTypes.setItems(type);
                availableTypes.setVisible(true);
            }
        } catch (NullPointerException e) {
            searchType = "none";
        }
    }

    @FXML
    void onAvailableTypes() {
        try {
            filter = availableTypes.getSelectionModel().getSelectedItem().toString();
        } catch (NullPointerException e) {
            filter = "none";
        }
    }

    @FXML
    void onSearch() {
        ArrayList<ServiceRequest> requests = new ArrayList<>();
        try {
            if (filter.equalsIgnoreCase("none")) {
                ResultSet all = ServiceRequestSingleton.getInstance().getRequests();
                requests = ServiceRequestSingleton.getInstance().resultSetToServiceRequest(all);
                all.close();
            } else {
                switch (searchType) {
                    case "Priority":
                        ResultSet rp = ServiceRequestSingleton.getInstance().getRequestsOfPriority(Integer.parseInt(filter));
                        requests = ServiceRequestSingleton.getInstance().resultSetToServiceRequest(rp);
                        rp.close();
                        break;

                    case "Status":
                        ResultSet rs = ServiceRequestSingleton.getInstance().getRequestsOfStatus(filter);
                        requests = ServiceRequestSingleton.getInstance().resultSetToServiceRequest(rs);
                        rs.close();
                        break;

                    case "Type":
                        ResultSet rt = ServiceRequestSingleton.getInstance().getRequestsOfType(filter);
                        requests = ServiceRequestSingleton.getInstance().resultSetToServiceRequest(rt);
                        rt.close();
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            searchResultTable.getItems().clear();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        //TODO: put result of search into table
        if (requests.size() < 1) {
            //TODO: indicate to user that there are no results
            return;
        } else {
            listRequests = FXCollections.observableArrayList(requests);
        }

        searchResultTable.setEditable(false);

        idNumberCol.setCellValueFactory(new PropertyValueFactory<ServiceRequest, Integer>("id"));
        requestTypeCol.setCellValueFactory(new PropertyValueFactory<ServiceRequest, String>("type"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<ServiceRequest, String>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<ServiceRequest, String>("lastName"));
        destinationCol.setCellValueFactory(new PropertyValueFactory<ServiceRequest, String>("location"));
        requestPriorityCol.setCellValueFactory(new PropertyValueFactory<ServiceRequest, Integer>("priority"));
        theStatusCol.setCellValueFactory(new PropertyValueFactory<ServiceRequest, String>("status"));
        btnsCol.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        Callback<TableColumn<ServiceRequest, String>, TableCell<ServiceRequest, String>> cellFactory
                = //
                new Callback<TableColumn<ServiceRequest, String>, TableCell<ServiceRequest, String>>() {
                    @Override
                    public TableCell call(final TableColumn<ServiceRequest, String> param) {
                        final TableCell<ServiceRequest, String> cell = new TableCell<ServiceRequest, String>() {

                            JFXButton btn = new JFXButton("Select");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        ServiceRequest s = getTableView().getItems().get(getIndex());
                                        onSelect(s);
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        btnsCol.setCellFactory(cellFactory);

        searchResultTable.setItems(listRequests);

        ServiceRequestSingleton.getInstance().setSearch(filter, searchType);
    }



    public void onSelect(ServiceRequest s) {
        ServiceRequestSingleton.getInstance().setPopUpRequest(s);
        serviceRequestPopUp = s;
        typeLabel.setText("Type: " + s.getType());
        idLabel.setText("Service Request #" + s.getId());
        firstNameLabel.setText("First Name: " + s.getFirstName());
        lastNameLabel.setText("Last Name: " + s.getLastName());
        locationLabel.setText(s.getLocation());
        statusLabel.setText(s.getStatus());
        instructionsTextArea.setText(s.getDescription());
        instructionsTextArea.setEditable(false);
        if(s.getStatus().equalsIgnoreCase("Complete")){
            completeCheck.setSelected(true);
        }else{
            completeCheck.setSelected(false);
        }

        if (serviceRequestPopUp.getStatus().equals("Complete")) {
            completedByLabel.setVisible(true);
            usernameLabel.setVisible(true);
            usernameLabel.setText(serviceRequestPopUp.getCompletedBy());
        }
        searchPane.setVisible(false);
        editRequestPane.setVisible(true);
    }

    @FXML
    void onClear() {
        availableTypes.setVisible(false);
        availableTypes.valueProperty().set(null);
        filterType.valueProperty().set(null);
        searchType = "none";
        filter = "none";
        try {
            searchResultTable.getItems().clear();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        ServiceRequestSingleton.getInstance().setSearchNull();
    }

    @FXML
    void onCancelSearch() {
        searchPane.setVisible(false);
    }


    ////////////////////////////////////////
    //                                    //
    //       Edit Service Request         //
    //                                    //
    ////////////////////////////////////////

    public void onCancelEdit() {
        editRequestPane.setVisible(false);
        searchPane.setVisible(true);
    }

    public void onSubmitEdit() {
        if (completeCheck.isSelected() && !serviceRequestPopUp.getStatus().equalsIgnoreCase("Complete")) {
            serviceRequestPopUp.setStatus("Complete");
            serviceRequestPopUp.setCompletedBy(PermissionSingleton.getInstance().getCurrUser());
            ServiceRequestSingleton.getInstance().updateCompletedBy(serviceRequestPopUp);
            ServiceRequestSingleton.getInstance().updateStatus(serviceRequestPopUp);
        }
        if (usernameSearch.getText() != null && !usernameSearch.getText().trim().isEmpty()) {
            ServiceRequestSingleton.getInstance().assignTo(usernameSearch.getText(), serviceRequestPopUp);
        }
        usernameSearch.setText("");
        editRequestPane.setVisible(false);
        searchPane.setVisible(true);
        onSearch();

    }

}

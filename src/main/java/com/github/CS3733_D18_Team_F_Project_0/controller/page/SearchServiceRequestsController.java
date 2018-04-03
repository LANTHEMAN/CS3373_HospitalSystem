package com.github.CS3733_D18_Team_F_Project_0.controller.page;

import com.github.CS3733_D18_Team_F_Project_0.controller.PaneSwitcher;
import com.github.CS3733_D18_Team_F_Project_0.controller.SwitchableController;
import com.github.CS3733_D18_Team_F_Project_0.sr.ServiceRequest;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class SearchServiceRequestsController implements SwitchableController {
    private PaneSwitcher switcher;


    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;
    }

    @FXML
    public ComboBox filterType;
    @FXML
    public ComboBox availableTypes;

    @FXML
    public TableView<ServiceRequest> searchResultTable;
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
}

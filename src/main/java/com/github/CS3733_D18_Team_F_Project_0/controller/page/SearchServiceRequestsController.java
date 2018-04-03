package com.github.CS3733_D18_Team_F_Project_0.controller.page;

import com.github.CS3733_D18_Team_F_Project_0.controller.PaneSwitcher;
import com.github.CS3733_D18_Team_F_Project_0.controller.SwitchableController;
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
    public TableView searchResultTable;
    @FXML
    public TableColumn id;
    @FXML
    public TableColumn type;
    @FXML
    public TableColumn firstName;
    @FXML
    public TableColumn lastName;
    @FXML
    public TableColumn destination;
    @FXML
    public TableColumn priority;
    @FXML
    public TableColumn status;
}

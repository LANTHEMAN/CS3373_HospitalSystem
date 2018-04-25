package edu.wpi.cs3733d18.teamF.controller.page;

import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import edu.wpi.cs3733d18.teamF.controller.PaneSwitcher;
import edu.wpi.cs3733d18.teamF.controller.PermissionSingleton;
import edu.wpi.cs3733d18.teamF.controller.SwitchableController;
import edu.wpi.cs3733d18.teamF.db.DatabaseWrapper;
import edu.wpi.cs3733d18.teamF.gfx.PaneVoiceController;
import edu.wpi.cs3733d18.teamF.gfx.impl.radial.GenericRadial;
import edu.wpi.cs3733d18.teamF.graph.MapSingleton;
import edu.wpi.cs3733d18.teamF.notifications.TwilioHandlerSingleton;
import edu.wpi.cs3733d18.teamF.sr.*;
import edu.wpi.cs3733d18.teamF.voice.VoiceCommandVerification;
import edu.wpi.cs3733d18.teamF.voice.VoiceLauncher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javafx.util.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

public class MainPage implements SwitchableController, Observer {
    private final ObservableList<String> priority = FXCollections.observableArrayList("0", "1", "2", "3", "4", "5");
    private final ObservableList<String> status = FXCollections.observableArrayList("Incomplete", "In Progress", "Complete");
    private final ObservableList<String> type = FXCollections.observableArrayList("Language Interpreter", "Religious Services", "Security Request", "Maintenance Request");
    private final ObservableList<String> filterOptions = FXCollections.observableArrayList("Priority", "Status", "Type");
    private final ObservableList<String> languages = FXCollections.observableArrayList("Spanish", "French", "Chinese");
    private final ObservableList<String> religions = FXCollections.observableArrayList("Catholic", "Protestant", "Islamic", "Hindu", "Jewish", "Buddhist");
    //TODO: Add realistic situations for maintenance request
    private final ObservableList<String> situations = FXCollections.observableArrayList("Elevator Repair", "Broken Bed", "Fix Door");
    @FXML
    public ComboBox filterType, availableTypes, availableLanguagesBox, situationSelection;
    @FXML
    public TableView<ServiceRequests> searchResultTable;
    @FXML
    public TableColumn btnsCol;
    @FXML
    public TableColumn<ServiceRequests, Integer> idNumberCol, requestPriorityCol;
    @FXML
    public TableColumn<ServiceRequests, String> requestTypeCol, firstNameCol, lastNameCol, destinationCol, theStatusCol;
    @FXML
    public Label typeLabel, idLabel, fullNameLabel, locationLabel, statusLabel, completedByLabel, usernameLabel;
    @FXML
    public TextArea instructionsTextArea;
    PaneSwitcher switcher;
    @FXML
    AnchorPane languageInterpreterPane;
    @FXML
    JFXTextField languageField;
    @FXML
    JFXTextField firstNameLanguage;
    @FXML
    JFXTextField lastNameLanguage;
    @FXML
    JFXTextField destinationLanguage;
    @FXML
    JFXTextArea instructionsLanguage;
    @FXML
    Label languageRequiredLI;
    @FXML
    Label lastNameRequiredLI;
    @FXML
    Label firstNameRequiredLI;
    @FXML
    Label locationRequiredLI;
    @FXML
    ToggleGroup staffToggleSR, securityToggleSR, securityToggleLI, securityToggleRS, staffToggleRS, securityToggleMaintenance, staffToggleMaintenance;
    @FXML
    JFXTextField securityLocationField, requestTitleField;
    @FXML
    Label securityLocationRequired, requestTitleRequired;
    @FXML
    JFXTextArea securityTextArea;
    @FXML
    AnchorPane securityPane;
    @FXML
    GridPane serviceRequestPane;
    @FXML
    JFXTabPane serviceRequestTabPane;
    @FXML
    TextField firstNameRS, lastNameRS, destinationRS;
    @FXML
    JFXComboBox religionSelect;
    @FXML
    TextArea instructionsRS;
    @FXML
    JFXListView destinationMaintenanceList, destinationRSList, destinationLIList, securityLocationList;
    @FXML
    Label religionRequiredRS, firstNameRequiredRS, lastNameRequiredRS, locationRequiredRS, occasionRequiredRS;
    String lastSearch = ServiceRequestSingleton.getInstance().getLastSearch();
    String lastFilter = ServiceRequestSingleton.getInstance().getLastFilter();
    @FXML
    Label invalidMaintenanceLocation, invalidLocationRS, invalidLocationLI, invalidLocationSR;
    /////////////////////////////////////////
    //                                     //
    //           Service Request           //
    //                                     //
    /////////////////////////////////////////
    private ServiceRequests serviceRequestsPopUp;
    @FXML
    private AnchorPane editRequestPane;
    /////////////////////////////////
    //       Search Services       //
    /////////////////////////////////
    private String searchType;
    private String filter;
    @FXML
    private AnchorPane searchPane;
    @FXML
    private JFXTextField usernameSearch;
    @FXML
    private JFXCheckBox completeCheck;
    @FXML
    private AnchorPane religiousServicesPane;
    @FXML
    private AnchorPane maintenancePane;
    @FXML
    private FontAwesomeIconView closeBtn;
    @FXML
    private StackPane mainPane;
    @FXML
    private JFXTextField destinationMaintenance;
    @FXML
    private Label maintenanceSituationRequired, maintenanceLocationRequired;
    @FXML
    private JFXTextArea instructionsMaintenance;
    @FXML
    private JFXListView assignedUsernames;
    @FXML
    private FontAwesomeIconView plusAssignTo;
    private VoiceCommandVerification voice;
    private PaneVoiceController paneVoiceController;
    @FXML
    private Pane voicePane;
    private GenericRadial radialMenu;
    @FXML
    private JFXListView usernameList;

    @Override
    public void initialize(PaneSwitcher switcher) {
        this.switcher = switcher;
        VoiceCommandVerification voice = new VoiceCommandVerification();
        voice.addObserver(this);
        VoiceLauncher.getInstance().addObserver(voice);


        paneVoiceController = new PaneVoiceController(voicePane);


        plusAssignTo.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (!usernameSearch.isVisible()) {
                usernameSearch.setVisible(true);
            }
        });


        filter = "none";
        searchType = "none";
        if (lastSearch != null && lastFilter != null) {
            searchType = lastSearch;
            filter = lastFilter;
        }

        filterType.getItems().addAll(filterOptions);
        availableLanguagesBox.getItems().addAll(languages);
        religionSelect.getItems().addAll(religions);
        situationSelection.getItems().addAll(situations);

        usernameSearch.setOnKeyTyped((KeyEvent e) -> {
            String input = usernameSearch.getText();
            input = input.concat("" + e.getCharacter());
            DatabaseWrapper.autoComplete(input, usernameList, "HUser", "username");
        });

        destinationMaintenance.setOnKeyTyped((KeyEvent e) -> {
            String input = destinationMaintenance.getText();
            input = input.concat("" + e.getCharacter());
            DatabaseWrapper.autoCompleteLocations(input, destinationMaintenanceList);
        });

        destinationRS.setOnKeyTyped((KeyEvent e) -> {
            String input = destinationRS.getText();
            input = input.concat("" + e.getCharacter());
            DatabaseWrapper.autoCompleteLocations(input, destinationRSList);
        });

        destinationLanguage.setOnKeyTyped((KeyEvent e) -> {
            String input = destinationLanguage.getText();
            input = input.concat("" + e.getCharacter());
            DatabaseWrapper.autoCompleteLocations(input, destinationLIList);
        });

        securityLocationField.setOnKeyTyped((KeyEvent e) -> {
            String input = securityLocationField.getText();
            input = input.concat("" + e.getCharacter());
            DatabaseWrapper.autoCompleteLocations(input, securityLocationList);
        });


        radialMenu = new GenericRadial(Arrays.asList(
                new Pair<>(new Pair<>("language.png", "Transportation Services"), () -> {
                    if (ServiceRequestSingleton.getInstance().isInTable(PermissionSingleton.getInstance().getCurrUser(), "LanguageInterpreter") || PermissionSingleton.getInstance().isAdmin()) {
                        languageInterpreterPane.toFront();
                    }
                })
                , new Pair<>(new Pair<>("book.png", "Gift Shop"), () -> {
                    if (ServiceRequestSingleton.getInstance().isInTable(PermissionSingleton.getInstance().getCurrUser(), "ReligiousServices") || PermissionSingleton.getInstance().isAdmin()) {
                        religiousServicesPane.toFront();
                    }
                })
                , new Pair<>(new Pair<>("shield.png", "Maintenance Request"), () -> {
                    if (ServiceRequestSingleton.getInstance().isInTable(PermissionSingleton.getInstance().getCurrUser(), "SecurityRequest") || PermissionSingleton.getInstance().isAdmin()) {
                        securityPane.toFront();
                    }
                })
                , new Pair<>(new Pair<>("wrench.png", "Security Request"), () -> {
                    if (ServiceRequestSingleton.getInstance().isInTable(PermissionSingleton.getInstance().getCurrUser(), "MaintenanceRequest") || PermissionSingleton.getInstance().isAdmin()) {
                        maintenancePane.toFront();
                    }
                })
                , new Pair<>(new Pair<>("shopping.png", "Religious Services"), () -> {
                    System.out.println("Gift");
                })
                , new Pair<>(new Pair<>("car.png", "Language Interpreter"), () -> {
                    System.out.println("Transport");
                })
        ));

        radialMenu.setTranslateX(0);
        radialMenu.setTranslateY(0);
        mainPane.getChildren().add(radialMenu);

        onSearch();
    }

    @FXML
    private void setAssignTo() {
        String selection = usernameList.getSelectionModel().getSelectedItem().toString();
        if (!assignedUsernames.getItems().contains(selection)) {
            assignedUsernames.getItems().add(selection);
        }
        usernameSearch.setVisible(false);
        usernameList.setVisible(false);
    }

    @FXML
    private void onDestinationRSList() {
        String selection = destinationRSList.getSelectionModel().getSelectedItem().toString();
        destinationRS.setText(selection);
        destinationRSList.setVisible(false);
    }

    @FXML
    private void onDestinationMaintenance() {
        String selection = destinationMaintenanceList.getSelectionModel().getSelectedItem().toString();
        destinationMaintenance.setText(selection);
        destinationMaintenanceList.setVisible(false);
    }

    @FXML
    private void onDestinationLIList() {
        String selection = destinationLIList.getSelectionModel().getSelectedItem().toString();
        destinationLanguage.setText(selection);
        destinationLIList.setVisible(false);
    }

    @FXML
    private void onSecurityLocationList() {
        String selection = securityLocationList.getSelectionModel().getSelectedItem().toString();
        securityLocationField.setText(selection);
        securityLocationList.setVisible(false);
    }

    @FXML
    void onSearch() {
        ArrayList<ServiceRequests> requests = new ArrayList<>();
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


        ObservableList<ServiceRequests> listRequests;
        if (requests.size() < 1) {
            return;
        } else {
            listRequests = FXCollections.observableArrayList(requests);
        }

        searchResultTable.setEditable(false);

        idNumberCol.setCellValueFactory(new PropertyValueFactory<ServiceRequests, Integer>("id"));
        requestTypeCol.setCellValueFactory(new PropertyValueFactory<ServiceRequests, String>("type"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<ServiceRequests, String>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<ServiceRequests, String>("lastName"));
        destinationCol.setCellValueFactory(new PropertyValueFactory<ServiceRequests, String>("location"));
        requestPriorityCol.setCellValueFactory(new PropertyValueFactory<ServiceRequests, Integer>("priority"));
        theStatusCol.setCellValueFactory(new PropertyValueFactory<ServiceRequests, String>("status"));
        btnsCol.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        Callback<TableColumn<ServiceRequests, String>, TableCell<ServiceRequests, String>> cellFactory
                =
                new Callback<TableColumn<ServiceRequests, String>, TableCell<ServiceRequests, String>>() {
                    @Override
                    public TableCell call(final TableColumn<ServiceRequests, String> param) {
                        final TableCell<ServiceRequests, String> cell = new TableCell<ServiceRequests, String>() {

                            JFXButton btn = new JFXButton("Select");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        ServiceRequests s = getTableView().getItems().get(getIndex());
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

    public void onSelect(ServiceRequests s) {
        ServiceRequestSingleton.getInstance().setPopUpRequest(s);
        serviceRequestsPopUp = s;
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

        if (serviceRequestsPopUp.getStatus().equals("Complete")) {
            completedByLabel.setVisible(true);
            usernameLabel.setVisible(true);
            usernameLabel.setText(serviceRequestsPopUp.getCompletedBy());
        }

        assignedUsernames.setItems(ServiceRequestSingleton.getInstance().getAssignedUsers(s.getId()));

        if (PermissionSingleton.getInstance().isAdmin()) {
            if (!plusAssignTo.isVisible()) {
                plusAssignTo.setVisible(true);
            }
        } else {
            if (plusAssignTo.isVisible()) {
                plusAssignTo.setVisible(false);
            }
        }

        if (usernameList.isVisible()) {
            usernameList.setVisible(false);
        }
        if (usernameSearch.isVisible()) {
            usernameSearch.setVisible(false);
        }

        editRequestPane.toFront();
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
        onSearch();
    }

    @FXML
    public void onSubmitEdit() {
        if (assignedUsernames.getItems().size() > 0) {
            ObservableList<String> users = assignedUsernames.getItems();
            for (String username : users) {
                if (!ServiceRequestSingleton.getInstance().alreadyAssignedTo(username, serviceRequestsPopUp.getId())) {
                    ServiceRequestSingleton.getInstance().assignTo(username, serviceRequestsPopUp);
                }
            }
        }

        if (completeCheck.isSelected() && !serviceRequestsPopUp.getStatus().equalsIgnoreCase("Complete")) {
            serviceRequestsPopUp.setStatus("Complete");
            serviceRequestsPopUp.setCompletedBy(PermissionSingleton.getInstance().getCurrUser());
            ServiceRequestSingleton.getInstance().updateCompletedBy(serviceRequestsPopUp);
            ServiceRequestSingleton.getInstance().updateStatus(serviceRequestsPopUp);
        }
        if (serviceRequestsPopUp.getStatus().equals("Complete") && serviceRequestsPopUp.getType().equals("Maintenance Request")) {
            MapSingleton.getInstance().getMap().enableNode(serviceRequestsPopUp.getLocation());
        }
        usernameSearch.setText("");
        editRequestPane.toBack();
        onSearch();
    }

    ////////////////////////
    //                    //
    //       LI           //
    //                    //
    ////////////////////////

    @FXML
    private void onCancelEdit() {
        editRequestPane.toBack();
    }

    @FXML
    void onSubmitLI() {
        int requiredFieldsEmpty = 0;
        String language;
        String first_name;
        String last_name;
        String location;
        String description;
        if (availableLanguagesBox.getSelectionModel().isEmpty()) {
            languageRequiredLI.setVisible(true);
            requiredFieldsEmpty++;
        }
        if (firstNameLanguage.getText() == null || firstNameLanguage.getText().trim().isEmpty()) {
            firstNameRequiredLI.setVisible(true);
            requiredFieldsEmpty++;
        }
        if (lastNameLanguage.getText() == null || lastNameLanguage.getText().trim().isEmpty()) {
            lastNameRequiredLI.setVisible(true);
            requiredFieldsEmpty++;
        }
        if (destinationLanguage.getText() == null || destinationLanguage.getText().trim().isEmpty()) {
            locationRequiredLI.setVisible(true);
            requiredFieldsEmpty++;
        }
        if (requiredFieldsEmpty > 0) {
            return;
        }
        location = destinationLanguage.getText();
        if (!MapSingleton.getInstance().getMap().isValidLocation(location)) {
            invalidLocationLI.setVisible(true);
            return;
        }

        if (instructionsLanguage.getText() == null || instructionsLanguage.getText().trim().isEmpty()) {
            description = "N/A";
        } else {
            description = instructionsLanguage.getText();
        }
        first_name = firstNameLanguage.getText();
        last_name = lastNameLanguage.getText();
        language = availableLanguagesBox.getSelectionModel().getSelectedItem().toString();
        String new_description = language + "/////" + description;
        RadioButton selection = (RadioButton) securityToggleLI.getSelectedToggle();
        int priority = Integer.parseInt(selection.getText());
        ServiceRequests request = new LanguageInterpreter(first_name, last_name, location, new_description, "Incomplete", priority, language, "1");
        ServiceRequestSingleton.getInstance().sendServiceRequest(request);
        ServiceRequestSingleton.getInstance().addServiceRequest(request);
        TwilioHandlerSingleton.getInstance().sendMessage("\n" + first_name + " " + last_name + " needs a " + language + " interpreter at " + location + ".\nAdditional Details: " + description);
        languageInterpreterPane.toBack();
        clearLanguage();
    }

    @FXML
    void onCancelLI() {
        languageInterpreterPane.toBack();
        clearLanguage();
    }

    private void clearLanguage() {
        languageField.clear();
        if (languageRequiredLI.isVisible()) {
            languageRequiredLI.setVisible(false);
        }
        firstNameLanguage.clear();
        if (firstNameRequiredLI.isVisible()) {
            firstNameRequiredLI.setVisible(false);
        }
        lastNameLanguage.clear();
        if (lastNameRequiredLI.isVisible()) {
            lastNameRequiredLI.setVisible(false);
        }
        destinationLanguage.clear();
        if (locationRequiredLI.isVisible()) {
            locationRequiredLI.setVisible(false);
        }
        if (invalidLocationLI.isVisible()) {
            invalidLocationLI.setVisible(false);
        }
        if (destinationLIList.isVisible()) {
            destinationLIList.setVisible(false);
        }
        instructionsLanguage.clear();
    }

    ////////////////////////
    //                    //
    //       RS           //
    //                    //
    ////////////////////////
    @FXML
    void onSubmitRS() {
        int requiredFieldsEmpty = 0;
        String religion;
        String first_name;
        String last_name;
        String location;
        String description;
        if (religionSelect.getSelectionModel().isEmpty()) {
            religionRequiredRS.setVisible(true);
            requiredFieldsEmpty++;
        }
        if (firstNameRS.getText() == null || firstNameRS.getText().trim().isEmpty()) {
            firstNameRequiredRS.setVisible(true);
            requiredFieldsEmpty++;
        }
        if (lastNameRS.getText() == null || lastNameRS.getText().trim().isEmpty()) {
            lastNameRequiredRS.setVisible(true);
            requiredFieldsEmpty++;
        }
        if (destinationRS.getText() == null || destinationRS.getText().trim().isEmpty()) {
            locationRequiredRS.setVisible(true);
            requiredFieldsEmpty++;
        }
        if (requiredFieldsEmpty > 0) {
            return;
        }
        location = destinationRS.getText();
        if (!MapSingleton.getInstance().getMap().isValidLocation(location)) {
            invalidLocationRS.setVisible(true);
            return;
        }
        if (instructionsRS.getText() == null || instructionsRS.getText().trim().isEmpty()) {
            description = "N/A";
        } else {
            description = instructionsRS.getText();
        }
        religion = religionSelect.getSelectionModel().getSelectedItem().toString();
        first_name = firstNameRS.getText();
        last_name = lastNameRS.getText();
        String new_description = religion + "/////" + description + "\n";
        RadioButton selected = (RadioButton) staffToggleRS.getSelectedToggle();
        String staffNeeded = selected.getText();
        selected = (RadioButton) securityToggleRS.getSelectedToggle();
        int priority = Integer.parseInt(selected.getText());
        ServiceRequests request = new ReligiousServices(first_name, last_name, location, new_description, "Incomplete", priority, religion, staffNeeded);
        ServiceRequestSingleton.getInstance().sendServiceRequest(request);
        ServiceRequestSingleton.getInstance().addServiceRequest(request);
        TwilioHandlerSingleton.getInstance().sendMessage("\n" + first_name + " " + last_name + " needs " + religion + " services at " + location + ".\nAdditional Details: " + description);
        religiousServicesPane.toBack();
        clearReligious();
    }

    @FXML
    void onCancelRS() {
        religiousServicesPane.toBack();
        clearReligious();
    }

    ////////////////////////
    //                    //
    //       SR           //
    //                    //
    ////////////////////////

    private void clearReligious() {
        religionSelect.getSelectionModel().clearSelection();
        if (religionRequiredRS.isVisible()) {
            religionRequiredRS.setVisible(false);
        }
        if (occasionRequiredRS.isVisible()) {
            occasionRequiredRS.setVisible(false);
        }
        firstNameRS.clear();
        if (firstNameRequiredRS.isVisible()) {
            firstNameRequiredRS.setVisible(false);
        }
        lastNameRS.clear();
        if (lastNameRequiredRS.isVisible()) {
            lastNameRequiredRS.setVisible(false);
        }
        destinationRS.clear();
        if (locationRequiredRS.isVisible()) {
            locationRequiredRS.setVisible(false);
        }
        if (invalidLocationRS.isVisible()) {
            invalidLocationRS.setVisible(false);
        }
        if (destinationRSList.isVisible()) {
            destinationRSList.setVisible(false);
        }
        instructionsRS.clear();
    }

    @FXML
    private void onSubmitSecurity() {
        int requiredFields = 0;
        if (securityLocationField.getText() == null || securityLocationField.getText().trim().isEmpty()) {
            securityLocationRequired.setVisible(true);
            requiredFields++;
        }
        if (requestTitleField.getText() == null || requestTitleField.getText().trim().isEmpty()) {
            requestTitleRequired.setVisible(true);
            requiredFields++;
        }
        if (requiredFields > 0) {
            return;
        }
        String location = securityLocationField.getText();
        if (!MapSingleton.getInstance().getMap().isValidLocation(location)) {
            invalidLocationSR.setVisible(true);
            return;
        }
        String description = securityTextArea.getText();
        String requestTitle = requestTitleField.getText();
        String status = "Incomplete";
        RadioButton selected = (RadioButton) securityToggleSR.getSelectedToggle();
        int priority = Integer.parseInt(selected.getText());
        RadioButton staffSelected = (RadioButton) staffToggleSR.getSelectedToggle();
        String staffNeeded = staffSelected.getText();
        description = requestTitle + "/////" + description;
        SecurityRequests sec = new SecurityRequests(location, description, status, priority, staffNeeded);

        ServiceRequestSingleton.getInstance().sendServiceRequest(sec);
        ServiceRequestSingleton.getInstance().addServiceRequest(sec);
        TwilioHandlerSingleton.getInstance().sendMessage("\nSecurity is required at " + location + ".\nAdditional Details: " + description);
        securityPane.toBack();
        clearSecurity();
    }

    @FXML
    private void onCancelSecurity() {
        securityPane.toBack();
        clearSecurity();
    }

    ////////////////////////
    //                    //
    //    Maintenance     //
    //                    //
    ////////////////////////

    private void clearSecurity() {
        securityLocationField.clear();
        securityTextArea.clear();
        requestTitleField.clear();
        if (securityLocationRequired.isVisible()) {
            securityLocationRequired.setVisible(false);
        }
        if (invalidLocationSR.isVisible()) {
            invalidLocationSR.setVisible(false);
        }
        if (securityLocationList.isVisible()) {
            securityLocationList.setVisible(false);
        }
    }

    @FXML
    private void onSubmitMaintenance() {
        int requiredFields = 0;
        if (situationSelection.getSelectionModel().isEmpty()) {
            maintenanceSituationRequired.setVisible(true);
            requiredFields++;
        }
        if (destinationMaintenance.getText() == null || destinationMaintenance.getText().trim().isEmpty()) {
            maintenanceLocationRequired.setVisible(true);
            requiredFields++;
        }
        if (requiredFields > 0) {
            return;
        }
        String location = destinationMaintenance.getText();
        if (!MapSingleton.getInstance().getMap().isValidLocation(location)) {
            invalidMaintenanceLocation.setVisible(true);
            return;
        }
        String situation = situationSelection.getSelectionModel().getSelectedItem().toString();
        String description = instructionsMaintenance.getText();
        String status = "Incomplete";
        RadioButton selected = (RadioButton) securityToggleMaintenance.getSelectedToggle();
        int priority = Integer.parseInt(selected.getText());
        RadioButton staffSelected = (RadioButton) staffToggleMaintenance.getSelectedToggle();
        String staffNeeded = staffSelected.getText();
        description = situation + "/////" + description;
        MaintenanceRequest sec = new MaintenanceRequest(location, description, status, priority, situation, staffNeeded);

        MapSingleton.getInstance().getMap().disableNode(MapSingleton.getInstance().getMap().getNodeIDFromLongName(location));

        ServiceRequestSingleton.getInstance().sendServiceRequest(sec);
        ServiceRequestSingleton.getInstance().addServiceRequest(sec);
        TwilioHandlerSingleton.getInstance().sendMessage("\nMaintenance is required at " + location + ".\nAdditional Details: " + description);
        maintenancePane.toBack();
        clearMaintenancePane();
    }

    @FXML
    private void onCancelMaintenance() {
        maintenancePane.toBack();
        clearMaintenancePane();
    }

    private void clearMaintenancePane() {
        situationSelection.getSelectionModel().clearSelection();
        instructionsMaintenance.clear();
        destinationMaintenance.clear();
        if (maintenanceLocationRequired.isVisible()) {
            maintenanceLocationRequired.setVisible(false);
        }
        if (maintenanceSituationRequired.isVisible()) {
            maintenanceSituationRequired.setVisible(false);
        }
        if (invalidMaintenanceLocation.isVisible()) {
            invalidMaintenanceLocation.setVisible(false);
        }
        if (destinationMaintenanceList.isVisible()) {
            destinationMaintenanceList.setVisible(false);
        }
    }


    @FXML
    private void onCreateNewServiceRequest() {
        mainPane.toFront();
        clearLanguage();
        clearReligious();
        clearSecurity();
        clearMaintenancePane();
    }

    @FXML
    private void onSearchServiceRequest() {
        onClear();
        onSearch();
    }


    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Observer caught");
        if (!(o instanceof VoiceCommandVerification)) {
            return;
        }
        System.out.println("Observer Passed");
        System.out.println("arg = " + arg);

        if (arg instanceof String) {
            if (arg.toString().equalsIgnoreCase("ACTIVATE")) {
                paneVoiceController.setVisibility(true);
            } else if (arg.toString().equalsIgnoreCase("Language")) {
                languageInterpreterPane.toFront();
            } else if (arg.toString().equalsIgnoreCase("Religious")) {
                religiousServicesPane.toFront();
            } else if (arg.toString().equalsIgnoreCase("Security")) {
                securityPane.toFront();
            } else if (arg.toString().equalsIgnoreCase("Create")) {
                SingleSelectionModel<Tab> selectionModel = serviceRequestTabPane.getSelectionModel();
                selectionModel.select(0);
            } else if (arg.toString().equalsIgnoreCase("Search")) {
                SingleSelectionModel<Tab> selectionModel = serviceRequestTabPane.getSelectionModel();
                selectionModel.select(1);
            }
        }
    }
}

package com.github.CS3733_D18_Team_F_Project_0.controller;

import com.github.CS3733_D18_Team_F_Project_0.controller.page.*;

public class Screens {

    public static final Screen Home = new Screen("home.fxml", HomeController.class);
    public static final Screen Login = new Screen("login.fxml", LoginController.class);
    public static final Screen ServiceRequest = new Screen("serviceRequest.fxml", ServiceRequestController.class);
    public static final Screen ReligiousServices = new Screen("religiousServices.fxml", ReligiousServicesController.class);
    public static final Screen LanguageInterpreter = new Screen("languageInterpreter.fxml", LanguageInterpreterController.class);

    public static class Screen {
        public final String fxmlFile;
        public final Class<? extends SwitchableController> Controller;

        Screen(String fxmlFile, Class<? extends SwitchableController> Controller) {
            this.fxmlFile = fxmlFile;
            this.Controller = Controller;
        }

    }

}

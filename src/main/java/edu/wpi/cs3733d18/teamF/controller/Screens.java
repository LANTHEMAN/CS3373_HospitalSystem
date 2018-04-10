package edu.wpi.cs3733d18.teamF.controller;

import edu.wpi.cs3733d18.teamF.controller.page.*;

public class Screens {

    public static final Screen Home = new Screen("home.fxml", HomeController.class);
    public static final Screen ReligiousServices = new Screen("religiousServices.fxml", ReligiousServicesController.class);
    public static final Screen LanguageInterpreter = new Screen("languageInterpreter.fxml", LanguageInterpreterController.class);

    static class Screen {
        public final String fxmlFile;
        public final Class<? extends SwitchableController> Controller;

        Screen(String fxmlFile, Class<? extends SwitchableController> Controller) {
            this.fxmlFile = fxmlFile;
            this.Controller = Controller;
        }

    }

}

package edu.wpi.cs3733d18.teamF.controller;

import edu.wpi.cs3733d18.teamF.controller.page.*;
import edu.wpi.cs3733d18.teamF.controller.page.element.InboxController;

public class Screens {

    public static final Screen Home = new Screen("home.fxml", HomeController.class);
    public static final Screen Error = new Screen("error.fxml", ErrorController.class);
    public static final Screen MainPage = new Screen("mainPage.fxml", MainPage.class);
    public static final Screen Inbox = new Screen("inbox.fxml", InboxController.class);

    static class Screen {
        public final String fxmlFile;
        public final Class<? extends SwitchableController> Controller;

        Screen(String fxmlFile, Class<? extends SwitchableController> Controller) {
            this.fxmlFile = fxmlFile;
            this.Controller = Controller;
        }

    }

}

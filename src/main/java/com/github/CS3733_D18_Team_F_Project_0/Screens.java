package com.github.CS3733_D18_Team_F_Project_0;

public class Screens {

    public static final Screen Home = new Screen("home.fxml", HomeController.class);
    public static final Screen Example = new Screen("example.fxml", ExampleController.class);
    public static final Screen Login = new Screen("login.fxml", LoginController.class);
    public static final Screen Floor = new Screen("selectFloor.fxml", FloorController.class);



    public static class Screen {
        public final String fxmlFile;
        public final Class<? extends SwitchableController> Controller;

        Screen(String fxmlFile, Class<? extends SwitchableController> Controller) {
            this.fxmlFile = fxmlFile;
            this.Controller = Controller;
        }
    }

}

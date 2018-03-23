package com.github.CS3733_D18_Team_F_Project_0;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, 1280, 720);
        PaneSwitcher paneSwitcher = new PaneSwitcher(scene);

        // add panes
        paneSwitcher.addPane(Screens.homeName, "home.fxml", HomeController.class);
        paneSwitcher.addPane(Screens.exampleName, "example.fxml", ExampleController.class);

        // inital pane
        paneSwitcher.switchTo("home");

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}


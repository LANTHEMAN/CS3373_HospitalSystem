package com.github.CS3733_D18_Team_F_Project_0;

import com.github.CS3733_D18_Team_F_Project_0.controller.PaneSwitcher;
import com.github.CS3733_D18_Team_F_Project_0.controller.Screens;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class Main extends Application {
    public static void main(String[] args) {
        // get rid of the database folder if its empty
        File file = new File("database/seg0");
        if (file.exists() && file.isDirectory() && file.list().length == 0) {
            // delete the empty database folder
            try {
                Files.walk(Paths.get("database"))
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Group root = new Group();
        Scene scene = new Scene(root, 1280, 720);
        PaneSwitcher paneSwitcher = new PaneSwitcher(scene);

        javafx.scene.image.Image image = new javafx.scene.image.Image(getClass().getResource("BWHIcon.png").toExternalForm());
        primaryStage.getIcons().add(image);

        // initial pane
        paneSwitcher.switchTo(Screens.Home);

        primaryStage.setTitle("Brigham and Women's Hospital");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}


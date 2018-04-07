package com.github.CS3733_D18_Team_F_Project_0;

import com.github.CS3733_D18_Team_F_Project_0.controller.PaneSwitcher;
import com.github.CS3733_D18_Team_F_Project_0.controller.Screens;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class Main extends Application {
    public static void main(String[] args) throws IOException {
        long fileSize = 0;
        // get rid of the database folder if its empty
        try {
            fileSize = Files.find(Paths.get("database"), 3
                    , (p, bfa) -> bfa.isRegularFile())
                    .mapToLong(path -> path.toFile().length())
                    .sum();

            // delete the database if it is empty
            if (fileSize < 100) {
                Files.walk(Paths.get("database"))
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        } catch (IOException e) {
        }

        Configuration configuration = new Configuration();

        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

        LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(configuration);
        // Start recognition process pruning previously cached data.

        recognizer.startRecognition(true);
        SpeechResult result = recognizer.getResult();

        System.out.println("Hello");
        while(true){
            result = recognizer.getResult();
            System.out.println(result.getHypothesis());
        }
        // Pause recognition process. It can be resumed then with startRecognition(false).
        //recognizer.stopRecognition();

        //launch(args);
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
        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }
}


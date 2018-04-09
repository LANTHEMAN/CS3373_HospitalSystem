package com.github.CS3733_D18_Team_F_Project_0.voice;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Observable;
import java.util.Scanner;

public class VoiceLauncher extends Observable implements Runnable {

    private boolean terminate = false;
    Configuration configuration = new Configuration();

    HashSet<String> commandStrings = new HashSet<>();

    public static VoiceLauncher getInstance(){ return LazyInitializer.INSTANCE; }

    private VoiceLauncher(){
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("Corpus.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while(scanner.hasNextLine()){
            commandStrings.add(scanner.nextLine().toUpperCase());
        }

        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("7755.dic");
        configuration.setLanguageModelPath("7755.lm");
    }

    private static class LazyInitializer {
        static final VoiceLauncher INSTANCE = new VoiceLauncher();
    }

    public void run() {
        try {
            LiveSpeechRecognizer recognize = new LiveSpeechRecognizer(configuration);

            recognize.startRecognition(true);

            //Create SpeechResult Object
            SpeechResult result;
            String command;

            //Checking if recognizer has recognized the speech
            while ((result = recognize.getResult()) != null && !terminate) {
                //Get the recognize speech
                command = result.getHypothesis();

                if(isValid(command)) {
                    signalClassChanged(command);
                }
            }
            recognize.stopRecognition();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void signalClassChanged(Object args) {
        this.setChanged();
        this.notifyObservers(args);
    }

    private boolean isValid(String command){
        return commandStrings.contains(command);
    }

    public void terminate(){
        terminate = true;
    }
}


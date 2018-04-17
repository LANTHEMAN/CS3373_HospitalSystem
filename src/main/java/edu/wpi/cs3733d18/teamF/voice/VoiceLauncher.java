package edu.wpi.cs3733d18.teamF.voice;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

import java.io.IOException;
import java.util.Observable;

public class VoiceLauncher extends Observable implements Runnable {

    Configuration configuration = new Configuration();
    private boolean terminate = false;
    LiveSpeechRecognizer recognize;

    private VoiceLauncher() {
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("3075.dic");
        configuration.setLanguageModelPath("3075.lm");

        try {
            recognize = new LiveSpeechRecognizer(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static VoiceLauncher getInstance() {
        return LazyInitializer.INSTANCE;
    }

    public void run() {

        recognize.startRecognition(true);

        //Create SpeechResult Object
        SpeechResult result;
        String command;

        //Checking if recognizer has recognized the speech
        while ((result = recognize.getResult()) != null && !terminate) {
            //Get the recognize speech
            command = result.getHypothesis();
            signalClassChanged(command);
        }
        recognize.stopRecognition();


    }

    private void signalClassChanged(Object args) {
        this.setChanged();
        this.notifyObservers(args);
    }

    public void terminate() {
        terminate = true;
    }

    public void pause() {
        try {
            recognize.stopRecognition();
            recognize = null;
            Thread.sleep(10);
        } catch (Exception e) {
        }
    }

    public void resume() {
        recognize.startRecognition(true);
    }

    private static class LazyInitializer {
        static final VoiceLauncher INSTANCE = new VoiceLauncher();
    }
}


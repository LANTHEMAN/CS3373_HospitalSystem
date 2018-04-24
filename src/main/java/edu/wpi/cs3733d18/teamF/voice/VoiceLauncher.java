package edu.wpi.cs3733d18.teamF.voice;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Observable;

public class VoiceLauncher extends Observable implements Runnable {

    Configuration configuration = new Configuration();
    private boolean terminate = false;

    private VoiceLauncher() {
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        exportResource("1615.dic");
        exportResource("1615.lm");
        configuration.setDictionaryPath("1615.dic");
        configuration.setLanguageModelPath("1615.lm");
    }

    static private String exportResource(String resourceName) {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        String jarFolder = "";
        try {
            stream = VoiceLauncher.class.getResourceAsStream(resourceName);
            if (stream == null) {
                throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
            }
            int readBytes;
            byte[] buffer = new byte[4096];
            jarFolder = Paths.get("").toAbsolutePath().toString().replace('\\', '/');
            resStreamOut = new FileOutputStream(jarFolder + "/" + resourceName);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
                if (resStreamOut != null) {
                    resStreamOut.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jarFolder + resourceName;
    }

    public static VoiceLauncher getInstance() {
        return LazyInitializer.INSTANCE;
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
                signalClassChanged(command);
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

    public void terminate() {
        terminate = true;
    }

    private static class LazyInitializer {
        static final VoiceLauncher INSTANCE = new VoiceLauncher();
    }
}


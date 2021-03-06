package edu.wpi.cs3733d18.teamF.voice;

import com.github.fedy2.weather.YahooWeatherService;
import com.github.fedy2.weather.data.Channel;
import com.github.fedy2.weather.data.unit.DegreeUnit;
import edu.wpi.cs3733d18.teamF.graph.Map;
import edu.wpi.cs3733d18.teamF.graph.MapSingleton;
import edu.wpi.cs3733d18.teamF.graph.Node;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class VoiceCommandVerification extends Observable implements Observer {
    private final Boolean canSayCommand[] = {false};
    Map map = MapSingleton.getInstance().getMap();
    private ConcurrentLinkedQueue<String> commands = new ConcurrentLinkedQueue<>();
    private TTS voice = new TTS();

    private Timeline commandExecutor = new Timeline(new KeyFrame(Duration.millis(100), event -> {
        String command = commands.poll();
        if (command != null) {
            if (isActivation(command)) {
                canSayCommand[0] = true;
                signalClassChanged("Activate");
                new Timer(true).schedule(new TimerTask() {
                    @Override
                    public void run() {
                        canSayCommand[0] = false;
                    }
                }, 5000);
            }else if(isFire(command)){
                signalClassChanged("FIRE");
            } else {
                if (!canSayCommand[0]) {
                    return;
                }
                canSayCommand[0] = false;

                if (command.equals("HELP")) {
                    signalClassChanged("Help");
                    voice.speak("Here is the help menu");
                } else if (command.contains("DIRECTIONS") || command.contains("WHERE")) {
                    HashSet<Node> nodes = new HashSet<>();

                    if (command.contains("BATHROOM")) {
                        nodes = map.getNodes(node -> node.getNodeType().equals("REST"));
                        voice.speak("Here is the route to the nearest bathroom");
                    } else if (command.contains("EXIT")) {
                        nodes = map.getNodes(node -> node.getNodeType().equals("EXIT") && !node.getLongName().contains("Ambulance"));
                        voice.speak("Here is the route to the nearest exit");
                    } else if (command.contains("NEUROSCIENCE")) {
                        nodes = map.getNodes(node -> node.getLongName().contains("Neuroscience"));
                        voice.speak("Here is the route to neuroscience");
                    } else if (command.contains("ORTHOPEDICS") || command.contains("RHEMUTOLOGY")) {
                        nodes = map.getNodes(node -> node.getLongName().contains("Orthopedics") || node.getLongName().contains("Rhemutalogy"));
                        voice.speak("Here is the route to Orthopedics and Rhemutology");
                    } else if (command.contains("PARKING") || command.contains("GARAGE")) {
                        nodes = map.getNodes(node -> node.getLongName().contains("Parking") && node.getLongName().contains("Garage"));
                        voice.speak("Here is the route to the parking garage");
                    } else if (command.contains("ELEVATOR")) {
                        nodes = map.getNodes(node -> node.getNodeType().equals("ELEV"));
                        voice.speak("Here is the route to the nearest elevator");
                    } else if (command.contains("DENTIST") || command.contains("DENTISTRY") || command.contains("ORAL")) {
                        nodes = map.getNodes(node -> node.getLongName().contains("Dentistry"));
                        voice.speak("Here is the route to Dentistry and Oral Medicine");
                    } else if (command.contains("PLASTIC SURGERY")) {
                        nodes = map.getNodes(node -> node.getLongName().contains("Plastic Surgery"));
                        voice.speak("Here is the route to Plastic Surgery");
                    } else if (command.contains("RADIOLOGY")) {
                        nodes = map.getNodes(node -> node.getLongName().contains("Radiation Oncology"));
                        voice.speak("Here is the route to Radiology");
                    } else if (command.contains("NUCLEAR")) {
                        nodes = map.getNodes(node -> node.getLongName().contains("Nuclear Medicine"));
                        voice.speak("Here is the route to Nuclear Medicine");
                    } else if (command.contains("MRI")) {
                        nodes = map.getNodes(node -> node.getLongName().contains("MRI"));
                        voice.speak("Here is the route to the MRI");
                    } else if (command.contains("GARDEN") && command.contains("CAFE")) {
                        nodes = map.getNodes(node -> node.getLongName().contains("Garden Cafe") && node.getNodeType().equals("RETL"));
                        voice.speak("Here is the route to the garden cafe");
                    } else if (command.contains("CAFE")) {
                        nodes = map.getNodes(node -> node.getLongName().contains("Cafe") && node.getNodeType().equals("RETL"));
                        voice.speak("Here is the route to the nearest cafe");
                    } else if (command.contains("DUNCAN") && command.contains("REID")) {
                        nodes = map.getNodes(node -> node.getLongName().contains("Duncan Reid Conference Room"));
                        voice.speak("Here is the route to Duncan Reid Conference Room");
                    } else if (command.contains("LEE") && command.contains("BELL") && command.contains("BREAST")) {
                        nodes = map.getNodes(node -> node.getLongName().contains("Lee Bell Breast Center"));
                        voice.speak("Here is the route to the Lee Bell Breast Center");
                    } else if (command.contains("JEN") && command.contains("CENTER") && command.contains("PRIMARY")) {
                        nodes = map.getNodes(node -> node.getLongName().contains("Jen Center for Primary Care"));
                        voice.speak("Here is the route to the Jen Center for Primary Care");
                    } else if (command.contains("EAR") && command.contains("NOSE") && command.contains("THROAT")) {
                        nodes = map.getNodes(node -> node.getLongName().contains("Ear Nose & Throat"));
                        voice.speak("Here are directions to the Ear Nose and Throat Center");
                    }

                    if (nodes.size() == 1) {
                        signalClassChanged(nodes.iterator().next());
                    } else if (nodes.size() > 1) {
                        signalClassChanged(nodes);
                    }

                } else if (command.contains("STAIRS") && command.contains("DISABLE")) {
                    map.disableStairs();
                    signalClassChanged("DisableStairs");
                    voice.speak("Stairs are now disabled for path finding");
                } else if (command.contains("STAIRS") && command.contains("ENABLE")) {
                    map.enableStairs();
                    signalClassChanged("EnableStairs");
                    voice.speak("Stairs are now enabled for path finding");
                } else if (command.contains("ELEVATOR") && command.contains("DISABLE")) {
                    map.disableElevators();
                    signalClassChanged("DisableElevators");
                    voice.speak("Elevators are now disabled for path finding");
                } else if (command.contains("ELEVATOR") && command.contains("ENABLE")) {
                    map.enableElevators();
                    signalClassChanged("EnableStairs");
                    voice.speak("Elevators are now enabled for path finding");
                } else if (command.contains("WEATHER")) {
                    YahooWeatherService service = null;
                    try {
                        service = new YahooWeatherService();
                    } catch (JAXBException e) {
                        e.printStackTrace();
                    }
                    Channel channel = null;
                    try {
                        channel = service.getForecast("2523945", DegreeUnit.FAHRENHEIT);
                    } catch (JAXBException | IOException e) {
                        e.printStackTrace();
                    }
                    voice.speak(String.format("The temperature is %d degrees fahrenheit", channel.getItem().getCondition().getTemp()));
                    voice.speak(channel.getAtmosphere().toString());
                } else if (command.contains("RAP")) {
                    voice.speak("Boots and Cats and Boots and Cats and Boots and Cats and Boots and Cats and Boots" +
                            "and Cats and Boots and Cats and Boots and Cats and Boots and Cats and Boots and Cats and Boots");
                }
            }
        }
    }));

    public VoiceCommandVerification() {
        commandExecutor.setCycleCount(Timeline.INDEFINITE);
        commandExecutor.play();
    }

    public boolean isActivation(String command) {
        return command.contains("HELLO KIOSK") ||
                command.contains("HEY KIOSK");
    }

    public boolean isFire(String command){
        String delim = "[ ]";
        String[] split = command.split(delim);
        int counter = 0;
        for(String s : split){
            if(s.equals("FIRE")){
                counter++;
            }
        }
        return counter >= 3;
    }

    private void signalClassChanged(Object args) {
        this.setChanged();
        this.notifyObservers(args);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(o instanceof VoiceLauncher)) {
            return;
        }

        if (arg instanceof String) {
            commands.add((String) arg);
        }
    }
}

package edu.wpi.cs3733d18.teamF.gfx;

import javafx.scene.image.Image;

import java.util.HashMap;

public class ImageCacheSingleton {
    public static Image maps2D[] = {
            new Image("edu/wpi/cs3733d18/teamF/controller/BW2D Maps/00_thelowerlevel2.png"),
            new Image("edu/wpi/cs3733d18/teamF/controller/BW2D Maps/00_thelowerlevel1.png"),
            new Image("edu/wpi/cs3733d18/teamF/controller/BW2D Maps/00_thegroundfloor.png"),
            new Image("edu/wpi/cs3733d18/teamF/controller/BW2D Maps/01_thefirstfloor.png"),
            new Image("edu/wpi/cs3733d18/teamF/controller/BW2D Maps/02_thesecondfloor.png"),
            new Image("edu/wpi/cs3733d18/teamF/controller/BW2D Maps/03_thethirdfloor.png")
    };
    public static Image maps3D[] = {
            new Image("edu/wpi/cs3733d18/teamF/controller/Wireframes/L2-ICONS.png"),
            new Image("edu/wpi/cs3733d18/teamF/controller/Wireframes/L1-ICONS.png"),
            new Image("edu/wpi/cs3733d18/teamF/controller/Wireframes/1-ICONS.png"),
            new Image("edu/wpi/cs3733d18/teamF/controller/Wireframes/1-ICONS.png"),
            new Image("edu/wpi/cs3733d18/teamF/controller/Wireframes/2-ICONS.png"),
            new Image("edu/wpi/cs3733d18/teamF/controller/Wireframes/3-ICONS.png")
    };

    private HashMap<String, Image> stringImageHashMap = new HashMap<>();

    private ImageCacheSingleton() {

    }

    public Image getImage(String fileName) {
        if (stringImageHashMap.containsKey(fileName)) {
            return stringImageHashMap.get(fileName);
        }
        stringImageHashMap.put(fileName, new Image(fileName));
        return stringImageHashMap.get(fileName);
    }

    public ImageCacheSingleton getInstance() {
        return LazyInitializer.INSTANCE;
    }

    private static class LazyInitializer {
        static final ImageCacheSingleton INSTANCE = new ImageCacheSingleton();
    }
}

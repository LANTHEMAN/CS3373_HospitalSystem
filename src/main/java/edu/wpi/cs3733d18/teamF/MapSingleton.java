package com.github.CS3733_D18_Team_F_Project_0;

import com.github.CS3733_D18_Team_F_Project_0.db.DatabaseHandler;
import com.github.CS3733_D18_Team_F_Project_0.db.DatabaseSingleton;

public class MapSingleton {

    DatabaseHandler dbHandler;
    Map map;

    private MapSingleton() {
        this.dbHandler = DatabaseSingleton.getInstance().getDbHandler();
        this.map = new Map();
        dbHandler.trackAndInitItem(map);
    }

    private static class LazyInitializer {
        static final MapSingleton INSTANCE = new MapSingleton();
    }

    public static MapSingleton getInstance() {
        return LazyInitializer.INSTANCE;
    }

    public Map getMap(){
        return map;
    }

}


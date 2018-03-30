package com.github.CS3733_D18_Team_F_Project_0;

public class DatabaseSingleton {
    private DatabaseHandler dbHandler;

    private DatabaseSingleton() {
        dbHandler = new DatabaseHandler();
    }

    public static DatabaseSingleton getInstance() {
        return LazyInitializer.INSTANCE;
    }

    public DatabaseHandler getDbHandler() {
        return dbHandler;
    }

    private static class LazyInitializer {
        static final DatabaseSingleton INSTANCE = new DatabaseSingleton();
    }
}

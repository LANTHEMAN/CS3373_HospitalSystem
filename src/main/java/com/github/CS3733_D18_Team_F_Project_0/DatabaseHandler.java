package com.github.CS3733_D18_Team_F_Project_0;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseHandler {
    static private final String DatabaseURL = "jdbc:derby:database;create=true";
    private Connection connection;

    public DatabaseHandler() {
        connectToDatabase();
    }

    private void connectToDatabase() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            connection = DriverManager.getConnection(DatabaseURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package com.github.CS3733_D18_Team_F_Project_0;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static javafx.scene.input.KeyCode.F;

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

    public void runSQLScript(String script) {
        try {
            org.apache.derby.tools.ij.runScript(connection, getClass().getResourceAsStream(script)
                    , StandardCharsets.UTF_8.name(), System.out, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void readCSVIn(String csvFileName) {
        try {
            File csvFile = new File(getClass().getResource(csvFileName).getFile());
            CSVParser parser = CSVParser.parse(csvFile, StandardCharsets.UTF_8, CSVFormat.RFC4180);

            for (CSVRecord record : parser) {
                if (record.get(0).equals("nodeID")) {
                    continue;
                }
                // TODO store in database
                String name = record.get(0);
                double x = Double.parseDouble(record.get(1));
                double y = Double.parseDouble(record.get(2));
                String floor = record.get(3);
                String building = record.get(4);
                String nodeType = record.get(5);
                String longName = record.get(6);
                String shortName = record.get(7);
                addNode(name, x, y, floor, building, nodeType, longName, shortName);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public ResultSet runQuery(String query) {
        ResultSet result;
        try {
            Statement statement = connection.createStatement();
            result = statement.executeQuery(query);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void runAction(String action) {
        try {
            Statement statement = connection.createStatement();
            statement.execute(action);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNode(String id, double x, double y, String floor, String building, String nodeType, String longName, String shortName) {
        String query = "INSERT INTO NODE VALUES ("
                + "'" + id + "',"
                + x + ","
                + y + ","
                + "'" + floor + "',"
                + "'" + building + "',"
                + "'" + nodeType + "',"
                + "'" + longName + "',"
                + "'" + shortName + "'"
                + ")";
        System.out.println(query);
        runAction(query);
    }

}

package com.github.CS3733_D18_Team_F_Project_0;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

public class DummyGraph implements DatabaseItem {

    HashMap<String, DummyNode> nodes;

    @Override
    public void initDatabase(DatabaseHandler dbHandler) {
        try {
            //if the table does not yet exist, initialize the table and read in the csv files
            if(!dbHandler.tableExists("NODE")){
                try {
                    dbHandler.runSQLScript("init_node_db.sql");

                    // TODO make into function
                    File csvFile = new File(getClass().getResource("MapFNodes.csv").getFile());
                    CSVParser parser = CSVParser.parse(csvFile, StandardCharsets.UTF_8, CSVFormat.RFC4180);

                    for (CSVRecord record : parser) {
                        if (record.get(0).equals("nodeID")) {
                            continue;
                        }
                        String name = record.get(0);
                        double x = Double.parseDouble(record.get(1));
                        double y = Double.parseDouble(record.get(2));
                        String floor = record.get(3);
                        String building = record.get(4);
                        String nodeType = record.get(5);
                        String longName = record.get(6);
                        String shortName = record.get(7);

                        String query = "INSERT INTO NODE VALUES ("
                                + "'" + name + "',"
                                + x + ","
                                + y + ","
                                + "'" + floor + "',"
                                + "'" + building + "',"
                                + "'" + nodeType + "',"
                                + "'" + longName + "',"
                                + "'" + shortName + "'"
                                + ")";
                        dbHandler.runAction(query);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getTableName() {
        return "NODE";
    }

    @Override
    public void syncLocalFromDB(ResultSet resultSet) {

    }

    @Override
    public void syncDBFromLocal(DatabaseHandler dbHandler) {

    }

    @Override
    public void syncCSVFromDB(DatabaseHandler dbHandler) {

    }
}

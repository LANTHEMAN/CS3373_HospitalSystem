package com.github.CS3733_D18_Team_F_Project_0;

import com.github.CS3733_D18_Team_F_Project_0.db.DatabaseHandler;
import com.github.CS3733_D18_Team_F_Project_0.db.DatabaseItem;
import com.github.CS3733_D18_Team_F_Project_0.graph.Graph;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Map implements DatabaseItem {

    Graph graph;

    public Map() {
        graph = new Graph();
    }

    @Override
    public void initDatabase(DatabaseHandler dbHandler) {
        try {
            //if the table does not yet exist in the db, initialize it
            if (!dbHandler.tableExists("NODE")) {
                System.out.println("DB: Initializing NODE table entry");
                dbHandler.runSQLScript("init_node_db.sql");


                List<String> nodeFilePaths = Files.walk(Paths.get(dbHandler.getClass().getResource("map").toURI()))
                        .filter(Files::isRegularFile)
                        .filter(path -> path.getFileName().toString().contains("nodes.csv"))
                        .map(path -> path.getFileName().toString())
                        .collect(Collectors.toList());

                for (String nodeFilePath : nodeFilePaths) {
                    File csvFile = new File(dbHandler.getClass().getResource("map/" + nodeFilePath).toURI().getPath());
                    CSVParser parser = CSVParser.parse(csvFile, StandardCharsets.UTF_8, CSVFormat.RFC4180);

                    for (CSVRecord record : parser) {
                        if (record.get(0).contains("nodeID")) {
                            continue;
                        }
                        String name = record.get(0);
                        int x = Integer.parseInt(record.get(1));
                        int y = Integer.parseInt(record.get(2));
                        String floor = record.get(3);
                        String building = record.get(4);
                        String nodeType = record.get(5);
                        String longName = record.get(6);
                        String shortName = record.get(7);
                        String teamName = record.get(8);
                        int x3d = Integer.parseInt(record.get(9));
                        int y3d = Integer.parseInt(record.get(10));

                        String cmd = "INSERT INTO NODE VALUES ("
                                + "'" + name + "'"
                                + "," + x
                                + "," + y
                                + ",'" + floor + "'"
                                + ",'" + building + "'"
                                + ",'" + nodeType + "'"
                                + ",'" + longName + "'"
                                + ",'" + shortName + "'"
                                + ",'" + teamName + "'"
                                + "," + x3d
                                + "," + y3d
                                + ")";
                        dbHandler.runAction(cmd);
                    }
                }
            }


        } catch (SQLException | IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        try {
            //if the table does not yet exist in the db, initialize it
            if (!dbHandler.tableExists("EDGE")) {
                System.out.println("DB: Initializing EDGE table entry");
                dbHandler.runSQLScript("init_edge_db.sql");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public LinkedList<String> getTableNames() {
        return new LinkedList<>(Arrays.asList("NODE", "EDGE"));
    }

    @Override
    public void syncLocalFromDB(String tableName, ResultSet resultSet) {

    }

    @Override
    public void syncCSVFromDB(DatabaseHandler dbHandler) {

    }
}

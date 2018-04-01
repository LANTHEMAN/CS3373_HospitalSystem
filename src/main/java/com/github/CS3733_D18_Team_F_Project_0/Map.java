package com.github.CS3733_D18_Team_F_Project_0;

import com.github.CS3733_D18_Team_F_Project_0.db.DatabaseHandler;
import com.github.CS3733_D18_Team_F_Project_0.db.DatabaseItem;
import com.github.CS3733_D18_Team_F_Project_0.graph.Graph;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

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
            }
        } catch (SQLException e) {
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

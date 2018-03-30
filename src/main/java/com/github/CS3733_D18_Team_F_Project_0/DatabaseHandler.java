package com.github.CS3733_D18_Team_F_Project_0;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.HashSet;

/**
 * 
 */
public class DatabaseHandler {
    private String databaseURL = "jdbc:derby:database;create=true";
    private Connection connection = null;
    private HashSet<DatabaseItem> trackedItems = new HashSet<>();

    public DatabaseHandler() {
        connectToDatabase();
    }

    public DatabaseHandler(String directoryName) {
        databaseURL = "jdbc:derby:" + directoryName + ";create=true";
        connectToDatabase();
    }

    public void disconnectFromDatabase() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void trackAndInitItem(DatabaseItem dbItem) {
        trackedItems.add(dbItem);
        dbItem.initDatabase(this);
        syncLocalFromDB(dbItem);
    }

    public void syncLocalFromDB(DatabaseItem dbItem) {
        for (String table : dbItem.getTableNames()) {
            dbItem.syncLocalFromDB(table, runQuery("SELECT * FROM " + table));
        }
    }

    public void syncDBFromLocal(DatabaseItem dbItem) {
        dbItem.syncDBFromLocal(this);
    }

    public void syncCSVFromDB(DatabaseItem dbItem) {
        dbItem.syncCSVFromDB(this);
    }

    public boolean tableExists(String tableName) throws SQLException {
        DatabaseMetaData md = connection.getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);
        while (rs.next()) {
            if (rs.getString(3).equals(tableName)) {
                return true;
            }
        }
        return false;
    }

    public void runSQLScript(String script) {
        try {
            org.apache.derby.tools.ij.runScript(connection, getClass().getResourceAsStream(script)
                    , StandardCharsets.UTF_8.name(), System.out, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
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

    private void connectToDatabase() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            connection = DriverManager.getConnection(databaseURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

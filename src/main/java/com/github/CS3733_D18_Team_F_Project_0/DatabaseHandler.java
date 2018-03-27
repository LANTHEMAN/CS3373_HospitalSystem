package com.github.CS3733_D18_Team_F_Project_0;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.HashMap;

public class DatabaseHandler {
    static private final String DatabaseURL = "jdbc:derby:database;create=true";
    private Connection connection = null;
    private HashMap<String, DatabaseItem> trackedItems = new HashMap<>();

    public DatabaseHandler() {
        connectToDatabase();
    }

    public void trackItem(String name, DatabaseItem dbItem) {
        trackedItems.put(name, dbItem);
        dbItem.initDatabase(this);
        syncLocal(name);
    }

    public void syncLocal(String item) {
        trackedItems.get(item).syncLocalFromDB(runQuery("SELECT * FROM " + trackedItems.get(item).getTableName()));
    }

    public void syncDB(String item) {
        trackedItems.get(item).syncDBFromLocal(this);
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
            connection = DriverManager.getConnection(DatabaseURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

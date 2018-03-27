package com.github.CS3733_D18_Team_F_Project_0;

import org.junit.Test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class DerbyTest {
    @Test
    public void dummyTest() throws SQLException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.runSQLScript("init_node_db.sql");
        dbHandler.readCSVIn("MapFNodes.csv");
        
        ResultSet nodeSet = dbHandler.runQuery("SELECT * FROM NODE");

        ResultSetMetaData rsmd = nodeSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (nodeSet.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = nodeSet.getString(i);
                System.out.print(columnValue + " " + rsmd.getColumnName(i));
            }
            System.out.println("");
        }
    }
}

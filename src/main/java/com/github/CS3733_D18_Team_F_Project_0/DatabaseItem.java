package com.github.CS3733_D18_Team_F_Project_0;

import java.sql.ResultSet;
import java.util.LinkedList;

public interface DatabaseItem {
    // given access to the database, initialize the Table and load it from the csv
    public void initDatabase(DatabaseHandler dbHandler);

    // returns the name of the table
    public LinkedList<String> getTableNames();

    // given the entire table from the DB, sync locally
    public void syncLocalFromDB(String tableName, ResultSet resultSet);

    // given access to the database, sync on the database
    public void syncDBFromLocal(DatabaseHandler dbHandler);

    // given access to the database, sync the local csv file
    public void syncCSVFromDB(DatabaseHandler dbHandler);
}

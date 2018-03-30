package com.github.CS3733_D18_Team_F_Project_0;

import java.sql.ResultSet;
import java.util.LinkedList;

public interface DatabaseItem {
    // given access to the database, initialize the Table and load it from the csv
    void initDatabase(DatabaseHandler dbHandler);

    // returns the name of the table
    LinkedList<String> getTableNames();

    // given the entire table from the DB, sync locally
    void syncLocalFromDB(String tableName, ResultSet resultSet);

    // given access to the database, sync on the database
    void syncDBFromLocal(DatabaseHandler dbHandler);

    // given access to the database, sync the local csv file
    void syncCSVFromDB(DatabaseHandler dbHandler);
}

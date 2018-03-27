package com.github.CS3733_D18_Team_F_Project_0;

import java.sql.ResultSet;

public interface DatabaseItem {
    // given access to the database, initialize the Table and load it from the csv
    public void initRepository(DatabaseHandler dbHandler);

    // returns the name of the table
    public String getTableName();

    // given the entire table from the DB, sync locally
    public void syncLocal(ResultSet resultSet);

    // given access to the database, sync on the database
    public void syncDB(DatabaseHandler dbHandler);

    // given access to the database, sync the local csv file
    public void syncCSV(DatabaseHandler dbHandler);
}

package com.github.CS3733_D18_Team_F_Project_0;

import java.sql.ResultSet;
import java.util.LinkedList;

public class DummyGraph implements DatabaseItem {

    LinkedList<DummyNode> nodes;

    @Override
    public void initRepository(DatabaseHandler dbHandler) {

    }

    @Override
    public String getTableName() {
        return "Node";
    }

    @Override
    public void syncLocal(ResultSet resultSet) {

    }

    @Override
    public void syncDB(DatabaseHandler dbHandler) {

    }

    @Override
    public void syncCSV(DatabaseHandler dbHandler) {

    }
}

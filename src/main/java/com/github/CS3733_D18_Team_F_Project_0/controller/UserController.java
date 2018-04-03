package com.github.CS3733_D18_Team_F_Project_0.controller;

import com.github.CS3733_D18_Team_F_Project_0.db.DatabaseHandler;
import com.github.CS3733_D18_Team_F_Project_0.db.DatabaseItem;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserController implements DatabaseItem {
    ArrayList<User> users;
    UserController(){
        users = new ArrayList<User>();
    }

    public UserController(ArrayList<User> users) {
        this.users = users;
    }

    @Override
    public void initDatabase(DatabaseHandler dbHandler) {
        try{
            if(!dbHandler.tableExists("USER")){
                System.out.println("DB: Initializing USER table entry");
                dbHandler.runSQLScript("init_user_db.sql");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void syncLocalFromDB(String tableName, ResultSet resultSet) {
        try{
            while(resultSet.next()){
                
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public LinkedList<String> getTableNames() {
        return null;
    }

    @Override
    public void syncCSVFromDB(DatabaseHandler dbHandler) {

    }
}


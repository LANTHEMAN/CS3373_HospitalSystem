package com.github.CS3733_D18_Team_F_Project_0.controller;

import com.github.CS3733_D18_Team_F_Project_0.db.DatabaseHandler;
import com.github.CS3733_D18_Team_F_Project_0.db.DatabaseItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;



public class PermissionManager implements DatabaseItem {
    ArrayList<User> users;
    PermissionManager(){
        users = new ArrayList<User>();
    }

    public PermissionManager(ArrayList<User> users) {
        this.users = users;
    }

    @Override
    public void initDatabase(DatabaseHandler dbHandler) {
        try{
            if(!dbHandler.tableExists("HUSER")){
                System.out.println("DB: Initializing HUSERS table entry");
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
                String username = resultSet.getString(1);
                String password = resultSet.getString(2);
                String type = resultSet.getString(3);
                User newUser = new User(username,password,type);
                users.add(newUser);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public LinkedList<String> getTableNames() {
        return new LinkedList<>(Arrays.asList("HUSER"));
    }

    @Override
    public void syncCSVFromDB(DatabaseHandler dbHandler) {}


}


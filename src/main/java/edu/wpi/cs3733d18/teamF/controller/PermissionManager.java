package edu.wpi.cs3733d18.teamF.controller;

import edu.wpi.cs3733d18.teamF.db.DatabaseHandler;
import edu.wpi.cs3733d18.teamF.db.DatabaseItem;

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
                String firstname = resultSet.getString(3);
                String lastname = resultSet.getString(4);
                String privilege = resultSet.getString(5);
                String occupation = resultSet.getString(6);
                User newUser = new User(username,password,firstname, lastname, privilege, occupation);
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


package com.github.CS3733_D18_Team_F_Project_0.controller;

import com.github.CS3733_D18_Team_F_Project_0.db.DatabaseHandler;
import com.github.CS3733_D18_Team_F_Project_0.db.DatabaseSingleton;

public class PermissionSingleton {
    DatabaseHandler dbHandler;
    PermissionManager users;
    String userPrivilege;
    private PermissionSingleton() {
        this.dbHandler = DatabaseSingleton.getInstance().getDbHandler();
        this.users = new PermissionManager();
        dbHandler.trackAndInitItem(users);
        userPrivilege = Privilege.GUEST;
    }
    private static class loginHelper{
        static final PermissionSingleton INSTANCE = new PermissionSingleton();
    }
    public static class Privilege{
        public static final String GUEST = "Guest";
        public static final String ADMIN = "Admin";
        public static final String SYSADMIN = "System Admin";
    }
    public static PermissionSingleton getInstance() {
        return loginHelper.INSTANCE;
    }

    public PermissionManager getUsers(){
        return users;
    }
    public boolean isAdmin(){
        if(userPrivilege.equals(Privilege.ADMIN)){
            return true;
        }
        return false;
    }
    public boolean login(String uname, String psword){
        return true;
    }
}

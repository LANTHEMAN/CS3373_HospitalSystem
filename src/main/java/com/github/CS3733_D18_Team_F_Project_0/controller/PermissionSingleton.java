package com.github.CS3733_D18_Team_F_Project_0.controller;

import com.github.CS3733_D18_Team_F_Project_0.db.DatabaseHandler;
import com.github.CS3733_D18_Team_F_Project_0.db.DatabaseSingleton;

public class PermissionSingleton {
    DatabaseHandler dbHandler;
    PermissionManager users;
    private PermissionSingleton() {
        this.dbHandler = DatabaseSingleton.getInstance().getDbHandler();
        this.users = new PermissionManager();
        dbHandler.trackAndInitItem(users);
    }
    private static class loginHelper{
        static final PermissionSingleton INSTANCE = new PermissionSingleton();
    }
    public static PermissionSingleton getInstance() {
        return loginHelper.INSTANCE;
    }
}

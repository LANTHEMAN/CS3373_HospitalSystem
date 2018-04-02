package com.github.CS3733_D18_Team_F_Project_0.controller;

import com.github.CS3733_D18_Team_F_Project_0.db.DatabaseHandler;
import com.github.CS3733_D18_Team_F_Project_0.db.DatabaseSingleton;

public class LoginSingleton {
    DatabaseHandler dbHandler;
    private LoginSingleton() {

    }
    private static class loginHelper{
        static final LoginSingleton INSTANCE = new LoginSingleton();
    }
    public static LoginSingleton getInstance() {
        return loginHelper.INSTANCE;
    }
}

package com.github.CS3733_D18_Team_F_Project_0.controller;

import com.github.CS3733_D18_Team_F_Project_0.db.DatabaseHandler;
import com.github.CS3733_D18_Team_F_Project_0.db.DatabaseSingleton;
import com.github.CS3733_D18_Team_F_Project_0.sr.ServiceRequestSingleton;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PermissionSingleton {
    DatabaseHandler dbHandler;
    PermissionManager pmanage;
    String userPrivilege;
    String currUser;

    private PermissionSingleton() {
        this.dbHandler = DatabaseSingleton.getInstance().getDbHandler();
        this.pmanage = new PermissionManager();
        dbHandler.trackAndInitItem(pmanage);
        userPrivilege = Privilege.GUEST;
        currUser = "Guest";
        if(!userExist("Admin")) {
            addEmployee(new Employee("Admin", "1234", "Admin", "Default", "Admin", "Admin"));
            ServiceRequestSingleton.getInstance().addOccupationLanguageInterpreter("Admin");
            ServiceRequestSingleton.getInstance().addOccupationReligiousServices("Admin");
        }
        if(!userExist("SysAdmin")) {
            addEmployee(new Employee("SysAdmin", "1234", "System Admin", "Sys", "Admin", "System Admin"));
            ServiceRequestSingleton.getInstance().addOccupationLanguageInterpreter("System Admin");
            ServiceRequestSingleton.getInstance().addOccupationReligiousServices("System Admin");
        }

    }

    private static class loginHelper{
        static final PermissionSingleton INSTANCE = new PermissionSingleton();
    }

    public static class Privilege{
        public static final String GUEST = "Guest";
        public static final String ADMIN = "Admin";
        public static final String SYSADMIN = "System Admin";
        public static final String STAFF = "Staff";
    }

    public static PermissionSingleton getInstance() {
        return loginHelper.INSTANCE;
    }

    public PermissionManager getPermissionManager(){
        return pmanage;
    }
    public boolean isAdmin(){
        if(userPrivilege.equals(Privilege.ADMIN) || userPrivilege.equals(Privilege.SYSADMIN)){
            return true;
        }
        return false;
    }
    public boolean login(String uname, String psword) {
        for (User u : pmanage.users) {
            if (u.uname.equals(uname)) {
                if (u.getPsword().equals(psword)) {
                    userPrivilege = getPrivilege(u.getType());
                    currUser = uname;
                    return true;
                }
            }
        }
        return false;
    }
    public void logout(){
        userPrivilege = Privilege.GUEST;
        currUser = "Guest";
    }
    private static String getPrivilege(String type){
        if(type.equals("sysadmin")){
            return Privilege.SYSADMIN;
        }
        else if(type.equals("admin")){
            return Privilege.ADMIN;
        }
        else{
            return Privilege.GUEST;
        }
    }

    public String getCurrUser() {
        return currUser;
    }

    public void addUser(User u) {
        pmanage.users.add(u);
        String sql = "INSERT INTO HUser"
                + " VALUES(username, password, privilege) ('" + u.getUname()
                + "', '" + u.getPsword()
                + "', '" + u.getType()
                +"')";
        dbHandler.runAction(sql);
    }

    public void addEmployee(Employee u) {
        pmanage.users.add(u);
        String sql = "INSERT INTO HUser"
                + " VALUES ('" + u.getUname()
                + "', '" + u.getPsword()
                + "', '" + u.getType()
                + "', '" + u.getFirstName()
                + "', '" + u.getLastName()
                + "', '" + u.getOccupation()
                + "')";
        dbHandler.runAction(sql);
    }

    public boolean userExist(String username){
        ResultSet rs;
        String sql = "SELECT * FROM HUser WHERE username = '" + username + "'";
        try {
            rs = dbHandler.runQuery(sql);

            if(!rs.next()){
                rs.close();
                return false;
            }else{
                rs.close();
                return true;
            }



        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}

package edu.wpi.cs3733d18.teamF.controller;

import edu.wpi.cs3733d18.teamF.db.DatabaseHandler;
import edu.wpi.cs3733d18.teamF.db.DatabaseSingleton;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

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

        if (!userExist("admin")) {
            addUser(new User("admin", "admin", "Admin", "Default", "Admin", "Admin", ""));
            /*
            if (!ServiceRequestSingleton.getInstance().isInTable("admin", "LanguageInterpreter")) {
                ServiceRequestSingleton.getInstance().addUsernameLanguageInterpreter("admin");
                ServiceRequestSingleton.getInstance().addUsernameReligiousServices("admin");
                ServiceRequestSingleton.getInstance().addUsernameSecurityRequest("admin");
            }
            */
        }

        if(!userExist("tdubuke")){
            addUser(new User("tdubuke", "password", "Tyler", "Dubuke",
                    "Admin", "IT", "0c39c6a5-6293-4ccd-af1a-b86e42d2fc73"));
        }

        if (!userExist("staff")) {

            /*addUser(new User("staff", "staff", "Staff", "Member", "Staff", "Nurse"));
            if (!ServiceRequestSingleton.getInstance().isInTable("staff", "LanguageInterpreter")) {
                ServiceRequestSingleton.getInstance().addUsernameLanguageInterpreter("staff");
                ServiceRequestSingleton.getInstance().addUsernameReligiousServices("staff");
            }
            */
        }

    }

    public static PermissionSingleton getInstance() {
        return loginHelper.INSTANCE;
    }

    private static String getPrivilege(String type) {
        if (type.equals("Admin")) {
            return Privilege.ADMIN;
        } else if (type.equals("Staff")) {
            return Privilege.STAFF;
        } else {
            return Privilege.GUEST;
        }
    }

    public PermissionManager getPermissionManager() {
        return pmanage;
    }

    public boolean isAdmin() {
        return (userPrivilege.equals(Privilege.ADMIN));
    }

    public boolean login(String uname, String psword) {
        for (User u : pmanage.users) {
            if (u.uname.equals(uname)) {
                String encryption = Encryption.encryptSHA256(psword);
                if (u.getPsword().equals(encryption)) {
                    userPrivilege = getPrivilege(u.getType());
                    currUser = uname;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean forceLogin(String uname){
        for (User u : pmanage.users) {
            if (u.uname.equals(uname)) {
                userPrivilege = getPrivilege(u.getType());
                currUser = uname;
                return true;
            }
        }
        return false;
    }

    public void logout() {
        userPrivilege = Privilege.GUEST;
        currUser = "Guest";
    }

    public String getCurrUser() {
        return currUser;
    }

    public void addUser(User u) {
        pmanage.users.add(u);
        String sql = "INSERT INTO HUser"
                + " VALUES ('" + u.getUname()
                + "', '" + u.getPsword()
                + "', '" + u.getFirstName()
                + "', '" + u.getLastName()
                + "', '" + u.getPrivilege()
                + "', '" + u.getOccupation()
                + "', '" + u.getFaceID()
                + "')";
        dbHandler.runAction(sql);
    }

    public void removeUser(User u) {
        String sql = "DELETE FROM HUser WHERE username = '" + u.getUname() + "'";
        dbHandler.runAction(sql);
        sql = "SELECT * FROM HUser";
        ResultSet resultSet = PermissionSingleton.getInstance().dbHandler.runQuery(sql);
        pmanage.syncLocalFromDB("HUser", resultSet);
    }

    public void updateUser(User u) {
        String sql = "UPDATE HUser SET password = '" + u.getPsword()
                + "', firstName = '" + u.getFirstName()
                + "', lastName = '" + u.getLastName()
                + "', privilege = '" + u.getPrivilege()
                + "', occupation = '" + u.getOccupation()
                + "' WHERE username = '" + u.getUname() + "'";
        dbHandler.runAction(sql);
        sql = "SELECT * FROM HUser";
        ResultSet resultSet = PermissionSingleton.getInstance().dbHandler.runQuery(sql);
        pmanage.syncLocalFromDB("HUser", resultSet);
    }

    public boolean userExist(String username) {
        ResultSet rs;
        String sql = "SELECT * FROM HUser WHERE username = '" + username + "'";
        try {
            rs = dbHandler.runQuery(sql);

            if (!rs.next()) {
                rs.close();
                return false;
            } else {
                rs.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static class loginHelper {
        static final PermissionSingleton INSTANCE = new PermissionSingleton();
    }

    public static class Privilege {
        public static final String GUEST = "Guest";
        public static final String ADMIN = "Admin";
        public static final String STAFF = "Staff";
    }

    public HashMap<String, String> getUserAndFace(){
        HashMap<String, String> unameFace = new HashMap<>();
        for(User u : PermissionSingleton.getInstance().getPermissionManager().users){
            unameFace.put(u.uname, u.getFaceID());
        }
        return unameFace;
    }

    public String getUserPrivilege() {
        return userPrivilege;
    }


}

package edu.wpi.cs3733d18.teamF.controller;

import edu.wpi.cs3733d18.teamF.db.DatabaseHandler;
import edu.wpi.cs3733d18.teamF.db.DatabaseSingleton;
import edu.wpi.cs3733d18.teamF.sr.ServiceRequestSingleton;

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

        if (!userExist("admin")) {
            addEmployee(new Employee("admin", "admin", "Admin", "Default", "Admin", "Admin"));
            ServiceRequestSingleton.getInstance().addOccupationLanguageInterpreter("Admin");
            ServiceRequestSingleton.getInstance().addOccupationReligiousServices("Admin");
        }

        if (!userExist("staff")) {
            addEmployee(new Employee("staff", "1234", "Staff", "Staff", "Member", "Nurse"));
            ServiceRequestSingleton.getInstance().addOccupationLanguageInterpreter("System Admin");
            ServiceRequestSingleton.getInstance().addOccupationReligiousServices("System Admin");
        }

    }

    public static PermissionSingleton getInstance() {
        return loginHelper.INSTANCE;
    }

    private static String getPrivilege(String type) {
        if (type.equals("System Admin")) {
            return Privilege.SYSADMIN;
        } else if (type.equals("Admin")) {
            return Privilege.ADMIN;
        } else if(type.equals("Staff")){
            return Privilege.STAFF;
        } else {
            return Privilege.GUEST;
        }
    }

    public PermissionManager getPermissionManager() {
        return pmanage;
    }

    public boolean isAdmin() {
        return (userPrivilege.equals(Privilege.ADMIN) || userPrivilege.equals(Privilege.SYSADMIN));
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
                + " VALUES(username, password, privilege) ('" + u.getUname()
                + "', '" + u.getPsword()
                + "', '" + u.getType()
                + "')";
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
        public static final String SYSADMIN = "System Admin";
        public static final String STAFF = "Staff";
    }

    public String getUserPrivilege() {
        return userPrivilege;
    }
}
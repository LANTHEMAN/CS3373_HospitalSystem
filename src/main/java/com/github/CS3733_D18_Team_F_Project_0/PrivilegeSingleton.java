package com.github.CS3733_D18_Team_F_Project_0;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class PrivilegeSingleton implements DatabaseItem {
    private static PrivilegeSingleton ourInstance = new PrivilegeSingleton();
    DatabaseHandler dbHandler;
    ArrayList<ServiceRequest> listOfRequests;
    private int id = 0;

    private PrivilegeSingleton() {
        // initialize this class with the database
        dbHandler = DatabaseSingleton.getInstance().getDbHandler();
        dbHandler.trackAndInitItem(this);
    }

    public static PrivilegeSingleton getInstance() {
        return ourInstance;
    }

    public int generateNewID() {
        int uniqueID = id;
        id++;
        return uniqueID;
    }

    private void updateStatus(ServiceRequest s) {
        String sql = "UPDATE ServiceRequest SET status = " + s.getStatus() + " WHERE id = " + s.getId() + ";";
        dbHandler.runAction(sql);
    }

    public void sendServiceRequest(ServiceRequest s) {
        String sql = "INSERT ALL into ServiceRequest VALUES (" + generateNewID()
                + ", " + s.getType()
                + ", " + s.getDestination().getNodeID()
                + ", " + s.getDescription()
                + ", " + s.getPriority()
                + ");";
        dbHandler.runAction(sql);
    }

    public ServiceRequest setComplete(ServiceRequest s) {
        s.setStatus("Request Complete");
        this.updateStatus(s);
        return s;
    }

    public ServiceRequest setInProgress(ServiceRequest s) {
        s.setStatus("In Progress");
        this.updateStatus(s);
        return s;
    }

    public ResultSet getRequests() {
        String sql = "SELECT * FROM ServiceRequest;";
        return dbHandler.runQuery(sql);
    }

    public ResultSet getRequestsOfType(String type) {
        String sql = "SELECT * FROM ServiceRequest WHERE type = '" + type + "';";
        return dbHandler.runQuery(sql);
    }

    public ResultSet getRequestsOfStatus(String status) {
        String sql = "SELECT * FROM ServiceRequest WHERE status = '" + status + "';";
        return dbHandler.runQuery(sql);
    }

    public ResultSet getRequestsOfPriority(int p) {
        String sql = "SELECT * FROM ServiceRequest WHERE priority = " + p + ";";
        return dbHandler.runQuery(sql);
    }


    @Override
    public void initDatabase(DatabaseHandler dbHandler) {
        dbHandler.runSQLScript("init_SR_db.sql");
    }

    @Override
    public LinkedList<String> getTableNames() {
        return new LinkedList<>(Arrays.asList("SERVICEREQUEST"));
    }

    @Override
    public void syncLocalFromDB(String tableName, ResultSet resultSet) {
        try{
            if(tableName.equals("SERVICEREQUEST")){
                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    String type = resultSet.getString(2);
                    String nodeID = resultSet.getString(3);
                    String instructions = resultSet.getString(4);
                    int priority = resultSet.getInt(5);

                    // TODO create ServiceRequest
                    // listOfRequests.add()
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void syncCSVFromDB(DatabaseHandler dbHandler) {
        // intentionally left empty
    }
}

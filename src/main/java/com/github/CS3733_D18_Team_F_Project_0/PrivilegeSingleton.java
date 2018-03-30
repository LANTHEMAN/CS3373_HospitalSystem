package com.github.CS3733_D18_Team_F_Project_0;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.util.ArrayList;

public class PrivilegeSingleton {
    private static PrivilegeSingleton ourInstance = new PrivilegeSingleton();
    private int id = 0;
    ArrayList<ServiceRequest> listOfRequests;

    public static PrivilegeSingleton getInstance() {
        return ourInstance;
    }

    private PrivilegeSingleton() {
    }

    public static PrivilegeSingleton getOurInstance() {
        return ourInstance;
    }

    public int getID(){
        int uniqueID = id;
        id++;
        return uniqueID;
    }

    private void updateStatus(ServiceRequest s){
        String sql = "UPDATE ServiceRequest SET status = " + s.getStatus() + " WHERE id = " + s.getId() + ";";
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.runQuery(sql);

    }

    public void sendServiceRequest(ServiceRequest s){
        String sql = "INSERT ALL into ServiceRequest VALUES (" + getID() + ", " + s.getType() + ", " + s.getDestination().getNodeID() + ", " + s.getDescription() + ", " + s.getPriority() + ");";
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.runQuery(sql);

    }

    public ServiceRequest setComplete(ServiceRequest s){
        s.setStatus("Request Complete");
        this.updateStatus(s);
        return s;
    }

    public ServiceRequest setInProgress(ServiceRequest s){
        s.setStatus("In Progress");
        this.updateStatus(s);
        return s;
    }

    public ResultSet getRequests(){
        String sql = "SELECT * FROM ServiceRequest;";
        DatabaseHandler dbHandler = new DatabaseHandler();
        return dbHandler.runQuery(sql);
    }

    public ResultSet getRequestsOfType(String type){
        String sql = "SELECT * FROM ServiceRequest WHERE type = '" + type + "';";
        DatabaseHandler dbHandler = new DatabaseHandler();
        return dbHandler.runQuery(sql);
    }

    public ResultSet getRequestsOfStatus(String status){
        String sql = "SELECT * FROM ServiceRequest WHERE status = '" + status + "';";
        DatabaseHandler dbHandler = new DatabaseHandler();
        return dbHandler.runQuery(sql);
    }

    public ResultSet getRequestsOfPriority(int p){
        String sql = "SELECT * FROM ServiceRequest WHERE priority = " + p + ";";
        DatabaseHandler dbHandler = new DatabaseHandler();
        return dbHandler.runQuery(sql);
    }





}

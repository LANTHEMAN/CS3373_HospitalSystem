package com.github.CS3733_D18_Team_F_Project_0;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PrivilegeSingleton implements DatabaseItem {
    private static PrivilegeSingleton ourInstance = new PrivilegeSingleton();

    private DatabaseHandler dbHandler;
    ArrayList<ServiceRequest> listOfRequests;
    private int id = 0;

    // just for testing
    private static HashMap<DatabaseHandler, PrivilegeSingleton> testDatabases = new HashMap<>();

    private PrivilegeSingleton() {
        // initialize this class with the database
        dbHandler = DatabaseSingleton.getInstance().getDbHandler();
        dbHandler.trackAndInitItem(this);

        // TODO get largest ID from database
    }

    private PrivilegeSingleton(DatabaseHandler dbHandler){
        this.dbHandler = dbHandler;
        dbHandler.trackAndInitItem(this);
    }

    public static PrivilegeSingleton getInstance() {
        return ourInstance;
    }
    public static PrivilegeSingleton getInstance(DatabaseHandler dbHandler){
        if(testDatabases.containsKey(dbHandler)){
            return testDatabases.get(dbHandler);
        }
        testDatabases.put(dbHandler, new PrivilegeSingleton(dbHandler));
        return testDatabases.get(dbHandler);
    }

    public int generateNewID() {
        return id++;
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
        dbHandler.runSQLScript("init_node_db.sql");
        dbHandler.runSQLScript("init_sr_db.sql");
    }

    @Override
    public LinkedList<String> getTableNames() {
        return new LinkedList<>(Arrays.asList("ServiceRequest"));
    }

    @Override
    public void syncLocalFromDB(String tableName, ResultSet resultSet) {
        try{
            if(tableName.equals("ServiceRequest")){
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

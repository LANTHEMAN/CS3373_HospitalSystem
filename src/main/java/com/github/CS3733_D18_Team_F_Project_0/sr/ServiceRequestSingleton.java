package com.github.CS3733_D18_Team_F_Project_0.sr;

import com.github.CS3733_D18_Team_F_Project_0.db.DatabaseHandler;
import com.github.CS3733_D18_Team_F_Project_0.db.DatabaseItem;
import com.github.CS3733_D18_Team_F_Project_0.db.DatabaseSingleton;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ServiceRequestSingleton implements DatabaseItem {
    private static ServiceRequestSingleton ourInstance = new ServiceRequestSingleton();

    private DatabaseHandler dbHandler;
    private ArrayList<ServiceRequest> listOfRequests = new ArrayList<>();
    private int id = 0;

    // just for testing
    private static HashMap<DatabaseHandler, ServiceRequestSingleton> testDatabases = new HashMap<>();

    private ServiceRequestSingleton() {
        // initialize this class with the database
        dbHandler = DatabaseSingleton.getInstance().getDbHandler();
        dbHandler.trackAndInitItem(this);

        // TODO get largest ID from database
    }

    private ServiceRequestSingleton(DatabaseHandler dbHandler){
        this.dbHandler = dbHandler;
        dbHandler.trackAndInitItem(this);
    }

    public static ServiceRequestSingleton getInstance() {
        return ourInstance;
    }
    public static ServiceRequestSingleton getInstance(DatabaseHandler dbHandler){
        if(testDatabases.containsKey(dbHandler)){
            return testDatabases.get(dbHandler);
        }
        testDatabases.put(dbHandler, new ServiceRequestSingleton(dbHandler));
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
        String sql = "INSERT ALL into ServiceRequest VALUES (" + s.getId()
                + ", " + s.getType()
                + ", " + s.getFirstName()
                + ", " + s.getLastName()
                + ", " + s.getLocation()
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
        if(dbHandler != DatabaseSingleton.getInstance().getDbHandler()){
            initDatabase(DatabaseSingleton.getInstance().getDbHandler());
        }
    }

    @Override
    public LinkedList<String> getTableNames() {
        return new LinkedList<>(Collections.singletonList("SERVICEREQUEST"));
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

    public ArrayList<ServiceRequest> getListOfRequests() {
        return listOfRequests;
    }

    private void setListOfRequests(ArrayList<ServiceRequest> listOfRequests) {
        this.listOfRequests = listOfRequests;
    }

    public void addServiceRequest(ServiceRequest s){
        int listSize = this.listOfRequests.size();
        int flag = 0;
        ArrayList<ServiceRequest> newList = new ArrayList<>();
        if (listSize == 0) {
            newList.add(0, s);
        } else {
            for (ServiceRequest i : this.listOfRequests) {
                if (i.getId() > s.getId() && flag == 0) {
                    newList.add(s);
                    flag++;
                } else {
                    newList.add(i);
                }
            }
            if (flag == 0) {
                newList.add(s);
            }
        }

        this.setListOfRequests(newList);
    }
}

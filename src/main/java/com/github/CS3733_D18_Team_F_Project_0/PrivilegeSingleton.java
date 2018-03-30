package com.github.CS3733_D18_Team_F_Project_0;

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

    private void update(ServiceRequest s){

    }

    public ServiceRequest setComplete(ServiceRequest s){
        s.setStatus("Request Complete");
        this.update(s);
        return s;
    }

    public ServiceRequest setInProgress(ServiceRequest s){
        s.setStatus("In Progress");
        this.update(s);
        return s;
    }

    public void getRequests(){
    }


}

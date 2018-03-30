package com.github.CS3733_D18_Team_F_Project_0;

import com.github.CS3733_D18_Team_F_Project_0.graph.Node;

public abstract class ServiceRequest {
    protected String type;
    protected Integer id;
    protected Node destination;
    protected String description;
    protected String status;

    public ServiceRequest(Node destination, String description) {
        this.id = PrivilegeSingleton.getOurInstance().getID();
        this.destination = destination;
        this.description = description;
        this.status = "Incomplete";
    }

    public String getType() {
        return type;
    }

    public Integer getId() {
        return id;
    }

    public Node getDestination() {
        return destination;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    protected void setType(String type){
        this.type = type;
    }
}

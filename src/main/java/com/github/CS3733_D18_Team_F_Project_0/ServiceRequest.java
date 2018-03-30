package com.github.CS3733_D18_Team_F_Project_0;

import com.github.CS3733_D18_Team_F_Project_0.graph.Node;

public abstract class ServiceRequest {
    private String type;
    private Integer id;
    private Node destination;
    private String description;
    private String status;
    private int priority;


    public ServiceRequest(String type, Node destination, String description) {
        this.type = type;
        this.id = PrivilegeSingleton.getInstance().generateNewID();
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

    public int getPriority() { return priority; }

    public void setPriority(int priority) { this.priority = priority;}

    public void setStatus(String status) {
        this.status = status;
    }

    protected void setType(String type){
        this.type = type;
    }

    public void setDescription(String s) {this.description = description;}

    public abstract void parseIntoDescription(String s);
}

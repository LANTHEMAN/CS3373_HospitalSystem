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
        this(type, destination, description, ServiceRequestSingleton.getInstance().generateNewID(), "Incomplete", 0);
    }

    public ServiceRequest(String type, Node destination, String description, int id, String status, int priority) {
        this.type = type;
        this.id = id;
        this.destination = destination;
        this.description = description;
        this.status = status;
        this.priority = priority;
    }

    public String getType() {
        return type;
    }

    protected void setType(String type) {
        this.type = type;
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

    public void setDescription(String s) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public abstract void parseIntoDescription(String s);
}

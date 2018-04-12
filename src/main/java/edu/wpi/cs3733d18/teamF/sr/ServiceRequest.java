package edu.wpi.cs3733d18.teamF.sr;



public abstract class ServiceRequest {
    private String type;
    private Integer id;
    private String firstName;
    private String lastName;
    private String location;
    private String description;
    private String status;
    private int priority;
    private String completedBy;
    private String assignedTo;


    public ServiceRequest(String type, String firstName, String lastName, String location, String description, String status, int priority) {
        this.type = type;
        this.id = ServiceRequestSingleton.getInstance().generateNewID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.description = description;
        this.status = status;
        this.priority = priority;
    }

    public ServiceRequest(String type, int id, String firstName, String lastName, String location, String description, String status, int priority){
        this.type = type;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.description = description;
        this.status = status;
        this.priority = priority;
    }

    public ServiceRequest(String type, int id, String firstName, String lastName, String location, String description, String status, int priority, String assignedTo){
        this.type = type;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.assignedTo = assignedTo;
    }

    public ServiceRequest(String type, int id, String firstName, String lastName, String location, String description, String status, int priority, String assignedTo, String completedBy){
        this.type = type;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.assignedTo = assignedTo;
        this.completedBy = completedBy;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String s) {
        this.description = s;
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

    public String getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(String completedBy) {
        this.completedBy = completedBy;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
}

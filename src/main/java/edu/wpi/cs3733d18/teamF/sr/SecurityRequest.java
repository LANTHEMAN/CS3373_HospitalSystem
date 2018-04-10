package edu.wpi.cs3733d18.teamF.sr;

public class SecurityRequest extends ServiceRequest {
    private String security;

    public SecurityRequest(String type, String firstName, String lastName, String location, String description, String status, int priority) {
        super("Security Request", firstName, lastName, location, description, status, priority);
        this.security = security;
    }

    public SecurityRequest(String type, int id, String firstName, String lastName, String location, String description, String status, int priority) {
        super("Security Request", id, firstName, lastName, location, description, status, priority);
        this.security = security;
    }

    public SecurityRequest(String type, int id, String firstName, String lastName, String location, String description, String status, int priority, String completedBy) {
        super("Security Request", id, firstName, lastName, location, description, status, priority, completedBy);
        this.security = security;
    }
    public String getSecurity() {
        return security;
    }

    public void setSecurity(String religion) {
        this.security = religion;
    }
}

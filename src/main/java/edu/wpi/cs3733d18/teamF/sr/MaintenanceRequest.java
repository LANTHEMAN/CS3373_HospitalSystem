package edu.wpi.cs3733d18.teamF.sr;

import java.sql.Timestamp;

public class MaintenanceRequest extends ServiceRequests{
    private String situation;

    public MaintenanceRequest(String firstName, String lastName, String location, String description, String status, int priority, String situation, String staffNeeded) {
        super("Maintenance Request", firstName, lastName, location, description, status, priority, staffNeeded);
        this.situation = situation;
    }

    public MaintenanceRequest(int id, String firstName, String lastName, String location, String description, String status, int priority, String situation, String staffNeeded) {
        super("Language Interpreter", id, firstName, lastName, location, description, status, priority, staffNeeded);
        this.situation = situation;
    }

    public MaintenanceRequest(int id, String firstName, String lastName, String location, String description, String status, int priority, String situation, String completedBy, Timestamp createdOn, Timestamp started, Timestamp completedOn, String staffNeeded) {
        super("Language Interpreter", id, firstName, lastName, location, description, status, priority, completedBy, createdOn, started, completedOn, staffNeeded);
        this.situation = situation;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }
}

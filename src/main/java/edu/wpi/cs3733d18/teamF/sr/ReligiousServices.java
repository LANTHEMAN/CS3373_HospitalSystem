package edu.wpi.cs3733d18.teamF.sr;

public class ReligiousServices extends ServiceRequest{
    private String religion;

    public ReligiousServices(String firstName, String lastName, String location, String description, String status, int priority, String religion) {
        super("Religious Services", firstName, lastName, location, description, status, priority);
        this.religion = religion;
    }

    public ReligiousServices(int id, String firstName, String lastName, String location, String description, String status, int priority, String religion) {
        super("Religious Services", id, firstName, lastName, location, description, status, priority);
        this.religion = religion;
    }

    public ReligiousServices(int id, String firstName, String lastName, String location, String description, String status, int priority, String religion, String completedBy) {
        super("Religious Services", id, firstName, lastName, location, description, status, priority, completedBy);
        this.religion = religion;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

}

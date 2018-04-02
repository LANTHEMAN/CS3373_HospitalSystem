package com.github.CS3733_D18_Team_F_Project_0.sr;

import com.github.CS3733_D18_Team_F_Project_0.graph.Node;

public class ReligiousServices extends ServiceRequest{
    private String religion;

    public ReligiousServices(String firstName, String lastName, String location, String description, int priority, String religion) {
        super("Religious Services", firstName, lastName, location, description, priority);
        this.religion = religion;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    @Override
    public void parseIntoDescription(String s) {

    }
}

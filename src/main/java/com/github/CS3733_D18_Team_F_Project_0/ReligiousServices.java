package com.github.CS3733_D18_Team_F_Project_0;

import com.github.CS3733_D18_Team_F_Project_0.graph.Node;

public class ReligiousServices extends ServiceRequest{
    private String religion;

    public ReligiousServices(String type, Node destination, String description, String religion) {
        super(destination, description);
        this.setType("Religious Services");
        this.religion = religion;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }
}

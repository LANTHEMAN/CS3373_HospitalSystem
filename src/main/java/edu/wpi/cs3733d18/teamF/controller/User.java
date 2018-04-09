package edu.wpi.cs3733d18.teamF.controller;


public class User {
    String uname;
    private String psword;
    String privilege;
    

    public User(String uname, String psword, String type) {
        this.uname = uname;
        this.psword = psword;
        this.privilege = type;
    }

    public String getUname() {
        return uname;
    }

    public String getPsword() {
        return psword;
    }

    public String getType() {
        return privilege;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public void setPsword(String psword) {
        this.psword = psword;
    }

    public void setType(String type) {
        this.privilege = type;
    }


}

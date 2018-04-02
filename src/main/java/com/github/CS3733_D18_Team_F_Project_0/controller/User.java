package com.github.CS3733_D18_Team_F_Project_0.controller;

public class User {
    String uname;
    String psword;
    String type;

    public User(String uname, String psword, String type) {
        this.uname = uname;
        this.psword = psword;
        this.type = type;
    }

    public String getUname() {
        return uname;
    }

    public String getPsword() {
        return psword;
    }

    public String getType() {
        return type;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public void setPsword(String psword) {
        this.psword = psword;
    }

    public void setType(String type) {
        this.type = type;
    }
}

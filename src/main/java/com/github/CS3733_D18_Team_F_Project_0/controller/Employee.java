package com.github.CS3733_D18_Team_F_Project_0.controller;

public class Employee extends User {

    String firstName;
    String lastName;
    String occupation;

    public Employee(String uname, String psword, String type, String firstName, String lastName, String occupation) {
        super(uname, psword, type);
        this.firstName = firstName;
        this.lastName = lastName;
        this.occupation = occupation;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getOccupation() {
        return occupation;
    }


}

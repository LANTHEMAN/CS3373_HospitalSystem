package edu.wpi.cs3733d18.teamF.controller;


import org.opencv.face.Face;

public class User {
    String uname;
    String firstName;
    String lastName;
    String privilege;
    String occupation;
    private String psword;
    private String FaceID;


    public User(String uname, String psword, String firstName, String lastName, String privilege, String occupation, String FaceID) {
        this.uname = uname;
        // do it for the encryption
        setPsword(psword);
        this.firstName = firstName;
        this.lastName = lastName;
        this.privilege = privilege;
        this.occupation = occupation;
        this.FaceID = FaceID;
    }

    public User(String uname, String psword, String firstName, String lastName, String privilege, String occupation, String FaceID, boolean fromDatabase){
        this.uname = uname;
        // do it for the encryption
        if(fromDatabase){ setAlreadyEncryptedPassword(psword);}
        this.firstName = firstName;
        this.lastName = lastName;
        this.privilege = privilege;
        this.occupation = occupation;
        this.FaceID = FaceID;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPsword() {
        return psword;
    }

    public void setPsword(String psword) {
        this.psword = Encryption.encryptSHA256(psword);
    }

    public void setAlreadyEncryptedPassword(String password){
        this.psword = password;
    }

    public String getType() {
        return privilege;
    }

    public void setType(String type) {
        this.privilege = type;
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

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setFaceID(String faceID){
        this.FaceID = faceID;
    }

    public String getFaceID(){
        return this.FaceID;
    }

}

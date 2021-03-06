package edu.wpi.cs3733d18.teamF.sr;

import java.sql.Timestamp;

public class LanguageInterpreter extends ServiceRequests {
    private String language;

    public LanguageInterpreter(String firstName, String lastName, String location, String description, String status, int priority, String language, String staffNeeded) {
        super("Language Interpreter", firstName, lastName, location, description, status, priority, staffNeeded);
        this.language = language;
    }

    public LanguageInterpreter(int id, String firstName, String lastName, String location, String description, String status, int priority, String language, String staffNeeded) {
        super("Language Interpreter", id, firstName, lastName, location, description, status, priority, staffNeeded);
        this.language = language;
    }

    public LanguageInterpreter(int id, String firstName, String lastName, String location, String description, String status, int priority, String language, String completedBy, Timestamp createdOn, Timestamp started, Timestamp completedOn, String staffNeeded) {
        super("Language Interpreter", id, firstName, lastName, location, description, status, priority, completedBy, createdOn, started, completedOn, staffNeeded);
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void parseIntoDescription(){
        super.setDescription(getLanguage() + "\n" + getDescription());
    }
}

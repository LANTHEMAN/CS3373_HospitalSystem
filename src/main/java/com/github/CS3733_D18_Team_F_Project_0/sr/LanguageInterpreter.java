package com.github.CS3733_D18_Team_F_Project_0.sr;

import com.github.CS3733_D18_Team_F_Project_0.graph.Node;

public class LanguageInterpreter extends ServiceRequest {
    private String language;

    public LanguageInterpreter(String firstName, String lastName, String location, String description, int priority, String language) {
        super("Language Interpreter", firstName, lastName, location, description, priority);
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

    @Override
    public void parseIntoDescription(String s) {

    }
}

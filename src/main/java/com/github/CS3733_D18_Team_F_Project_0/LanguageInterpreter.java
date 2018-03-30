package com.github.CS3733_D18_Team_F_Project_0;

import com.github.CS3733_D18_Team_F_Project_0.graph.Node;

public class LanguageInterpreter extends ServiceRequest {
    private String language;

    public LanguageInterpreter(String type, Node destination, String description, String status, String language) {
        super(destination, description);
        this.setType("Language Interpreter");
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}

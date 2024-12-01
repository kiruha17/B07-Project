package com.example.b07group57.models;

public class HabitStructure {
    private String description;
    private String type;

    private String impact;

    public HabitStructure(String description, String type, String impact){

        this.description = description;
        this.type = type;
        this.impact = impact;

    }

    public String getDescription(){
        return description;
    }

    public String getType(){
        return  type;
    }

    public String getImpact(){
        return impact;
    }
}

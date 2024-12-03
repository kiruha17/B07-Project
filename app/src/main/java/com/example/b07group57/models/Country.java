package com.example.b07group57.models;

public class Country {
    private String name;
    private String value;

    public Country(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name;
    }
}

package com.example.b07group57.models;

public class ACFResult {
    public double total;
    public double transportation;
    public double food;
    public double housing;
    public double consumption;

    public ACFResult(double transportation, double food, double housing, double consumption) {
        this.transportation = transportation;
        this.food = food;
        this.housing = housing;
        this.consumption = consumption;
        this.total = transportation + food + housing + consumption;
    }
}
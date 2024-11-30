package com.example.b07group57.models;

public class OffsetProject {
    private String name;
    private String description;
    private String location;
    private double costPerTon;
    private double impactMetrics;
    private String websiteUrl;

    public OffsetProject(String name, String description, String location,
                         double costPerTon, double impactMetrics, String websiteUrl) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.costPerTon = costPerTon;
        this.impactMetrics = impactMetrics;
        this.websiteUrl = websiteUrl;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public double getCostPerTon() { return costPerTon; }
    public double getImpactMetrics() { return impactMetrics; }
    public String getWebsiteUrl() { return websiteUrl; }
}

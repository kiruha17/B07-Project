package com.example.b07group57.models;

import java.util.ArrayList;
import java.util.List;

public class OffSetProjectsList {
    public List<OffsetProject> fetchProjects() {
        List<OffsetProject> projects = new ArrayList<>();
        projects.add(new OffsetProject(
                "Reforestation in Kenya",
                "Planting 10,000 trees to combat deforestation.",
                "Kenya",
                25.0,
                2,
                "https://example.com/reforestation-kenya"
        ));
        projects.add(new OffsetProject(
                "Renewable Energy in India",
                "Solar farms providing sustainable electricity.",
                "India",
                30.0,
                1,
                "https://example.com/renewable-energy-india"
        ));

        //Can add as many projects as needed
        return projects;
    }
}

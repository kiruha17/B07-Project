package com.example.b07group57.models;

import java.util.ArrayList;
import java.util.List;

public class OffSetProjectsList {
    public List<OffsetProject> fetchProjects() {
        List<OffsetProject> projects = new ArrayList<>();
        projects.add(new OffsetProject(
                "Amazon Valparaíso Preservation",
                "Protects 28,000 hectares of tropical rainforest and supports sustainable agriculture in an area with some of the most biodiversity in the world. Save 20 trees per 1 ton of CO2e.",
                "Brazil",
                20,
                "https://standfortrees.org/protect-a-forest/valparaiso/"
        ));
        projects.add(new OffsetProject(
                "Brazilian Rosewood Preservation",
                "Protects threatened tree species and animals, while providing jobs in forest management and education in forest preservation. Save 20 trees per 1 ton of CO2e.",
                "Brazil",
                20,
                "https://standfortrees.org/protect-a-forest/brazilian-rosewood/"
        ));
        projects.add(new OffsetProject(
                "Brightening South Asia's Future with Solar Power",
                "Generates renewable clean energy from the sun which contributes to sustainable development and well being in its local area to combat the 916M ton oil equivelent in energy consumption.",
                "India",
                12.5,
                "https://shop.climeco.com/product/brightening-south-asias-future-with-solar-power/"
        ));
        projects.add(new OffsetProject(
                "Clean Energy for Steel Production",
                "Utilizes surplus waste gases to generate electricity and steam, in order to mitigate climate change.",
                "South Korea",
                12.5,
                "https://shop.climeco.com/product/east-asia-utilizes-surplus-waste-gases-to-generate-clean-energy-for-steel-production/"
        ));
        projects.add(new OffsetProject(
                "Educating The Next Energy Generation Workforce",
                "Builds wind turbine farms providing clean energy and building the next generation in the energy workforce.",
                "USA- South Dakota",
                12.5,
                "https://shop.climeco.com/product/educating-the-next-energy-generation-workforce/"
        ));
        projects.add(new OffsetProject(
                "Powering North Carolina Through Waste",
                "This project collects and combusts landfill gas to generate renewable energy, which is estimated to reduce 50,000 tonnes of CO2e annually.",
                "USA- North Carolina",
                12.5,
                "https://shop.climeco.com/product/powering-north-carolina-through-waste/"
        ));
        projects.add(new OffsetProject(
                "Virginia Beach Forest Preservation",
                "Preserves forestry from residential development, and mitigates ecological risks associated with climate change. Save 20 trees per 1 ton of CO2e.",
                "USA-Virginia",
                30,
                "https://standfortrees.org/protect-a-forest/virginia-beach-forest/"
        ));
        // Can add more projects when needed.
        return projects;
    }
}

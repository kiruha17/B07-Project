package com.example.b07group57;

import static org.junit.Assert.assertEquals;
import com.example.b07group57.models.SurveyData;
import com.example.b07group57.utils.HousingCalculator;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HousingCalculatorTest {

    @Test
    public void testHousingCalculator1() throws IOException {

        InputStream testFile = Files.newInputStream(Paths.get("src/main/assets/HousingData.xlsx"));

        SurveyData surveyData = new SurveyData("", "",
                "", "", "", "",
                "", "", "",
                "", "$100-$150",
                "", "", "Natural Gas",
                "2", "1000–2000 sq. ft.",
                "Townhouse", "", "", "", "",
                "No", "", "", "Natural Gas");

        assertEquals(5500.0, HousingCalculator.calculateHousing(testFile, surveyData), 0);
    }

    @Test
    public void testHousingCalculator2() throws IOException {

        InputStream testFile = Files.newInputStream(Paths.get("src/main/assets/HousingData.xlsx"));

        SurveyData surveyData = new SurveyData("", "",
                "", "", "", "",
                "", "", "",
                "", "Over $200",
                "", "", "Natural Gas",
                "3 to 4", "Over 2000 sq. ft.",
                "Other", "", "", "", "",
                "Yes, partially (less than 50% of energy use)", "",
                "", "Propane");

        assertEquals(6533.0, HousingCalculator.calculateHousing(testFile, surveyData), 0);
    }
}
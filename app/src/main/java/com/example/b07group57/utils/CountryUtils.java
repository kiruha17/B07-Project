package com.example.b07group57.utils;

import android.content.Context;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import com.example.b07group57.models.Country;
public class CountryUtils {
    public static List<Country> parseCSV(Context context, int resourceId) {
        List<Country> countryList = new ArrayList<>();
        try {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(",");
                if (split.length == 2) {
                    countryList.add(new Country(split[0].trim(), split[1].trim()));
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countryList;
    }
}

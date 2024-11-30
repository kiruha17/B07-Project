package com.example.b07group57.utils;

import android.content.Context;
import android.util.Log;

import com.example.b07group57.models.SurveyData;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;

public class HousingCalculator {

    public static double calculateHousing(Context context, SurveyData data) {
        double total = 0;

        // Add constant CO₂ impact for differing water and home heaters
        if (!data.waterHeater.equals(data.homeHeater)) {
            total += 233;
        }

        // Deduct based on renewable energy usage
        switch (data.renewableEnergy) {
            case "Yes, primarily (more than 50% of energy use)":
                total -= 6000;
                break;
            case "Yes, partially (less than 50% of energy use)":
                total -= 4000;
                break;
            case "No":
                break;
            default:
                Log.e("calculateHousing", "Invalid renewableEnergy value");
                return Double.NEGATIVE_INFINITY;
        }

        // Add Excel-based dynamic calculation
        double housingCO2 = getHousingCO2(context, data);
        if (housingCO2 != Double.NEGATIVE_INFINITY) {
            total += housingCO2;
        } else {
            Log.e("calculateHousing", "Error calculating housing CO₂");
            return Double.NEGATIVE_INFINITY;
        }

        return total;
    }

    private static double getHousingCO2(Context context, SurveyData data) {
        double result = Double.NEGATIVE_INFINITY;

        try {
            InputStream inputStream = context.getAssets().open("HousingData.xlsx");
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet("Sheet1");

            if (sheet == null) {
                Log.e("getHousingCO2", "Sheet not found: Sheet1");
                return result;
            }

            // Step 1: Locate the row for the housing type
            int housingRow = locateRow(sheet, data.homeType);
            if (housingRow == -1) {
                Log.e("getHousingCO2", "Housing type row not found");
                return result;
            }

            // Step 2: Locate the column for the size, directly right of the housing type
            Row housingTypeRow = sheet.getRow(housingRow);
            int sizeColumn = locateColumnInRow(housingTypeRow, data.homeSize);
            if (sizeColumn == -1) {
                Log.e("getHousingCO2", "Housing size column not found");
                return result;
            }

            // Step 3: Locate the row for the number of occupants below the size column
            int occupantsRow = locateOccupantsRow(sheet, data.homePeople, housingRow + 2, sizeColumn);
            if (occupantsRow == -1) {
                Log.e("getHousingCO2", "Occupants row not found");
                return result;
            }

            // Step 4: Locate the column for electricity bill within the block
            Row sizeHeaderRow = sheet.getRow(housingRow + 1);
            int billColumn = locateColumnInRow(sizeHeaderRow, data.electricityBill);
            if (billColumn == -1) {
                Log.e("getHousingCO2", "Electricity bill column not found");
                return result;
            }

            // Step 5: Retrieve the cell value
            Row occupantsDataRow = sheet.getRow(occupantsRow);
            if (occupantsDataRow != null) {
                Cell cell = occupantsDataRow.getCell(billColumn);
                if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                    result = cell.getNumericCellValue();
                } else {
                    Log.e("getHousingCO2", "Cell value is not numeric");
                }
            }

            workbook.close();
            inputStream.close();
        } catch (Exception e) {
            Log.e("getHousingCO2", "Error reading Excel file: " + e.getMessage());
        }

        return result;
    }

    private static int locateRow(Sheet sheet, String searchValue) {
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell cell = row.getCell(0); // Assuming housing type in column A
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    if (cell.getStringCellValue().equalsIgnoreCase(searchValue)) {
                        return i;
                    }
                }
            }
        }
        return -1; // Row not found
    }

    private static int locateColumnInRow(Row row, String searchValue) {
        if (row != null) {
            for (Cell cell : row) {
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    if (cell.getStringCellValue().equalsIgnoreCase(searchValue)) {
                        return cell.getColumnIndex();
                    }
                }
            }
        }
        return -1; // Column not found
    }

    private static int locateOccupantsRow(Sheet sheet, String searchValue, int startRow, int sizeColumn) {
        for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell cell = row.getCell(sizeColumn - 1); // Occupants are left of the size column
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    if (cell.getStringCellValue().equalsIgnoreCase(searchValue)) {
                        return i;
                    }
                }
            }
        }
        return -1; // Row not found
    }
}
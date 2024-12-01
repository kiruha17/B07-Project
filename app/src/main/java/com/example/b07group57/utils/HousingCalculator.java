package com.example.b07group57.utils;

import com.example.b07group57.models.SurveyData;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;

public class HousingCalculator {

    public static double calculateHousing(InputStream excelFile, SurveyData data) {
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
                System.out.println("Invalid renewableEnergy value");
                return Double.NEGATIVE_INFINITY;
        }

        // Add Excel-based dynamic calculation
        double housingCO2 = getHousingCO2(excelFile, data);
        if (housingCO2 != Double.NEGATIVE_INFINITY) {
            total += housingCO2;
        } else {
            System.out.println("Error calculating housing CO₂");
            return Double.NEGATIVE_INFINITY;
        }
        return total;
    }

    private static double getHousingCO2(InputStream excelFile, SurveyData data) {
        double result = Double.NEGATIVE_INFINITY;

        try {
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet sheet = workbook.getSheet("Sheet1");

            if (sheet == null) {
                System.out.println("Sheet not found: Sheet1");
                return result;
            }

            // Step 1: Locate housing type and size
            int housingTypeRow = -1;

            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell housingTypeCell = row.getCell(0); // Housing type in column A
                Cell sizeCell = row.getCell(1); // Size in column B

                if (housingTypeCell != null && sizeCell != null &&
                        housingTypeCell.getCellType() == CellType.STRING &&
                        sizeCell.getCellType() == CellType.STRING &&
                        housingTypeCell.getStringCellValue().equalsIgnoreCase(data.homeType) &&
                        sizeCell.getStringCellValue().equalsIgnoreCase(data.homeSize)) {
                    housingTypeRow = i;
                    break;
                }
            }

            if (housingTypeRow == -1) {
                System.out.println("Housing type and size not found");
                return result;
            }

            // Debug messages
            System.out.println(housingTypeRow);

            // Step 2: Locate the row for the number of occupants
            int occupantsRow = -1;

            for (int i = housingTypeRow + 3; i <= housingTypeRow + 7; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell occupantsCell = row.getCell(0); // Column A
                if (occupantsCell != null) {
                    String occupantsValue = null;
                    if (occupantsCell.getCellType() == CellType.STRING) {
                        occupantsValue = occupantsCell.getStringCellValue();
                    } else if (occupantsCell.getCellType() == CellType.NUMERIC) {
                        occupantsValue = String.valueOf((int) occupantsCell.getNumericCellValue());
                    }
                    if (occupantsValue != null && occupantsValue.equalsIgnoreCase(data.homePeople)) {
                        occupantsRow = i;
                        break;
                    }
                }
            }

            if (occupantsRow == -1) {
                System.out.println("Number of occupants not found");
                return result; // Return result if occupantsRow is not found
            }

            // Debug messages
            System.out.println(occupantsRow);

            // Step 3: Locate the column for the electricity bill range
            int billRangeColumn = -1;
            Row billRangeRow = sheet.getRow(housingTypeRow + 1); // Row where bill ranges start
            if (billRangeRow != null) {
                for (Cell cell : billRangeRow) {
                    if (cell.getCellType() == CellType.STRING &&
                            cell.getStringCellValue().equalsIgnoreCase(data.electricityBill)) {
                        billRangeColumn = cell.getColumnIndex();
                        break;
                    }
                }
            }

            if (billRangeColumn == -1) {
                System.out.println("Electricity bill range not found");
                return result;
            }

            // Debug messages
            System.out.println(billRangeColumn);

            // Step 4: Locate the heating energy type column within the bill range
            int heatingTypeColumn = -1;
            Row heatingTypeRow = sheet.getRow(housingTypeRow + 2); // Row below the bill range row
            if (heatingTypeRow != null) {
                for (int i = billRangeColumn; i < billRangeColumn + 5; i++) { // Check the next 5 cells for heating type
                    Cell cell = heatingTypeRow.getCell(i);
                    if (cell != null && cell.getCellType() == CellType.STRING &&
                            cell.getStringCellValue().equalsIgnoreCase(data.homeHeater)) {
                        heatingTypeColumn = i;
                        break;
                    }
                }
            }

            if (heatingTypeColumn == -1) {
                System.out.println("Heating energy type not found");
                return result;
            }

            // Debug messages
            System.out.println(heatingTypeColumn);

            // Step 5: Retrieve the CO2 value
            Row occupantsDataRow = sheet.getRow(occupantsRow);
            if (occupantsDataRow != null) {
                Cell cell = occupantsDataRow.getCell(heatingTypeColumn);
                if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                    result = cell.getNumericCellValue();
                } else {
                    System.out.println("CO2 value is not numeric or missing");
                }
            }

            workbook.close();
        } catch (Exception e) {
            System.out.println("Error reading Excel file: " + e.getMessage());
        }

        return result;
    }
}
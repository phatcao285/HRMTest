package com.phat.helpers;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelHelper {

    private FileInputStream fis;
    private FileOutputStream fileOut;
    private Workbook wb;
    private Sheet sh;
    private Cell cell;
    private Row row;
    private CellStyle cellstyle;
    private Color mycolor;
    private String excelFilePath;
    private Map<String, Integer> columns = new HashMap<>();

    public void setExcelFile(String ExcelPath, String SheetName){
        try {
            File f = new File(ExcelPath);

            if (!f.exists()) {
                System.out.println("File doesn't exist.");
            }

            fis = new FileInputStream(ExcelPath);
            wb = WorkbookFactory.create(fis);
            sh = wb.getSheet(SheetName);

            if (sh == null) {
                throw new Exception("Sheet name doesn't exist.");
            }

            this.excelFilePath = ExcelPath;

            //adding all the column header names to the map 'columns'
            sh.getRow(0).forEach(cell ->{
                columns.put(cell.getStringCellValue(), cell.getColumnIndex());
            });

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public String getCellData(int columnIndex, int rowIndex) {
        try {
            cell = sh.getRow(rowIndex).getCell(columnIndex);
            String CellData = null;
            switch (cell.getCellType()) {
                case STRING:
                    CellData = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        CellData = String.valueOf(cell.getDateCellValue());
                    } else {
                        CellData = String.valueOf((long) cell.getNumericCellValue());
                    }
                    break;
                case BOOLEAN:
                    CellData = Boolean.toString(cell.getBooleanCellValue());
                    break;
                case BLANK:
                    CellData = "";
                    break;
            }
            return CellData;
        } catch (Exception e) {
            return "";
        }
    }

    //Gọi ra hàm này nè
    public String getCellData(String columnName, int rowIndex) {
        return getCellData(columns.get(columnName), rowIndex);
    }
    // Đọc toàn bộ sheet, Trả về Object[][] với số dòng = số dòng dữ liệu trong sheet.
    public Object[][] getExcelData(String filePath, String sheetName) {
        Object[][] data = null;
        Workbook workbook = null;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);
            Sheet sh = workbook.getSheet(sheetName);

            Row row = sh.getRow(0);
            int noOfRows = sh.getPhysicalNumberOfRows();
            int noOfCols = row.getLastCellNum();

            System.out.println(noOfRows + " - " + noOfCols);

            Cell cell;

            // Trừ 1 cột (không đọc cột TestCaseID)
            data = new Object[noOfRows - 1][noOfCols - 1];

            for (int i = 1; i < noOfRows; i++) {
                row = sh.getRow(i);

                // j = 1 để bỏ qua cột đầu tiên (TestCaseID)
                for (int j = 1; j < noOfCols; j++) {
                    cell = row.getCell(j);

                    if (cell == null) {
                        data[i - 1][j - 1] = ""; // shift index vì bỏ cột 0
                        continue;
                    }

                    switch (cell.getCellType()) {
                        case STRING:
                            data[i - 1][j - 1] = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            data[i - 1][j - 1] = String.valueOf(cell.getNumericCellValue());
                            break;
                        case BLANK:
                            data[i - 1][j - 1] = "";
                            break;
                        default:
                            data[i - 1][j - 1] = cell.toString();
                            break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("The exception is:" + e.getMessage());
            throw new RuntimeException(e);
        }
        return data;
    }
    // get data by testCaseId
    public Object[][] getExcelData(String filePath, String sheetName, String testCaseId) {
        Object[][] data = null;
        DataFormatter formatter = new DataFormatter();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Row headerRow = sheet.getRow(0);
            int noOfCols = headerRow.getLastCellNum();
            int noOfRows = sheet.getPhysicalNumberOfRows();

            // Đếm số dòng trùng testCaseId
            int rowCount = 0;
            for (int i = 1; i < noOfRows; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                String currentId = formatter.formatCellValue(row.getCell(0));
                if (testCaseId.equalsIgnoreCase(currentId)) {
                    rowCount++;
                }
            }

            data = new Object[rowCount][noOfCols - 1]; // bỏ cột TestCaseID

            // get data
            int index = 0;
            for (int i = 1; i < noOfRows; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String currentId = formatter.formatCellValue(row.getCell(0));
                if (!testCaseId.equalsIgnoreCase(currentId)) continue;

                for (int j = 1; j < noOfCols; j++) { // bỏ cột TestCaseID
                    Cell cell = row.getCell(j);

                    if (cell == null) {
                        data[index][j - 1] = "";
                        continue;
                    }

                    switch (cell.getCellType()) {
                        case STRING:
                            data[index][j - 1] = cell.getStringCellValue();
                            break;

                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                // Change date format to dd/MM/yyyy
                                Date date = cell.getDateCellValue();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                data[index][j - 1] = sdf.format(date);
                            } else {
                                double val = cell.getNumericCellValue();
                                //If value is integer, cast to int to remove decimal point
                                if (val == (int) val) {
                                    data[index][j - 1] = (int) val;
                                } else {
                                    data[index][j - 1] = val;
                                }
                            }
                            break;

                        case BOOLEAN:
                            data[index][j - 1] = cell.getBooleanCellValue();
                            break;

                        case FORMULA:
                            data[index][j - 1] = formatter.formatCellValue(cell);
                            break;

                        case BLANK:
                            data[index][j - 1] = "";
                            break;

                        default:
                            data[index][j - 1] = formatter.formatCellValue(cell);
                            break;
                    }
                }
                index++;
            }

        } catch (Exception e) {
            throw new RuntimeException("Error reading Excel file: " + e.getMessage(), e);
        }

        return data;
    }
}
package com.phat.dataprovider;

import com.phat.helpers.ExcelHelper;
import org.testng.annotations.DataProvider;

public class DataProviderFactory {
    String data_path = "src/test/resources/testdata/HRM.xlsx";

    @DataProvider(name = "data_LoginSuccess_registered_account")
    public Object[][] dataLoginSuccess() {
        ExcelHelper excelHelper = new ExcelHelper();
        Object[][] logindata = excelHelper.getExcelData(data_path, "Client", "TC01");
        Object[][] result = new Object[logindata.length][2];
        for (int i = 0; i < logindata.length; i++) {
            result[i][0] = logindata[i][6]; //username
            result[i][1] = logindata[i][2]; //password
        }
        return result;
    }

    @DataProvider(name = "data_LoginFail_without_username")
    public Object[][] dataLoginFail1() {
        ExcelHelper excelHelper = new ExcelHelper();
        Object[][] logindata = excelHelper.getExcelData(data_path, "Login", "TC03");
        return logindata;
    }

    @DataProvider(name = "data_LoginFail_without_password")
    public Object[][] dataLoginFail2() {
        ExcelHelper excelHelper = new ExcelHelper();
        Object[][] logindata = excelHelper.getExcelData(data_path, "Login", "TC02");
        return logindata;
    }
    @DataProvider(name = "data_AddNewClient")
    public Object[][] dataAddNewClient() {
        ExcelHelper excelHelper = new ExcelHelper();
        Object[][] clientdata = excelHelper.getExcelData(data_path, "Client", "TC01");
        Object[][] result = new Object[clientdata.length][8];
        for (int i = 0; i < clientdata.length; i++) {
            result[i][0] = clientdata[i][0]; //firstName
            result[i][1] = clientdata[i][1]; //lastName
            result[i][2] = clientdata[i][2]; //password
            result[i][3] = clientdata[i][3]; //contactNumber
            result[i][4] = clientdata[i][4]; //gender
            result[i][5] = clientdata[i][5]; //email
            result[i][6] = clientdata[i][6]; //username
            result[i][7] = clientdata[i][7]; //filepath
        }
        return result;
    }
    @DataProvider(name = "data_editClient")
    public Object[][] dataEditClient() {
        ExcelHelper excelHelper = new ExcelHelper();
        Object[][] clientdata = excelHelper.getExcelData(data_path, "Client", "TC02");
        return clientdata;
    }
    @DataProvider(name = "data_DeleteClient")
    public Object[][] dataDeleteClient() {
        ExcelHelper excelHelper = new ExcelHelper();
        Object[][] clientdata = excelHelper.getExcelData(data_path, "Client", "TC05");
        Object[][] result = new Object[clientdata.length][8];
        for (int i = 0; i < clientdata.length; i++) {
            result[i][0] = clientdata[i][0]; //firstName
            result[i][1] = clientdata[i][1]; //lastName
            result[i][2] = clientdata[i][2]; //password
            result[i][3] = clientdata[i][3]; //contactNumber
            result[i][4] = clientdata[i][4]; //gender
            result[i][5] = clientdata[i][5]; //email
            result[i][6] = clientdata[i][6]; //username
            result[i][7] = clientdata[i][7]; //filepath
        }
        return result;
    }
    @DataProvider(name = "data_AddNewProject")
    public Object[][] dataAddNewProject() {
        ExcelHelper excelHelper = new ExcelHelper();
        Object[][] projectdata = excelHelper.getExcelData(data_path, "Project", "TC01");
        Object[][] result = new Object[projectdata.length][5];
        for (int i = 0; i < projectdata.length; i++) {
            result[i][0] = projectdata[i][0]; //title
            result[i][1] = projectdata[i][1]; //client
            result[i][2] = projectdata[i][2]; //startDate
            result[i][3] = projectdata[i][3]; //endDate
            result[i][4] = projectdata[i][4]; //summary
        }
        return result;
    }
    @DataProvider(name = "data_editProject")
    public Object[][] dataEditProject() {
        ExcelHelper excelHelper = new ExcelHelper();
        Object[][] projectdata = excelHelper.getExcelData(data_path, "Project", "TC02");
        Object[][] result = new Object[projectdata.length][6];
        for (int i = 0; i < projectdata.length; i++) {
            result[i][0] = projectdata[i][0]; //title
            result[i][1] = projectdata[i][1]; //client
            result[i][2] = projectdata[i][2]; //startDate
            result[i][3] = projectdata[i][3]; //endDate
            result[i][4] = projectdata[i][4]; //summary
            result[i][5] = projectdata[i][5]; //update_endDate
        }
        return result;
    }
    @DataProvider(name = "data_deleteProject")
    public Object[][] dataDeleteProject() {
        ExcelHelper excelHelper = new ExcelHelper();
        Object[][] projectdata = excelHelper.getExcelData(data_path, "Project", "TC03");
        Object[][] result = new Object[projectdata.length][5];
        for (int i = 0; i < projectdata.length; i++) {
            result[i][0] = projectdata[i][0]; //title
            result[i][1] = projectdata[i][1]; //client
            result[i][2] = projectdata[i][2]; //startDate
            result[i][3] = projectdata[i][3]; //endDate
            result[i][4] = projectdata[i][4]; //summary
        }
        return result;
    }
    @DataProvider(name = "data_editStatusProject")
    public Object[][] dataEditStatusProject() {
        ExcelHelper excelHelper = new ExcelHelper();
        Object[][] projectdata = excelHelper.getExcelData(data_path, "Project", "TC04");
        Object[][] result = new Object[projectdata.length][8];
        for (int i = 0; i < projectdata.length; i++) {
            result[i][0] = projectdata[i][0]; //title
            result[i][1] = projectdata[i][1]; //client
            result[i][2] = projectdata[i][2]; //startDate
            result[i][3] = projectdata[i][3]; //endDate
            result[i][4] = projectdata[i][4]; //summary
            result[i][5] = projectdata[i][6]; //status
            result[i][6] = projectdata[i][7]; //priority
            result[i][7] = projectdata[i][8]; //progress
        }
        return result;
    }
    @DataProvider(name = "data_addAttachFileProject")
    public Object[][] dataAddAttachFileProject() {
        ExcelHelper excelHelper = new ExcelHelper();
        Object[][] projectdata = excelHelper.getExcelData(data_path, "Project", "TC05");
        Object[][] result = new Object[projectdata.length][7];
        for (int i = 0; i < projectdata.length; i++) {
            result[i][0] = projectdata[i][0]; //title
            result[i][1] = projectdata[i][1]; //client
            result[i][2] = projectdata[i][2]; //startDate
            result[i][3] = projectdata[i][3]; //endDate
            result[i][4] = projectdata[i][4]; //summary
            result[i][5] = projectdata[i][9]; //file name
            result[i][6] = projectdata[i][10]; //file path
        }
        return result;
    }
    @DataProvider(name = "data_addNewTask")
    public Object[][] dataAddNewTask() {
        ExcelHelper excelHelper = new ExcelHelper();
        Object[][] taskdata = excelHelper.getExcelData(data_path, "Task", "TC01");
        Object[][] result = new Object[taskdata.length][5];
        for (int i = 0; i < taskdata.length; i++) {
            result[i][0] = taskdata[i][0]; //title task
            result[i][1] = taskdata[i][1]; //startDate task
            result[i][2] = taskdata[i][2]; //endDate task
            result[i][3] = taskdata[i][3]; //project
            result[i][4] = taskdata[i][4]; //summary
        }
        return result;
    }
    @DataProvider(name = "data_deleteTask")
    public Object[][] dataDeleteTask() {
        ExcelHelper excelHelper = new ExcelHelper();
        Object[][] taskdata = excelHelper.getExcelData(data_path, "Task", "TC02");
        Object[][] result = new Object[taskdata.length][5];
        for (int i = 0; i < taskdata.length; i++) {
            result[i][0] = taskdata[i][0]; //title task
            result[i][1] = taskdata[i][1]; //startDate task
            result[i][2] = taskdata[i][2]; //endDate task
            result[i][3] = taskdata[i][3]; //project
            result[i][4] = taskdata[i][4]; //summary
        }
        return result;
    }
}
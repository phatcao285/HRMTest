package com.phat.helpers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateHelper {
    public static LocalDate parseDate(String date){
        DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(date,dateformat);
    }
    public static int convertMonthToNumber(String monthText){
        switch (monthText){ //Convert từ tháng số -> tháng chữ và chỉ lấy 3 chữ đầu
            case "JAN": return 1;
            case "FEB": return 2;
            case "MAR": return 3;
            case "APR": return 4;
            case "MAY": return 5;
            case "JUN": return 6;
            case "JUL": return 7;
            case "AUG": return 8;
            case "SEP": return 9;
            case "OCT": return 10;
            case "NOV": return 11;
            case "DEC": return 12;
            default: return -1;
        }
    }
}
package com.example.printifybackend.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final DateTimeFormatter jsDateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public static LocalDateTime jsDateToLocalDateTime(String jsDate) {
        try {
            return LocalDateTime.parse(jsDate, jsDateTimeFormatter);
        } catch (Exception e) {
            return null;
        }
    }

}

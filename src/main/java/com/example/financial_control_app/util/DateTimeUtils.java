package com.example.financial_control_app.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public abstract class DateTimeUtils {
    public static LocalDateTime parseToLocalDateTime(String dateStr, boolean endOfDay) {
        try {
            return LocalDateTime.parse(dateStr);
        } catch (DateTimeParseException e) {
            try {
                return endOfDay ?
                        LocalDate.parse(dateStr).atTime(23, 59, 59) :
                        LocalDate.parse(dateStr).atStartOfDay();
            } catch (java.time.format.DateTimeParseException ex) {
                throw e;
            }
        }
    }
}

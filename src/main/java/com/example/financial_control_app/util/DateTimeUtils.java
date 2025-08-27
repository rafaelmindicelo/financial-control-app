package com.example.financial_control_app.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public abstract class DateTimeUtils {
    public static LocalDateTime parseToLocalDateTime(String dateStr, boolean endDate) {
        try {
            return LocalDateTime.parse(dateStr);
        } catch (DateTimeParseException e) {
            try {
                return endDate ?
                        LocalDate.parse(dateStr).atTime(LocalTime.now()) :
                        LocalDate.parse(dateStr).atStartOfDay();
            } catch (DateTimeParseException ex) {
                throw e;
            }
        }
    }
}

package com.example.financial_control_app.dto.expense;

public record ExpenseFilterDTO(String startDate,
                               String endDate,
                               Integer categoryId) {
}

package com.example.financial_control_app.dto.expense;

import java.time.LocalDateTime;

public record ExpenseFilterParams(LocalDateTime startDate, LocalDateTime endDate, Integer categoryId) {
}

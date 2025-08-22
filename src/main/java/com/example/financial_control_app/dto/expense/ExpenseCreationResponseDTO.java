package com.example.financial_control_app.dto.expense;

import lombok.Getter;

@Getter
public class ExpenseCreationResponseDTO {
    private final String code = "EXPENSE_CREATED";
    private final String message = "Expense created successfully";
}

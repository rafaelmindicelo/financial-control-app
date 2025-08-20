package com.example.financial_control_app.expense.dtos;

import lombok.Getter;

@Getter
public class ExpenseCreationResponse {
    private final String code = "EXPENSE_CREATED";
    private final String message = "Expense created successfully";
}

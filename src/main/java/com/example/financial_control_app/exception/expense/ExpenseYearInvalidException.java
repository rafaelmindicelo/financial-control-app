package com.example.financial_control_app.exception.expense;

public class ExpenseYearInvalidException extends RuntimeException {
    public ExpenseYearInvalidException(String message) {
        super(message);
    }
}

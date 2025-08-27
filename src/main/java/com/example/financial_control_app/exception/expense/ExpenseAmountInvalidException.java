package com.example.financial_control_app.exception.expense;

public class ExpenseAmountInvalidException extends RuntimeException {
    public ExpenseAmountInvalidException(String message) {
        super(message);
    }
}

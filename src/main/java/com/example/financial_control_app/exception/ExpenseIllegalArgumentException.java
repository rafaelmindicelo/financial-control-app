package com.example.financial_control_app.exception;

public class ExpenseIllegalArgumentException extends RuntimeException {
    public ExpenseIllegalArgumentException(String message) {
        super(message);
    }
}

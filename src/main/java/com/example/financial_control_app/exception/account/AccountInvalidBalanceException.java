package com.example.financial_control_app.exception.account;

public class AccountInvalidBalanceException extends RuntimeException {
    public AccountInvalidBalanceException(String message) {
        super(message);
    }
}

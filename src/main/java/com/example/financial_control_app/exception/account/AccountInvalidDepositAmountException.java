package com.example.financial_control_app.exception.account;

public class AccountInvalidDepositAmountException extends RuntimeException {
    public AccountInvalidDepositAmountException(String message) {
        super(message);
    }
}

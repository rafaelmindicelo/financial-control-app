package com.example.financial_control_app.exception.account;

public class AccountDescriptionAlreadyExistsException extends RuntimeException {
    public AccountDescriptionAlreadyExistsException(String message) {
        super(message);
    }
}

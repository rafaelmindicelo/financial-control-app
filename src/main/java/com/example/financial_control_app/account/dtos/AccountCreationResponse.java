package com.example.financial_control_app.account.dtos;

import lombok.Getter;

@Getter
public class AccountCreationResponse {
    private final String code = "ACCOUNT_CREATED";
    private final String message = "Account created successfully";
}

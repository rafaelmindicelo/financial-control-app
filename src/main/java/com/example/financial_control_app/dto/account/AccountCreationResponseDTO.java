package com.example.financial_control_app.dto.account;

import lombok.Getter;

@Getter
public class AccountCreationResponseDTO {
    private final String code = "ACCOUNT_CREATED";
    private final String message = "Account created successfully";
}

package com.example.financial_control_app.account.dtos;

import lombok.Getter;

@Getter
public class AccountDepositResponse {
    private final String code = "DEPOSIT_SUCCESS";
    private final String message = "Deposit completed successfully";
}

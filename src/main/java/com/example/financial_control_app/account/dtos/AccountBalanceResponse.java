package com.example.financial_control_app.account.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
public class AccountBalanceResponse {
    private final String code = "BALANCE_RETRIEVED";
    private final String message = "Account balance retrieved successfully";

    @Setter
    private double balance;

    public AccountBalanceResponse(double balance) {
        this.balance = balance;
    }

}

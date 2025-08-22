package com.example.financial_control_app.dto.account;

import lombok.Getter;
import lombok.Setter;

@Getter
public class AccountBalanceResponseDTO {
    private final String code = "BALANCE_RETRIEVED";
    private final String message = "Account balance retrieved successfully";

    @Setter
    private double balance;

    public AccountBalanceResponseDTO(double balance) {
        this.balance = balance;
    }

}

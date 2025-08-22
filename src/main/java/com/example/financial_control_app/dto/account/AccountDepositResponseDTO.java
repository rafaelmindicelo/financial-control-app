package com.example.financial_control_app.dto.account;

import lombok.Getter;

@Getter
public class AccountDepositResponseDTO {
    private final String code = "DEPOSIT_SUCCESS";
    private final String message = "Deposit completed successfully";
}

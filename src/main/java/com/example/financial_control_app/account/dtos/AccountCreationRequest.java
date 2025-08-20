package com.example.financial_control_app.account.dtos;

import lombok.Getter;

@Getter
public class AccountCreationRequest {
    private String owner;
    private double balance;
}

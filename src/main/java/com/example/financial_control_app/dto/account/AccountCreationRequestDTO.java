package com.example.financial_control_app.dto.account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountCreationRequestDTO {
    private String description;
    private double balance;
    private Long userId;
}

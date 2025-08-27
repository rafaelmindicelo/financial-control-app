package com.example.financial_control_app.dto.user;

import lombok.Getter;

@Getter
public class UserCreationResponseDTO {
    private final String code = "USER_CREATED";
    private final String message = "User created successfully";
}

package com.example.financial_control_app.dto.expense;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExpenseCreationRequestDTO {
    private String description;
    private double amount;
    private LocalDateTime date;
    private Integer categoryId;
    private Long accountId;
}

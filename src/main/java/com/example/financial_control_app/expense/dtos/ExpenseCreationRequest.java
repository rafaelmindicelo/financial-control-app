package com.example.financial_control_app.expense.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class ExpenseCreationRequest {
    private String description;
    private double amount;
    private LocalDateTime date;
    private Integer categoryId;
    //private Category category;
    private Long accountId;
}

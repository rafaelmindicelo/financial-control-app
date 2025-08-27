package com.example.financial_control_app.expense;

import com.example.financial_control_app.account.AccountModel;
import com.example.financial_control_app.category.CategoryModel;
import com.example.financial_control_app.exception.account.AccountIdNullException;
import com.example.financial_control_app.exception.expense.ExpenseAmountInvalidException;
import com.example.financial_control_app.exception.expense.ExpenseDescriptionInvalidException;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "tbl_expense")
public class ExpenseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String description;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryModel category;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    @JsonBackReference
    private AccountModel account;

    public ExpenseModel(String description, double amount, LocalDateTime date, CategoryModel category, AccountModel account) {
        if (description == null || description.isEmpty() || description.length() > 100) {
            throw new ExpenseDescriptionInvalidException("Expense description cannot be null or empty and must be less than 100 characters");
        }

        if (amount <= 0) {
            throw new ExpenseAmountInvalidException("Expense amount must be greater than zero");
        }

        if (account != null && account.getId() == null) {
            throw new AccountIdNullException("Account ID cannot be null");
        }

        this.description = description;
        this.amount = amount;
        this.date = (date == null) ? LocalDateTime.now() : date;
        this.category = category;
        this.account = account;
    }
}

package com.example.financial_control_app.account;

import com.example.financial_control_app.exception.account.AccountIllegalArgumentException;
import com.example.financial_control_app.expense.ExpenseModel;
import com.example.financial_control_app.user.UserModel;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Getter
@Entity
@Table(name = "tbl_account")
public class AccountModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String description;

    @Column(nullable = false)
    private double balance;

    @OneToMany(mappedBy = "account")
    @JsonManagedReference
    private List<ExpenseModel> expenses;

    @OneToOne(mappedBy = "account")
    //@JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    protected AccountModel() {
    }

    public AccountModel(String description, double balance) {
        boolean isDescriptionLengthExceeded = description != null && description.length() > 50;
        if (description == null || description.isBlank() || isDescriptionLengthExceeded) {
            throw new AccountIllegalArgumentException("Account description cannot be null or empty or exceed 50 characters");
        }

        if (balance < 0) {
            throw new AccountIllegalArgumentException("Initial balance cannot be negative");
        }

        this.description = description;
        this.balance = balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new AccountIllegalArgumentException("Deposit amount must be greater than zero");
        }
        this.balance += amount;
    }
}

package com.example.financial_control_app.account;

import com.example.financial_control_app.exception.AccountIllegalArgumentException;
import com.example.financial_control_app.expense.ExpenseModel;
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
    private String owner;

    @Column(nullable = false)
    private double balance;

    @OneToMany(mappedBy = "account")
    @JsonManagedReference
    private List<ExpenseModel> expenses;

    protected AccountModel() {
    }

    public AccountModel(String owner, double balance) {
        boolean isOwnerLengthExceeded = owner != null && owner.length() > 50;
        if (owner == null || owner.isBlank() || isOwnerLengthExceeded) {
            throw new AccountIllegalArgumentException("Account owner cannot be null or empty or exceed 50 characters");
        }

        if (balance < 0) {
            throw new AccountIllegalArgumentException("Initial balance cannot be negative");
        }

        this.owner = owner;
        this.balance = balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new AccountIllegalArgumentException("Deposit amount must be greater than zero");
        }
        this.balance += amount;
    }
}

package com.example.financial_control_app.account;

import com.example.financial_control_app.expense.ExpenseModel;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
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
}

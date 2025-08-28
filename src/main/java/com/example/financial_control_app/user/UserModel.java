package com.example.financial_control_app.user;

import com.example.financial_control_app.account.AccountModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_user")
@NoArgsConstructor
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String username;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String passwordHash;

    @OneToOne(optional = true)
    @JoinColumn(name = "account_id", unique = true)
    private AccountModel account;

    public UserModel(String username, String email, String passwordHash) {
        if (username == null || username.isBlank() || username.length() > 100) {
            throw new IllegalArgumentException("Username cannot be null, empty, or exceed 100 characters");
        }
        if (email == null || email.isBlank() || email.length() > 100) {
            throw new IllegalArgumentException("Email cannot be null, empty, or exceed 100 characters");
        }
        if (passwordHash == null || passwordHash.isBlank() || passwordHash.length() > 100) {
            throw new IllegalArgumentException("Password hash cannot be null, empty, or exceed 100 characters");
        }
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }
}

package com.example.financial_control_app.account;

import com.example.financial_control_app.dto.account.*;
import com.example.financial_control_app.exception.AccountIllegalArgumentException;
import com.example.financial_control_app.exception.AccountNotFoundException;
import com.example.financial_control_app.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AccountCreationRequestDTO accountToCreate) {
        try {
            accountService.create(accountToCreate);
            return ResponseEntity.status(HttpStatus.CREATED).body(new AccountCreationResponseDTO());
        } catch (AccountIllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(ex.getMessage()));
        }
    }

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<?> deposit(@PathVariable Long accountId, @RequestParam DepositRequestDTO depositRequest) {
        try {
            accountService.deposit(accountId, depositRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(new AccountDepositResponseDTO());

        } catch (AccountNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(ex.getMessage()));
        } catch (AccountIllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(ex.getMessage()));
        }
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<?> getBalance(@PathVariable Long accountId) {
        try {
            double balance = accountService.getBalance(accountId);
            return ResponseEntity.status(HttpStatus.OK).body(new AccountBalanceResponseDTO(balance));
        } catch (AccountNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(ex.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllAccounts() {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAllAccounts());
    }
}

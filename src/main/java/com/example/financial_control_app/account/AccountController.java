package com.example.financial_control_app.account;

import com.example.financial_control_app.dto.account.*;
import com.example.financial_control_app.exception.ErrorMessage;
import com.example.financial_control_app.exception.account.*;
import com.example.financial_control_app.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<?> create(@RequestBody AccountCreationRequestDTO accountToCreate, @AuthenticationPrincipal CustomUserDetails user) {
        try {
            accountToCreate.setUserId(user.getUserId());
            accountService.create(accountToCreate);
            return ResponseEntity.status(HttpStatus.CREATED).body(new AccountCreationResponseDTO());
        } catch (AccountDescriptionAlreadyExistsException | AccountDescriptionInvalidException |
                 AccountInvalidBalanceException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(ex.getMessage()));
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestParam DepositRequestDTO depositRequest, @AuthenticationPrincipal CustomUserDetails user) {
        try {
            Long accountId = user.getAccountId();
            accountService.deposit(accountId, depositRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(new AccountDepositResponseDTO());

        } catch (AccountNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(ex.getMessage()));
        } catch (AccountInvalidDepositAmountException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(ex.getMessage()));
        }
    }

    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(@AuthenticationPrincipal CustomUserDetails user) {
        try {
            Long accountId = user.getAccountId();
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

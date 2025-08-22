package com.example.financial_control_app.account;

import com.example.financial_control_app.dto.account.AccountCreationRequestDTO;
import com.example.financial_control_app.dto.account.DepositRequestDTO;
import com.example.financial_control_app.exception.AccountNotFoundException;
import com.example.financial_control_app.exception.AccountIllegalArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    public void create(AccountCreationRequestDTO account) {
        AccountModel accountToCreate = new AccountModel();

        if (account.owner() == null || account.owner().isEmpty()) {
            throw new AccountIllegalArgumentException("Account owner cannot be null or empty");
        }

        accountRepository.findByOwner(account.owner()).ifPresent(existingAccount -> {
            throw new AccountIllegalArgumentException("An account with this owner already exists");
        });

        accountToCreate.setOwner(account.owner());

        if (account.balance() < 0) {
            throw new AccountIllegalArgumentException("Initial balance cannot be negative");
        }

        accountToCreate.setBalance(account.balance());

        accountRepository.save(accountToCreate);
    }

    public void deposit(Long accountId, DepositRequestDTO depositRequest) {
        AccountModel account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + accountId + " does not exist"));

        if (depositRequest.value() <= 0) {
            throw new AccountIllegalArgumentException("Deposit value must be greater than zero");
        }

        account.setBalance(account.getBalance() + depositRequest.value());
        accountRepository.save(account);
    }

    public double getBalance(Long accountId) {
        AccountModel account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + accountId + " does not exist"));

        return account.getBalance();
    }

    public List<AccountModel> getAllAccounts() {
        return (List<AccountModel>) accountRepository.findAll();
    }
}

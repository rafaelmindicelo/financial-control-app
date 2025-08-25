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
        accountRepository.findByOwner(account.owner()).ifPresent(existingAccount -> {
            throw new AccountIllegalArgumentException("An account with this owner already exists");
        });

        AccountModel accountToCreate = new AccountModel(account.owner(), account.balance());

        accountRepository.save(accountToCreate);
    }

    public void deposit(Long accountId, DepositRequestDTO depositRequest) {
        AccountModel account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + accountId + " does not exist"));

        account.deposit(depositRequest.value());
        accountRepository.save(account);
    }

    public double getBalance(Long accountId) {
        AccountModel account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + accountId + " does not exist"));

        return account.getBalance();
    }

    public List<AccountModel> getAllAccounts() {
        return accountRepository.findAll();
    }
}

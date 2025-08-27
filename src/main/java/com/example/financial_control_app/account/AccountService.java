package com.example.financial_control_app.account;

import com.example.financial_control_app.dto.account.AccountCreationRequestDTO;
import com.example.financial_control_app.dto.account.DepositRequestDTO;
import com.example.financial_control_app.exception.account.AccountNotFoundException;
import com.example.financial_control_app.exception.account.AccountDescriptionAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    public void create(AccountCreationRequestDTO account) {
        accountRepository.findByDescription(account.getDescription()).ifPresent(existingAccount -> {
            throw new AccountDescriptionAlreadyExistsException("An account with this description already exists");
        });

        AccountModel accountToCreate = new AccountModel(account.getDescription(), account.getBalance());

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

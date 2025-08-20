package com.example.financial_control_app.expense;

import com.example.financial_control_app.account.AccountModel;
import com.example.financial_control_app.account.AccountRepository;
import com.example.financial_control_app.category.CategoryModel;
import com.example.financial_control_app.category.CategoryRepository;
import com.example.financial_control_app.exception.AccountNotFoundException;
import com.example.financial_control_app.exception.CategoryNotFoundException;
import com.example.financial_control_app.exception.ExpenseIllegalArgumentException;
import com.example.financial_control_app.exception.NullArgumentException;
import com.example.financial_control_app.expense.dtos.ExpenseCreationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<ExpenseModel> findByAccountId(Long accountId) {
        accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + accountId + " not found"));

        return expenseRepository.findByAccountId(accountId);
    }

    public void add(ExpenseCreationRequest expense) {
        ExpenseModel expenseModel = new ExpenseModel();

        if (expense.getDescription() == null || expense.getDescription().isEmpty()) {
            throw new ExpenseIllegalArgumentException("Expense name cannot be null or empty");
        }

        expenseModel.setDescription(expense.getDescription());

        if (expense.getAmount() <= 0) {
            throw new ExpenseIllegalArgumentException("Expense amount must be greater than zero");
        }

        expenseModel.setAmount(expense.getAmount());

        if (expense.getDate() == null) {
            expense.setDate(LocalDateTime.now());
        }

        expenseModel.setDate(expense.getDate());

        Optional<CategoryModel> categoryModel = categoryRepository.findById(expense.getCategoryId());

        if (categoryModel.isEmpty()) {
            throw new CategoryNotFoundException("Category with ID " + expense.getCategoryId() + " not found");
        }

        expenseModel.setCategory(categoryModel.get());

        if (expense.getAccountId() == null) {
            throw new NullArgumentException("Account ID cannot be null");
        }

        AccountModel account = accountRepository.findById(expense.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + expense.getAccountId() + " not found"));

        expenseModel.setAccount(account);

        expenseRepository.save(expenseModel);
    }

    public List<ExpenseModel> findByAccountIdAndYear(Long accountId, int year) {
        accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + accountId + " not found"));

        if (year < 1900 || year > LocalDate.now().getYear()) {
            throw new ExpenseIllegalArgumentException("Year must be between 1900 and the current year");
        }

        LocalDateTime startDate = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(year, 12, 31, 23, 59);

        return expenseRepository.findByAccountIdAndYear(accountId, startDate, endDate);
    }

    public List<ExpenseModel> findByAccountIdAndDateBetweenAndCategoryId(Long accountId, LocalDateTime startDateTime, LocalDateTime endDateTime, Integer categoryId) {
        accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + accountId + " not found"));

        if (categoryId != null) {
            categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException("Category with ID " + categoryId + " not found"));
        }

        return expenseRepository.findByAccountIdAndDateBetweenAndCategoryId(accountId, startDateTime, endDateTime, categoryId);
    }
}

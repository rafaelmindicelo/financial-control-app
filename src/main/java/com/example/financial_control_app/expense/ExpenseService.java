package com.example.financial_control_app.expense;

import com.example.financial_control_app.account.AccountModel;
import com.example.financial_control_app.account.AccountRepository;
import com.example.financial_control_app.category.CategoryModel;
import com.example.financial_control_app.category.CategoryRepository;
import com.example.financial_control_app.dto.expense.ExpenseCreationRequestDTO;
import com.example.financial_control_app.dto.expense.ExpenseFilterParams;
import com.example.financial_control_app.exception.account.AccountIdNullException;
import com.example.financial_control_app.exception.account.AccountNotFoundException;
import com.example.financial_control_app.exception.category.CategoryNotFoundException;
import com.example.financial_control_app.exception.expense.ExpenseDateInvalidException;
import com.example.financial_control_app.exception.expense.ExpenseYearInvalidException;
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

    public void create(ExpenseCreationRequestDTO expense) {
        Optional<CategoryModel> categoryModel = categoryRepository.findById(expense.getCategoryId());

        if (categoryModel.isEmpty()) {
            throw new CategoryNotFoundException("Category with ID " + expense.getCategoryId() + " not found");
        }

        AccountModel account = accountRepository.findById(expense.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + expense.getAccountId() + " not found"));


        ExpenseModel expenseModel = new ExpenseModel(expense.getDescription(), expense.getAmount(), expense.getDate(), categoryModel.get(), account);

        expenseRepository.save(expenseModel);
    }

    public List<ExpenseModel> findByAccountIdAndYear(Long accountId, int year) {
        if (accountId == null) {
            throw new AccountIdNullException("There are no expenses for an account without ID");
        }

        accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + accountId + " not found"));

        if (year < 1900 || year > LocalDate.now().getYear()) {
            throw new ExpenseYearInvalidException("Year must be between 1900 and the current year");
        }

        LocalDateTime startDate = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime endDate = year == LocalDateTime.now().getYear() ?
                LocalDateTime.now() :
                LocalDateTime.of(year, 12, 31, 23, 59);

        return expenseRepository.findByAccountIdAndYear(accountId, startDate, endDate);
    }

    public List<ExpenseModel> findByAccountIdAndDateBetweenAndCategoryId(Long accountId, ExpenseFilterParams filter) {
        if (accountId == null) {
            throw new AccountIdNullException("There are no expenses for an account without ID");
        }

        accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + accountId + " not found"));

        if (filter.categoryId() != null) {
            categoryRepository.findById(filter.categoryId()).orElseThrow(() -> new CategoryNotFoundException("Category with ID " + filter.categoryId() + " not found"));
        }

        if (filter.startDate() != null && filter.endDate() != null && filter.startDate().isAfter(filter.endDate())) {
            throw new ExpenseDateInvalidException("Start date cannot be after end date");
        }

        LocalDateTime startDateTime = filter.startDate() == null ?
                LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), 1, 0, 0)
                : filter.startDate();

        LocalDateTime endDateTime = filter.endDate() == null ?
                LocalDateTime.now()
                : filter.endDate();

        return expenseRepository.findByAccountIdAndDateBetweenAndCategoryId(accountId, startDateTime, endDateTime, filter.categoryId());
    }
}

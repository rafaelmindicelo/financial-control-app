package com.example.financial_control_app.expense;

import com.example.financial_control_app.dto.expense.ExpenseFilterDTO;
import com.example.financial_control_app.dto.expense.ExpenseFilterParams;
import com.example.financial_control_app.exception.*;
import com.example.financial_control_app.dto.expense.ExpenseCreationRequestDTO;
import com.example.financial_control_app.dto.expense.ExpenseCreationResponseDTO;
import com.example.financial_control_app.exception.account.AccountIllegalArgumentException;
import com.example.financial_control_app.exception.account.AccountNotFoundException;
import com.example.financial_control_app.exception.category.CategoryNotFoundException;
import com.example.financial_control_app.exception.expense.ExpenseIllegalArgumentException;
import com.example.financial_control_app.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    @Autowired
    private final ExpenseService expenseService;

    @GetMapping("/account/{accountId}")
    public ResponseEntity<?> findExpensesByAccountId(@PathVariable Long accountId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(expenseService.findByAccountId(accountId));
        } catch (AccountNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(ex.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> addExpense(@RequestBody ExpenseCreationRequestDTO expenseToCreate) {
        try {
            expenseService.add(expenseToCreate);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ExpenseCreationResponseDTO());
        } catch (ExpenseIllegalArgumentException | AccountIllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(ex.getMessage()));
        } catch (CategoryNotFoundException | AccountNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(ex.getMessage()));
        }
    }

    @GetMapping("/{accountId}/year/{year}")
    public ResponseEntity<?> filterExpensesByYear(@PathVariable Long accountId, @PathVariable int year) {
        try {
            List<ExpenseModel> filteredExpenses = expenseService.findByAccountIdAndYear(accountId, year);
            return ResponseEntity.status(HttpStatus.OK).body(filteredExpenses);
        } catch (AccountNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(ex.getMessage()));
        } catch (ExpenseIllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(ex.getMessage()));
        }
    }

    @GetMapping("/{accountId}/date-range")
    public ResponseEntity<?> filterExpensesByDateRangeAndCategory(
            @PathVariable Long accountId,
            @RequestParam(required = false) ExpenseFilterDTO filter) {

        try {
            ExpenseFilterParams filterParams = new ExpenseFilterParams(
                    filter.startDate() != null ? DateTimeUtils.parseToLocalDateTime(filter.startDate(), false) : null,
                    filter.endDate() != null ? DateTimeUtils.parseToLocalDateTime(filter.endDate(), true) : null,
                    filter.categoryId());

            List<ExpenseModel> filteredExpenses = expenseService.findByAccountIdAndDateBetweenAndCategoryId(accountId, filterParams);
            return ResponseEntity.status(HttpStatus.OK).body(filteredExpenses);
        } catch (AccountNotFoundException | CategoryNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(ex.getMessage()));
        } catch (java.time.format.DateTimeParseException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("Invalid date format. Please use ISO_LOCAL_DATE_TIME format."));
        }
    }
}

package com.example.financial_control_app.expense;

import com.example.financial_control_app.exception.*;
import com.example.financial_control_app.expense.dtos.ExpenseCreationRequest;
import com.example.financial_control_app.expense.dtos.ExpenseCreationResponse;
import com.example.financial_control_app.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    public ResponseEntity<?> addExpense(@RequestBody ExpenseCreationRequest expenseToCreate) {
        try {
            expenseService.add(expenseToCreate);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ExpenseCreationResponse());
        } catch (ExpenseIllegalArgumentException | NullArgumentException ex) {
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
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Integer categoryId) {

        try {
            LocalDateTime startDateTime = startDate == null ?
                    LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), 1, 0, 0)
                    : DateTimeUtils.parseToLocalDateTime(startDate);

            LocalDateTime endDateTime = endDate == null ? LocalDateTime.now() : DateTimeUtils.parseToLocalDateTime(endDate);

            System.out.println("Start Date: " + startDateTime);
            System.out.println("End Date: " + endDateTime);
            System.out.println("Category ID: " + categoryId);

            List<ExpenseModel> filteredExpenses = expenseService.findByAccountIdAndDateBetweenAndCategoryId(accountId, startDateTime, endDateTime, categoryId);
            return ResponseEntity.status(HttpStatus.OK).body(filteredExpenses);
        } catch (AccountNotFoundException | CategoryNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(ex.getMessage()));
        } catch (java.time.format.DateTimeParseException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("Invalid date format. Please use ISO_LOCAL_DATE_TIME format."));
        }
    }
}

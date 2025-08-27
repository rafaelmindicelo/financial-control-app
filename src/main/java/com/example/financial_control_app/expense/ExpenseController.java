package com.example.financial_control_app.expense;

import com.example.financial_control_app.dto.expense.ExpenseCreationRequestDTO;
import com.example.financial_control_app.dto.expense.ExpenseCreationResponseDTO;
import com.example.financial_control_app.dto.expense.ExpenseFilterDTO;
import com.example.financial_control_app.dto.expense.ExpenseFilterParams;
import com.example.financial_control_app.exception.ErrorMessage;
import com.example.financial_control_app.exception.account.AccountIdNullException;
import com.example.financial_control_app.exception.account.AccountNotFoundException;
import com.example.financial_control_app.exception.category.CategoryNotFoundException;
import com.example.financial_control_app.exception.expense.ExpenseAmountInvalidException;
import com.example.financial_control_app.exception.expense.ExpenseDateInvalidException;
import com.example.financial_control_app.exception.expense.ExpenseDescriptionInvalidException;
import com.example.financial_control_app.exception.expense.ExpenseYearInvalidException;
import com.example.financial_control_app.security.CustomUserDetails;
import com.example.financial_control_app.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeParseException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    @Autowired
    private final ExpenseService expenseService;

    @GetMapping()
    public ResponseEntity<?> findExpensesByAccountId(@AuthenticationPrincipal CustomUserDetails user) {
        try {
            Long accountId = user.getAccountId();
            return ResponseEntity.status(HttpStatus.OK).body(expenseService.findByAccountId(accountId));
        } catch (AccountNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(ex.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody ExpenseCreationRequestDTO expenseToCreate) {
        try {
            expenseService.create(expenseToCreate);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ExpenseCreationResponseDTO());
        } catch (ExpenseDescriptionInvalidException | ExpenseAmountInvalidException | AccountIdNullException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(ex.getMessage()));
        } catch (CategoryNotFoundException | AccountNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(ex.getMessage()));
        }
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<?> filterExpensesByYear(@AuthenticationPrincipal CustomUserDetails user, @PathVariable int year) {
        try {
            Long accountId = user.getAccountId();
            List<ExpenseModel> filteredExpenses = expenseService.findByAccountIdAndYear(accountId, year);
            return ResponseEntity.status(HttpStatus.OK).body(filteredExpenses);
        } catch (AccountNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(ex.getMessage()));
        } catch (AccountIdNullException | ExpenseYearInvalidException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(ex.getMessage()));
        }
    }

    @GetMapping("/date-range")
    public ResponseEntity<?> filterExpensesByDateRangeAndCategory(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(required = false) ExpenseFilterDTO filter) {

        try {
            Long accountId = user.getAccountId();
            ExpenseFilterParams filterParams = new ExpenseFilterParams(
                    filter.startDate() != null ? DateTimeUtils.parseToLocalDateTime(filter.startDate(), false) : null,
                    filter.endDate() != null ? DateTimeUtils.parseToLocalDateTime(filter.endDate(), true) : null,
                    filter.categoryId());

            List<ExpenseModel> filteredExpenses = expenseService.findByAccountIdAndDateBetweenAndCategoryId(accountId, filterParams);
            return ResponseEntity.status(HttpStatus.OK).body(filteredExpenses);
        } catch (AccountNotFoundException | CategoryNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(ex.getMessage()));
        } catch (DateTimeParseException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("Invalid date format. Please use ISO_LOCAL_DATE_TIME format."));
        } catch (AccountIdNullException | ExpenseDateInvalidException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(ex.getMessage()));
        }
    }
}

package com.example.financial_control_app.expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseModel, Long> {
    List<ExpenseModel> findByAccountId(Long accountId);

    @Query("SELECT e FROM ExpenseModel e WHERE e.account.id = :accountId AND e.date >= :startDate AND e.date <= :endDate")
    List<ExpenseModel> findByAccountIdAndYear(
            @Param("accountId") Long accountId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT e FROM ExpenseModel e WHERE e.account.id = :accountId AND e.date BETWEEN :startDate AND :endDate AND (:categoryId IS NULL OR e.category.id = :categoryId)")
    List<ExpenseModel> findByAccountIdAndDateBetweenAndCategoryId(
            @Param("accountId") Long accountId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("categoryId") Integer categoryId
    );
}

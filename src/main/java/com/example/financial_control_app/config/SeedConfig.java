package com.example.financial_control_app.config;

import com.example.financial_control_app.account.AccountModel;
import com.example.financial_control_app.account.AccountRepository;
import com.example.financial_control_app.category.CategoryModel;
import com.example.financial_control_app.category.CategoryRepository;
import com.example.financial_control_app.expense.ExpenseModel;
import com.example.financial_control_app.expense.ExpenseRepository;
import com.example.financial_control_app.user.UserModel;
import com.example.financial_control_app.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Random;


@Configuration
public class SeedConfig {
    private static final Logger logger = LoggerFactory.getLogger(SeedConfig.class);

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository,
                                   AccountRepository accountRepository,
                                   ExpenseRepository expenseRepository,
                                   CategoryRepository categoryRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            // avoids re-creating
            long usersCount = userRepository.count();
            if (usersCount > 0) {

                logger.warn("Found {} user(s) in the database. To run the seed, you need to clear the database first.", usersCount);
                return;
            }

            if (categoryRepository.count() == 0) {
                CategoryModel category1 = new CategoryModel();
                category1.setDescription("Fixas");
                categoryRepository.save(category1);

                CategoryModel category2 = new CategoryModel();
                category2.setDescription("Variáveis");
                categoryRepository.save(category2);

            }

            // 1. Creates user
            UserModel user = new UserModel();
            user.setUsername("jonhdoe");
            user.setEmail("jonh_doe@test.com");
            user.setPasswordHash(passwordEncoder.encode("123456"));
            user = userRepository.save(user);

            // 2. Creates account for the user
            AccountModel account = new AccountModel();
            account.setDescription("Conta Principal");
            account.setBalance(20000.00);
            account.setUser(user);
            account = accountRepository.save(account);
            user.setAccount(account);
            userRepository.save(user);

            // 3. Creates random expenses for the account
            Random random = new Random();
            LocalDate today = LocalDate.now();
            int expenseCount = 50;

            double totalExpenses = 0.0;

            for (int i = 0; i < expenseCount; i++) {
                CategoryModel category = new CategoryModel();
                category.setId(random.nextBoolean() ? 1 : 2);

                ExpenseModel expense = new ExpenseModel();
                expense.setDescription("Despesa " + (i + 1));
                expense.setAmount(Math.round((10 + (500 - 10) * random.nextDouble()) * 100.0) / 100.0); // values between 10 and 500
                expense.setCategory(category); // category 1 or 2

                // random date within the last 2 years
                int randomDays = random.nextInt(365);
                expense.setDate(today.minusDays(randomDays).atStartOfDay());

                expense.setAccount(account);

                expenseRepository.save(expense);
                totalExpenses += expense.getAmount();
            }

            // Updates balance after creating expenses
            account.setBalance(account.getBalance() - totalExpenses);
            accountRepository.save(account);

            logger.info("Seed executed successfully: user  + account + {} expenses created.", expenseCount);
        };
    }
}

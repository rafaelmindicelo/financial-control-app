package com.example.financial_control_app.account;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<AccountModel, Long> {
    Optional<Object> findByOwner(String owner);
}

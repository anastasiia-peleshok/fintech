package com.fintech.demo.service;

import com.fintech.demo.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    public List<Account> getAccounts();

    public Optional<Account> getAccountById(long theId);

    public Account saveAccount(Account theAccount);

    public void deleteAccount(long theId);

    Account updateEmployee(Account updatedAccount);

//to figure out update operation
}

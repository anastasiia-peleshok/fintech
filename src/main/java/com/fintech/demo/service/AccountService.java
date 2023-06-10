package com.fintech.demo.service;

import com.fintech.demo.entity.Account;
import com.fintech.demo.exceptions.CreditLimitExceedIncomeException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    public List<Account> getAccounts();

    public Optional<Account> getAccountById(long theId) throws IllegalArgumentException;

    public Account saveAccount(Account theAccount) throws IllegalArgumentException;

    public void deleteAccount(long theId);

    Account updateAccount(long accountId,Account updatedAccount) throws IllegalArgumentException;
    Account updateCreditLimit(long accountId, BigDecimal requestedCreditLimit) throws IllegalArgumentException, CreditLimitExceedIncomeException;

//to figure out update operation
}

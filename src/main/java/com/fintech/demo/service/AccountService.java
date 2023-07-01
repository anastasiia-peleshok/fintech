package com.fintech.demo.service;

import com.fintech.demo.entity.Account;
import com.fintech.demo.exceptions.CreditLimitExceedIncomeException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    public List<Account> getAccounts();

    public Optional<Account> getAccountById(long theId);

    public Account saveAccount(Account theAccount);

    public void deleteAccount(long theId);

    Account updateAccount(long accountId,Account updatedAccount);
    Account updateCreditLimit(long accountId, BigDecimal requestedCreditLimit);

//to figure out update operation
}

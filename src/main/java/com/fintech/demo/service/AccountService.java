package com.fintech.demo.service;

import com.fintech.demo.entity.Account;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {
     List<Account> getAccounts();

     Optional<Account> getAccountById(long theId);

     Account saveAccount(Account theAccount);

     void deleteAccount(long theId);

    Account updateAccount(long accountId,Account updatedAccount);
    Account updateCreditLimit(long accountId, BigDecimal requestedCreditLimit);

}

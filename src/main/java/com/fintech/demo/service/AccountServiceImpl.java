package com.fintech.demo.service;

import com.fintech.demo.dao.AccountRepository;
import com.fintech.demo.entity.Account;
import com.fintech.demo.exceptions.CreditLimitExceedIncomeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.DuplicateFormatFlagsException;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {

        this.accountRepository = accountRepository;
    }

    @Override
    public List<Account> getAccounts() {

        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> getAccountById(long theId) throws IllegalArgumentException {
        if (accountRepository.findById(theId).isEmpty()) {
            throw new IllegalArgumentException("Account with id" + theId + " is not found");
        }
        return accountRepository.findById(theId);
    }

    @Transactional
    @Override
    public Account saveAccount(Account newAccount) throws IllegalArgumentException {
        if (accountRepository.findById(newAccount.getId()).isPresent()) {
            throw new IllegalArgumentException("Account with id " + newAccount.getId() + " is already exist");
        } else if (accountRepository.existsByIdentifier(newAccount.getIdentifier())) {
            throw new IllegalArgumentException("Account with identifier " + newAccount.getIdentifier() + " is already exist");
        } else if (accountRepository.existsByPassportNumber(newAccount.getPassportNumber())) {
            throw new IllegalArgumentException("Account with  passport number " + newAccount.getPassportNumber() + " is already exist");
        } else {
            Account theAccount = accountRepository.save(newAccount);
            return theAccount;
        }

//        Optional<Account> savedAccount = accountRepository.findById(newAccount.getId());
//        if(savedAccount.isPresent()){
//            throw new IllegalArgumentException("Employee already exist with given email:" + newAccount.getId());
//        }
//        return accountRepository.save(newAccount);

    }

    @Transactional
    @Override
    public Account updateAccount(long accountId, Account updatedAccount) throws IllegalArgumentException {

        return accountRepository.findById(accountId).map(savedAccount -> {
                    savedAccount.setFirstName(updatedAccount.getFirstName());
                    savedAccount.setLastName(updatedAccount.getLastName());
                    savedAccount.setIdentifier(updatedAccount.getIdentifier());
                    savedAccount.setPassportNumber(updatedAccount.getPassportNumber());
                    savedAccount.setAnnualIncome(updatedAccount.getAnnualIncome());

                    Account theAccount = accountRepository.save(savedAccount);
                    return theAccount;
                })
                .orElseThrow(() -> new IllegalArgumentException("Account with id" + accountId + " is not found"));
    }

    @Override
    public Account updateCreditLimit(long accountId, BigDecimal requestedCreditLimit) throws IllegalArgumentException, CreditLimitExceedIncomeException {
        Optional<Account> theAccount = accountRepository.findById(accountId);
        if (theAccount.isEmpty()) {
            throw new IllegalArgumentException("Account with id " + accountId + " is not found");
        } else {
            BigDecimal monthlyIncome = theAccount.get().getAnnualIncome().divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
            if (monthlyIncome.compareTo(requestedCreditLimit) < 0) {
                throw new CreditLimitExceedIncomeException("Credit limit exceeds monthly income. Operation is declined!");
            } else {
                theAccount.get().setCreditLimit(requestedCreditLimit);
                Account updatedAccount = accountRepository.save(theAccount.get());
                return updatedAccount;
            }

        }
    }

    @Transactional
    @Override
    public void deleteAccount(long theId) {
        if (accountRepository.findById(theId).isEmpty()) {
            throw new IllegalArgumentException("Account with id" + theId + " is not found");
        }
        accountRepository.deleteById(theId);
    }
}

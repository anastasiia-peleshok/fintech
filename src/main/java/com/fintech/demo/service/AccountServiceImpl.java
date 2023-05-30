package com.fintech.demo.service;

import com.fintech.demo.dao.AccountRepository;
import com.fintech.demo.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {

        this.accountRepository = accountRepository;
    }

    @Transactional
    @Override
    public List<Account> getAccounts() {

        return accountRepository.findAll();
    }

    @Transactional
    @Override
    public Optional<Account> getAccountById(long theId) {
        return accountRepository.findById(theId);
    }

    @Transactional
    @Override
    public Account saveAccount(Account theAccount) {
        return accountRepository.save(theAccount);
    }

    @Transactional
    @Override
    public Account updateEmployee(Account updatedAccount) {
        return accountRepository.save(updatedAccount);
    }

    @Transactional
    @Override
    public void deleteAccount(long theId) {
        accountRepository.deleteById(theId);
    }
}

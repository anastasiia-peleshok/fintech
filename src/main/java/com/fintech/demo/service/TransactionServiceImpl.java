package com.fintech.demo.service;

import com.fintech.demo.dao.AccountRepository;
import com.fintech.demo.dao.TransactionRepository;
import com.fintech.demo.entity.Account;
import com.fintech.demo.entity.Transaction;
import com.fintech.demo.exceptions.AmountExceedBalanceException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
@PropertySource("src/main/resources/application.properties")
public class TransactionServiceImpl implements TransactionService {
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;
    @Value("${commissionPercent}")
    private double commissionPercent;

    @Autowired
    public TransactionServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {

        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    @Override
    public Transaction executeTransaction(long accountSenderId, long accountReceiverId, String description, BigDecimal amount) {

        Account senderAccount = accountRepository.findById(accountSenderId)
                .orElseThrow(() -> new IllegalArgumentException("Account with id " + accountSenderId + " is not found"));

        Account receiverAccount = accountRepository.findById(accountReceiverId)
                .orElseThrow(() -> new IllegalArgumentException("Account with id " + accountReceiverId + " is not found"));

        if (senderAccount.getAvailableBalance().compareTo(amount) < 0) {
            throw new AmountExceedBalanceException("Amount " + amount + " exceeds account balance");
        }

        Transaction transaction = new Transaction(accountSenderId, accountReceiverId, description, amount);
        senderAccount.setAvailableBalance(senderAccount.getAvailableBalance().subtract(amount));
        receiverAccount.setAvailableBalance(receiverAccount.getAvailableBalance().add(amount).subtract(calculateCommission(amount)));

             Transaction savedTransaction = transactionRepository.save(transaction);
            return savedTransaction;

    }
    private  BigDecimal calculateCommission(BigDecimal amount) {
        BigDecimal commission =amount.multiply(BigDecimal.valueOf(commissionPercent)).stripTrailingZeros();
        return commission;
    }
}


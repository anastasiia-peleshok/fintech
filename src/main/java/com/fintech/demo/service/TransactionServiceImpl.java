package com.fintech.demo.service;

import com.fintech.demo.configuration.ApplicationProperties;
import com.fintech.demo.dao.AccountRepository;
import com.fintech.demo.dao.TransactionRepository;
import com.fintech.demo.entity.Account;
import com.fintech.demo.entity.Transaction;
import com.fintech.demo.exceptions.AmountExceedBalanceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final ApplicationProperties applicationProperties;

    private  BigDecimal calculateCommission(BigDecimal amount) {
        BigDecimal commission =amount.multiply(BigDecimal.valueOf(applicationProperties.getCommissionPercent())).stripTrailingZeros();
        return commission;
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

        Transaction transaction = new Transaction(senderAccount, receiverAccount, description, amount);
        senderAccount.setAvailableBalance(senderAccount.getAvailableBalance().subtract(amount));
        receiverAccount.setAvailableBalance(receiverAccount.getAvailableBalance().add(amount).subtract(calculateCommission(amount)));

        Transaction savedTransaction = transactionRepository.save(transaction);
        return savedTransaction;

    }

}
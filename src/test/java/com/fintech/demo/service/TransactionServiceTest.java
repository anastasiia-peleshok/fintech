package com.fintech.demo.service;

import com.fintech.demo.dao.AccountRepository;
import com.fintech.demo.dao.TransactionRepository;
import com.fintech.demo.entity.Account;
import com.fintech.demo.entity.Transaction;
import com.fintech.demo.exceptions.AmountExceedBalanceException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fintech.demo.exceptions.CreditLimitExceedIncomeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.fail;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    public void execute_transaction_when_amount_exceed_balance_test() {
        long accountSenderId = 1;
        long accountReceiverId = 2;
        String description = "Test Transaction";
        BigDecimal amount = BigDecimal.valueOf(100);

        Account senderAccount = new Account();
        senderAccount.setAvailableBalance(BigDecimal.valueOf(50));

        Account receiverAccount = new Account();
        receiverAccount.setAvailableBalance(BigDecimal.ZERO);

        when(accountRepository.findById(accountSenderId)).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findById(accountReceiverId)).thenReturn(Optional.of(receiverAccount));

        assertThrows(AmountExceedBalanceException.class, () ->
                transactionService.executeTransaction(accountSenderId, accountReceiverId, description, amount)
        );
    }

    @Test
    public void execute_transaction_when_sender_is_not_present_test() {

        long accountSenderId = 1;
        long accountReceiverId = 2;
        String description = "Test Transaction";
        BigDecimal amount = BigDecimal.valueOf(100);

        Account senderAccount = new Account();
        senderAccount.setAvailableBalance(BigDecimal.valueOf(50));
        when(accountRepository.findById(accountSenderId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                transactionService.executeTransaction(accountSenderId, accountReceiverId, description, amount)
        );
    }

    @Test
    public void execute_transaction_when_receiver_is_not_present_test() {

        long accountSenderId = 1;
        long accountReceiverId = 2;
        String description = "Test Transaction";
        BigDecimal amount = BigDecimal.valueOf(100);

        Account receiverAccount = new Account();
        receiverAccount.setAvailableBalance(BigDecimal.valueOf(50));
        when(accountRepository.findById(accountSenderId)).thenReturn(Optional.empty());


        assertThrows(IllegalArgumentException.class, () ->
                transactionService.executeTransaction(accountSenderId, accountReceiverId, description, amount)
        );
    }

    @Test
    public void execute_transaction_success_test() {

        long accountSenderId = 1;
        long accountReceiverId = 2;
        String description = "Test Transaction";
        BigDecimal amount = BigDecimal.valueOf(100);

        Account senderAccount = new Account();
        senderAccount.setId(accountSenderId);
        senderAccount.setAvailableBalance(BigDecimal.valueOf(500));

        Account receiverAccount = new Account();
        receiverAccount.setId(accountReceiverId);
        receiverAccount.setAvailableBalance(BigDecimal.valueOf(200));

        when(accountRepository.findById(accountSenderId)).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findById(accountReceiverId)).thenReturn(Optional.of(receiverAccount));

        Transaction transaction = new Transaction(accountSenderId, accountReceiverId, description, amount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction savedTransaction = transactionService.executeTransaction(accountSenderId, accountReceiverId, description, amount);

        assertEquals(transaction, savedTransaction);
    }

}

package com.fintech.demo.service;

import com.fintech.demo.configuration.ApplicationProperties;
import com.fintech.demo.dao.AccountRepository;
import com.fintech.demo.dao.TransactionRepository;
import com.fintech.demo.entity.Account;
import com.fintech.demo.entity.Transaction;
import com.fintech.demo.exceptions.AmountExceedBalanceException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ApplicationProperties applicationProperties;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    public void execute_transaction_success_test() {
        Account accountSender=new Account();
        Account accountReceiver=new Account();
        accountSender.setId(1);
        accountReceiver.setId(2);
        long accountSenderId=1;
        long accountReceiverId=2;
        String description = "Test Transaction";
        BigDecimal amount = BigDecimal.valueOf(100);
        BigDecimal commissionPercent = new BigDecimal("0.04");

        Account senderAccount = new Account();
        senderAccount.setId(1);
        senderAccount.setAvailableBalance(BigDecimal.valueOf(500));

        Account receiverAccount = new Account();
        receiverAccount.setId(2);
        receiverAccount.setAvailableBalance(BigDecimal.valueOf(200));

        when(accountRepository.findById(accountSender.getId())).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findById(accountReceiver.getId())).thenReturn(Optional.of(receiverAccount));
        when(applicationProperties.getCommissionPercent()).thenReturn(commissionPercent.doubleValue());

        Transaction transaction = new Transaction(accountSender, accountReceiver, description, amount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction savedTransaction = transactionService.executeTransaction(accountSenderId, accountReceiverId, description, amount);

        assertEquals(transaction, savedTransaction);

        assertEquals(BigDecimal.valueOf(296), receiverAccount.getAvailableBalance());
    }
    @Test
    public void execute_transaction_when_amount_exceed_balance_test() {
        long accountSenderId =1;
        long accountReceiverId =2;
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

        long accountSenderId =1;
        long accountReceiverId =2;
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

        long accountSenderId =1;
        long accountReceiverId =2;
        String description = "Test Transaction";
        BigDecimal amount = BigDecimal.valueOf(100);

        Account receiverAccount = new Account();
        receiverAccount.setAvailableBalance(BigDecimal.valueOf(50));
        when(accountRepository.findById(accountSenderId)).thenReturn(Optional.empty());


        assertThrows(IllegalArgumentException.class, () ->
                transactionService.executeTransaction(accountSenderId, accountReceiverId, description, amount)
        );
    }



}

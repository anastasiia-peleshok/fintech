package com.fintech.demo.service;

import com.fintech.demo.entity.Transaction;

import java.math.BigDecimal;

public interface TransactionService {
    public Transaction executeTransaction(long accountSenderId, long accountReceiverId, String description, BigDecimal amount);
}

package com.fintech.demo.exceptions;

public class TransactionExecutionException extends RuntimeException{
    public TransactionExecutionException(String errorMessage) {
        super(errorMessage);
    }
}

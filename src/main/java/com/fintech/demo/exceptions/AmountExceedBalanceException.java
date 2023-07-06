package com.fintech.demo.exceptions;

public class AmountExceedBalanceException extends RuntimeException {
    public AmountExceedBalanceException(String errorMessage) {
        super(errorMessage);
    }
}
package com.fintech.demo.exceptions;

public class CreditLimitExceedIncomeException extends RuntimeException{
    public CreditLimitExceedIncomeException(String errorMessage) {
        super(errorMessage);
    }
}

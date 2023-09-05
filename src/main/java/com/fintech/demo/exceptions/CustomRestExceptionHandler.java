package com.fintech.demo.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(HttpServletRequest req,IllegalArgumentException ex ){
        return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }
    @ExceptionHandler(CreditLimitExceedIncomeException.class)
    public ResponseEntity<Object> handleCreditLimitExceedIncomeException(HttpServletRequest req,CreditLimitExceedIncomeException ex ){
        return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }
    @ExceptionHandler(AmountExceedBalanceException.class)
    public ResponseEntity<Object> handleAmountExceedBalanceException(HttpServletRequest req,AmountExceedBalanceException ex ){
        return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }
    @ExceptionHandler(TransactionExecutionException.class)
    public ResponseEntity<Object> handleTransactionExecutionException(HttpServletRequest req,AmountExceedBalanceException ex ){
        return buildResponseEntity(new ErrorResponse(HttpStatus.NOT_IMPLEMENTED, ex.getMessage()));
    }

    private ResponseEntity<Object> buildResponseEntity(ErrorResponse errorResponse){
        return new ResponseEntity<Object>(errorResponse,errorResponse.getStatus());
    }
}

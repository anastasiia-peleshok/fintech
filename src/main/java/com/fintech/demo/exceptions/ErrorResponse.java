package com.fintech.demo.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {
    private HttpStatus status;
    private LocalDateTime timeStamp;
    private String message;

    public ErrorResponse(HttpStatus status){
        this.status = status;
    }

    public ErrorResponse(){
        timeStamp=LocalDateTime.now();
    }

    public ErrorResponse(HttpStatus status, String message) {
        this();
        this.status = status;
        this.message = message;
    }

    public ErrorResponse(HttpStatus status, LocalDateTime timeStamp, String message) {
        this.status = status;
        this.timeStamp = timeStamp;
        this.message = message;
    }
}

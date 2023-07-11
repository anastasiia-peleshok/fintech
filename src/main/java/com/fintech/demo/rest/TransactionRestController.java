package com.fintech.demo.rest;

import com.fintech.demo.entity.Transaction;
import com.fintech.demo.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/transactions/")
@RequiredArgsConstructor
public class TransactionRestController {
    private final TransactionService transactionService;

    @PostMapping()
    @ResponseBody
    public ResponseEntity<Transaction> executeTransaction(@RequestParam("accountSenderId") long accountSenderId, @RequestParam("accountReceiverId")long accountReceiverId, @RequestParam("description")String description, @RequestParam("amount")BigDecimal amount) {
        return ResponseEntity.ok(transactionService.executeTransaction( accountSenderId,  accountReceiverId,  description, amount));
    }
}

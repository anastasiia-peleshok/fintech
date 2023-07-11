package com.fintech.demo.rest;

import com.fintech.demo.entity.Account;
import com.fintech.demo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts/")
@RequiredArgsConstructor
public class AccountRestController {
    private final AccountService accountService;

    @GetMapping(value = "{accountId}")
    @ResponseBody
    public ResponseEntity<Account> getAccount(@PathVariable long accountId){
        return ResponseEntity.ok(accountService.getAccountById(accountId).get());
    }

    @GetMapping()
    @ResponseBody
    public ResponseEntity<List<Account>> getAccounts() {
        return ResponseEntity.ok(accountService.getAccounts());
    }

    @PostMapping()
    @ResponseBody
    public ResponseEntity<Account> saveAccount(@RequestBody Account newAccount) {
        return ResponseEntity.ok(accountService.saveAccount(newAccount));
    }

    @DeleteMapping("{accountId}")
    @ResponseBody
    public ResponseEntity<Void> deleteAccount(@PathVariable long accountId){
        accountService.deleteAccount(accountId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{accountId}")
    @ResponseBody
    public ResponseEntity<Account> updateAccount(@PathVariable long accountId, @RequestBody Account theAccount) {
        return ResponseEntity.ok(accountService.updateAccount(accountId, theAccount));
    }

    @PutMapping("{accountId}/credit-limit")
    @ResponseBody
    public ResponseEntity<Account> updateCreditLimit(@PathVariable long accountId, @RequestParam BigDecimal requestedCreditLimit){
        return ResponseEntity.ok(accountService.updateCreditLimit(accountId, requestedCreditLimit));
    }


}

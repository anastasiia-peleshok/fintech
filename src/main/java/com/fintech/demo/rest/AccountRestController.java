package com.fintech.demo.rest;

import com.fintech.demo.entity.Account;
import com.fintech.demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
public class AccountRestController {
    private AccountService accountService;

    @Autowired
    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("{accountId}")
    @ResponseBody
    public ResponseEntity<String> getAccounts(@PathVariable long accountId) {
        if (accountService.getAccountById(accountId).isEmpty()) {
            return new ResponseEntity("Account with id " + accountId + " is not found", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(accountService.getAccountById(accountId).get(), HttpStatus.OK);
        }

    }

    @GetMapping("accounts")
    @ResponseBody
    public ResponseEntity<String> getAccounts() {
        return new ResponseEntity(accountService.getAccounts(), HttpStatus.OK);

    }

    @PostMapping("newAccount")
    @ResponseBody
    public ResponseEntity<String> saveAccount(@RequestParam Account newAccount) {
        if (accountService.getAccounts().stream().map(a -> a.getIdentifier()).anyMatch(a -> a.equals(newAccount.getIdentifier()))) {
            return new ResponseEntity("Account with identifier " + newAccount.getIdentifier() + " is already exist", HttpStatus.BAD_REQUEST);
        } else if (accountService.getAccounts().stream().map(a -> a.getPassportNumber()).anyMatch(a -> a.equals(newAccount.getPassportNumber()))) {
            return new ResponseEntity("Account with  passport number " + newAccount.getPassportNumber() + " is already exist", HttpStatus.BAD_REQUEST);
        } else {
            Account theAccount = accountService.saveAccount(newAccount);
            return new ResponseEntity("Account with id " + theAccount.getId() + " was successfully created", HttpStatus.CREATED);
        }

    }

    @DeleteMapping("{accountId}")
    @ResponseBody
    public ResponseEntity<String> deleteAccount(@PathVariable long accountId) {
        if (accountService.getAccountById(accountId).isEmpty()) {
            return new ResponseEntity("Account with id " + accountId + " is not found", HttpStatus.NOT_FOUND);
        } else {
            accountService.deleteAccount(accountId);
            return new ResponseEntity("Account with id " + accountId + " was successfully deleted", HttpStatus.OK);
        }
    }

    @PutMapping("{accountId}")
    @ResponseBody
    public ResponseEntity<Account> updateCreditLimit(@PathVariable long accountId, @RequestParam Account theAccount) {
        return accountService.getAccountById(accountId)
                .map(savedAccount -> {
                    savedAccount.setFirstName(theAccount.getFirstName());
                    savedAccount.setLastName(theAccount.getLastName());
                    savedAccount.setIdentifier(theAccount.getIdentifier());
                    savedAccount.setPassportNumber(theAccount.getPassportNumber());
                    savedAccount.setAnnualIncome(theAccount.getAnnualIncome());

                    Account updatedAccount = accountService.updateEmployee(savedAccount);
                    return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("creditLimitUpdate/{accountId}")
    @ResponseBody
    public ResponseEntity<String> updateCreditLimit(@PathVariable long accountId, @RequestParam BigDecimal requestedCreditLimit) {
        Optional<Account> theAccount = accountService.getAccountById(accountId);
        if (theAccount.isEmpty()) {
            return new ResponseEntity("Account with id " + accountId + " is not found", HttpStatus.NOT_FOUND);
        } else if (theAccount.get().getAnnualIncome().divide(BigDecimal.valueOf(12)).compareTo(requestedCreditLimit) < 0) {
            return new ResponseEntity("Credit limit exceed monthly income. Operation is declined!", HttpStatus.NOT_MODIFIED);
        } else {
            theAccount.get().setCreditLimit(requestedCreditLimit);
            accountService.updateEmployee(theAccount.get());
            return new ResponseEntity("Credit limit was successfully changed", HttpStatus.OK);
        }
    }


}

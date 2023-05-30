package com.fintech.demo.service;

import com.fintech.demo.dao.AccountRepository;
import com.fintech.demo.entity.Account;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTests {
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;

    @BeforeEach
    public void setup() {
        account =
                new Account ("Anastasiia","Peleshok",123L, 34562356L,BigDecimal.valueOf(100000L));
    }

    @DisplayName("JUnit test for saveEmployee method")
    @Test
    public void givenAccountObject_whenSaveAccount_thenReturnAccountObject() {
        given(accountRepository.findById(account.getId()))
                .willReturn(Optional.empty());
        given(accountRepository.save(account))
                .willReturn(account);
        System.out.println(accountRepository);
        System.out.println(accountService);
        Account savedAccount = accountService.saveAccount(account);
        System.out.println(savedAccount);

        assertThat(savedAccount).isNotNull();
    }
}

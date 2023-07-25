package com.fintech.demo.service;
import com.fintech.demo.dao.AccountRepository;
import com.fintech.demo.entity.Account;
import com.fintech.demo.exceptions.CreditLimitExceedIncomeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTests {
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;

    @DisplayName("JUnit test for saveAccount method successfully")
    @Test
    public void save_account_success_test() {
        account = new Account();
        when(accountRepository.existsByIdentifier(account.getIdentifier()))
                .thenReturn(false);
        when(accountRepository.existsByPassportNumber(account.getPassportNumber()))
                .thenReturn(false);
        when(accountRepository.save(account))
                .thenReturn(account);
        Account savedAccount = accountService.saveAccount(account);
        assertEquals(account, savedAccount);
    }

    @DisplayName("JUnit test for saveAccount method when id is already exist")
    @Test
    public void save_account_when_id_is_exist_test() {
        account = new Account();
        when(accountRepository.findById(account.getId()))
                .thenReturn(Optional.of(account));
        assertThatThrownBy(() -> {
            accountService.saveAccount(account);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Account with id " + account.getId() + " is already exist");
    }


    @DisplayName("JUnit test for saveAccount method when identifier is already exist")
    @Test
    public void save_account_when_identifier_is_exist_test() {
        account = new Account();
        when(accountRepository.existsByIdentifier(account.getIdentifier()))
                .thenReturn(true);
        assertThatThrownBy(() -> {
            accountService.saveAccount(account);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Account with identifier " + account.getIdentifier() + " is already exist");

    }

    @DisplayName("JUnit test for saveAccount method when passport is already exist")
    @Test
    public void save_account_when_passport_is_exist_test() {
        account = new Account();
        when(accountRepository.existsByPassportNumber(account.getPassportNumber()))
                .thenReturn(true);
        assertThatThrownBy(() -> {
            accountService.saveAccount(account);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Account with  passport number " + account.getPassportNumber() + " is already exist");
    }

    @DisplayName("JUnit test for getAccount method successfully")
    @Test
    public void get_account_by_id_successes_test() {
        account = new Account();
        when(accountRepository.findById(account.getId()))
                .thenReturn(Optional.of(account));

        Account savedAccount = accountService.getAccountById(account.getId()).get();
        assertEquals(account, savedAccount);
    }

    @DisplayName("JUnit test for getAccount method when account is not found")
    @Test
    public void get_account_by_id_when_account_not_found_test() {
        account = new Account();
        when(accountRepository.findById(account.getId())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> {
            accountService.getAccountById(account.getId()).get();
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Account with id" + account.getId() + " is not found");
    }

    @DisplayName("JUnit test for getAccounts method")
    @Test
    public void get_accounts_test() {
        Account account1 = new Account();
        Account account2 = new Account();
        given(accountRepository.findAll()).willReturn(List.of(account1, account2));

        List<Account> list = accountService.getAccounts();
        assertEquals(List.of(account1, account2), list);
    }

    @DisplayName("JUnit test for updateAccount method successfully")
    @Test
    public void update_account_success_test() {
        long accountId = 1L;
        Account existingAccount = new Account();
        existingAccount.setId(accountId);

        Account updatedAccount = new Account();
        updatedAccount.setId(accountId);
        updatedAccount.setFirstName("John");
        updatedAccount.setLastName("Doe");
        updatedAccount.setIdentifier(123L);
        updatedAccount.setPassportNumber(123456789L);
        updatedAccount.setAnnualIncome(BigDecimal.valueOf(100000L));

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(existingAccount)).thenReturn(updatedAccount);

        Account result = accountService.updateAccount(accountId, updatedAccount);
        assertEquals(updatedAccount.getFirstName(), result.getFirstName());
        assertEquals(updatedAccount.getLastName(), result.getLastName());
        assertEquals(updatedAccount.getIdentifier(), result.getIdentifier());
        assertEquals(updatedAccount.getPassportNumber(), result.getPassportNumber());
        assertEquals(updatedAccount.getAnnualIncome(), result.getAnnualIncome());
    }

    @DisplayName("JUnit test for updateAccount method when account is not found")
    @Test
    public void update_account_when_account_not_found_test() {
        long accountId = 1L;
        Account updatedAccount = new Account();
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> {
            accountService.updateAccount(accountId, updatedAccount);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Account with id" + accountId + " is not found");
    }

    @DisplayName("JUnit test for updateCreditLimit method successfully")
    @Test
    public void update_credit_limit_success_test() {
        long accountId = 1L;
        BigDecimal currentIncome = BigDecimal.valueOf(5000);
        BigDecimal requestedCreditLimit = BigDecimal.valueOf(10);

        Account existingAccount = new Account();
        existingAccount.setId(accountId);
        existingAccount.setAnnualIncome(currentIncome);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(existingAccount)).thenReturn(existingAccount);

        Account result = accountService.updateCreditLimit(accountId, requestedCreditLimit);

        assertEquals(requestedCreditLimit, result.getCreditLimit());
    }

    @DisplayName("JUnit test for updateCreditLimit method when Account not found")
    @Test
    public void update_credit_limit_when_account_not_found_test() {
        long accountId = 1L;
        BigDecimal requestedCreditLimit = BigDecimal.valueOf(1000);
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> {
            accountService.updateCreditLimit(accountId, requestedCreditLimit);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Account with id " + accountId + " is not found");
    }

    @DisplayName("JUnit test for updateCreditLimit method when income exceed limit")
    @Test
    public void update_credit_limit_when_exceed_income_test() {
        long accountId = 1L;
        BigDecimal currentIncome = BigDecimal.valueOf(5000);
        BigDecimal requestedCreditLimit = BigDecimal.valueOf(6000);

        Account existingAccount = new Account();
        existingAccount.setId(accountId);
        existingAccount.setAnnualIncome(currentIncome);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
        assertThatThrownBy(() -> {
            accountService.updateCreditLimit(accountId, requestedCreditLimit);
        }).isInstanceOf(CreditLimitExceedIncomeException.class)
                .hasMessageContaining("Credit limit exceeds monthly income. Operation is declined!");
    }

    @DisplayName("JUnit test for deleteAccount method when account is not found")
    @Test
    public void delete_account_when_account_not_fount_test() {
        long accountId = 1L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> {
            accountService.deleteAccount(accountId);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Account with id" + accountId + " is not found");
    }

    @DisplayName("JUnit test for deleteAccount method successfully")
    @Test
    public void delete_account_success_test() {
        long accountId = 1L;
        Account existingAccount = new Account();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));

        accountService.deleteAccount(accountId);
        assertFalse(accountRepository.findAll().stream().anyMatch(account -> account.getId() == accountId));
    }
}



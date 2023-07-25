package com.fintech.demo.rest;

import com.fintech.demo.entity.Account;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class AccountRestControllerTest extends  AbstractIntegrationTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Container
    private static final MySQLContainer mysqlContainer = new MySQLContainer("mysql:latest")
            .withDatabaseName("test")
            .withUsername("root")
            .withPassword("password");

    @BeforeAll
    static void beforeAll() {
        mysqlContainer.start();
        System.setProperty("spring.datasource.url", mysqlContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", mysqlContainer.getUsername());
        System.setProperty("spring.datasource.password", mysqlContainer.getPassword());
    }

    @Test
    public void get_account_by_id_when_id_is_present() {
        // Your test logic here
        ResponseEntity<Account> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/accounts/{accountId}",
                Account.class,
                1L
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void get_account_by_id_when_id_is_not_present() {
        ResponseEntity<Account> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/accounts/{accountId}",
                Account.class,
                7L
        );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void get_accounts_test() {
        ResponseEntity<List<Account>> response = restTemplate.exchange(
                "http://localhost:" + port + "/accounts/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Account>>() {
                });
        List<Account> employees = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testSaveAccount() {
        Account newAccount = new Account("Svet", "Pelsdf", 98755L, 987651L, new BigDecimal(BigInteger.valueOf(50000L)));
        ResponseEntity<Account> response = restTemplate.postForEntity("http://localhost:" + port + "/accounts/", newAccount, Account.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void save_account_when_identifier_is_duplicated() {
        Account newAccount = new Account("Svet", "Pelsdf", 123456789L, 987651L, new BigDecimal(BigInteger.valueOf(50000L)));
        ResponseEntity<Account> response = restTemplate.postForEntity("http://localhost:" + port + "/accounts/", newAccount, Account.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void save_account_when_passport_number_is_duplicated() {
        Account newAccount = new Account("Svet", "Pelsdf", 1234569L, 123456789L, new BigDecimal(BigInteger.valueOf(50000L)));
        ResponseEntity<Account> response = restTemplate.postForEntity("http://localhost:" + port + "/accounts/", newAccount, Account.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void delete_account_when_id_is_present() {
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/accounts/{accountId}",
                HttpMethod.DELETE,
                null,
                Void.class,
                1L
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void delete_account_when_id_is_not_present() {
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/accounts/{accountId}",
                HttpMethod.DELETE,
                null,
                Void.class,
                7L
        );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void update_account_when_id_is_present() {
        Account updatedAccount = new Account("Updated", "Account", 123456789, 987654321, new BigDecimal("60000.00"));
        ResponseEntity<Account> response = restTemplate.exchange(
                "http://localhost:" + port + "/accounts/{accountId}",
                HttpMethod.PUT,
                new HttpEntity<>(updatedAccount),
                Account.class,
                1L
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void update_account_when_id_is_not_present() {
        Account updatedAccount = new Account("Updated", "Account", 123456789, 987654321, new BigDecimal("60000.00"));
        ResponseEntity<Account> response = restTemplate.exchange(
                "http://localhost:" + port + "/accounts/{accountId}",
                HttpMethod.PUT,
                new HttpEntity<>(updatedAccount),
                Account.class,
                9L
        );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

    }

    @Test
    public void update_credit_limit_when_limit_exceed_income() {
        BigDecimal newCreditLimit = BigDecimal.valueOf(100000L);
        ResponseEntity<Account> updateResponse = restTemplate.exchange(
                "http://localhost:" + port + "/accounts/{accountId}/credit-limit?requestedCreditLimit={requestedCreditLimit}",
                HttpMethod.PUT,
                new HttpEntity<>(newCreditLimit),
                Account.class,
                1L,
                newCreditLimit
        );
        assertEquals(HttpStatus.BAD_REQUEST, updateResponse.getStatusCode());
    }

    @Test
    public void update_credit_limit_when_limit_is_not_exceed_income() {
        BigDecimal newCreditLimit = BigDecimal.valueOf(500L);
        ResponseEntity<Account> updateResponse = restTemplate.exchange(
                "http://localhost:" + port + "/accounts/{accountId}/credit-limit?requestedCreditLimit={requestedCreditLimit}",
                HttpMethod.PUT,
                new HttpEntity<>(newCreditLimit),
                Account.class,
                1L,
                newCreditLimit
        );
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertEquals(BigDecimal.valueOf(500L), updateResponse.getBody().getCreditLimit());
    }
}

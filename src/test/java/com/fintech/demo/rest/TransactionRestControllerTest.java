package com.fintech.demo.rest;

import com.fintech.demo.entity.Account;
import com.fintech.demo.entity.Transaction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class TransactionRestControllerTest extends AbstractIntegrationTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

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
    public void execute_transaction_when_balance_bigger_amount() {
        Account accountSender=new Account();
        Account accountReceiver=new Account();
        accountSender.setId(1);
        accountReceiver.setId(2);
        String description = "Transaction description";
        BigDecimal amount = new BigDecimal("100.00");

        String url = "http://localhost:" + port + "/transactions/";

        MultiValueMap<String, Object> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("accountSenderId", accountSender.getId());
        requestParams.add("accountReceiverId", accountReceiver.getId());
        requestParams.add("description", description);
        requestParams.add("amount", amount);

        ResponseEntity<Transaction> response = restTemplate.postForEntity(
                url,
                requestParams,
                Transaction.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    public void execute_transaction_when_balance_smaller_amount() {
        long accountSenderId =1;
        long accountReceiverId = 2;
        String description = "Transaction description";
        BigDecimal amount = new BigDecimal("1000.00");

        String url = "http://localhost:" + port + "/transactions/";

        MultiValueMap<String, Object> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("accountSenderId", accountSenderId);
        requestParams.add("accountReceiverId", accountReceiverId);
        requestParams.add("description", description);
        requestParams.add("amount", amount);

        ResponseEntity<Transaction> response = restTemplate.postForEntity(
                url,
                requestParams,
                Transaction.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void execute_transaction_when_sender_is_not_present() {
        long accountSenderId =10;
        long accountReceiverId = 2;
        String description = "Transaction description";
        BigDecimal amount = new BigDecimal("1000.00");

        String url = "http://localhost:" + port + "/transactions/";

        MultiValueMap<String, Object> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("accountSenderId", accountSenderId);
        requestParams.add("accountReceiverId", accountReceiverId);
        requestParams.add("description", description);
        requestParams.add("amount", amount);

        ResponseEntity<Transaction> response = restTemplate.postForEntity(
                url,
                requestParams,
                Transaction.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }
    @Test
    public void execute_transaction_when_receiver_is_not_present() {
        long accountSenderId =1;
        long accountReceiverId = 20;
        String description = "Transaction description";
        BigDecimal amount = new BigDecimal("1000.00");

        String url = "http://localhost:" + port + "/transactions/";

        MultiValueMap<String, Object> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("accountSenderId", accountSenderId);
        requestParams.add("accountReceiverId", accountReceiverId);
        requestParams.add("description", description);
        requestParams.add("amount", amount);

        ResponseEntity<Transaction> response = restTemplate.postForEntity(
                url,
                requestParams,
                Transaction.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }
}




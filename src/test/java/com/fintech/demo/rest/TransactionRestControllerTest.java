package com.fintech.demo.rest;

import com.fintech.demo.entity.Account;
import com.fintech.demo.entity.Transaction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class TransactionRestControllerTest extends AbstractIntegrationTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
//    @Autowired
//    private JdbcTemplate jdbcTemplate;

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

//    @BeforeEach
//    public void setUp() {
//        executeSqlScript("schema.sql");
//        executeSqlScript("data.sql");
//    }

//    private void executeSqlScript(String scriptPath) {
//        Resource resource = new ClassPathResource(scriptPath);
//        try (InputStream inputStream = resource.getInputStream()) {
//            String sqlScript = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
//            String[] queries = sqlScript.split(";");
//
//            for (String query : queries) {
//                jdbcTemplate.execute(query.trim());
//            }
//            System.out.println("Successfully executed Scripts");
//        } catch (IOException e) {
//            throw new UncheckedIOException("Failed to read SQL script: " + scriptPath, e);
//        }
//    }

    @Test
    public void execute_transaction_when_balance_bigger_amount() {
        long accountSenderId = 1;
        long accountReceiverId = 2;
        String description = "Transaction description";
        BigDecimal amount = new BigDecimal("100.00");


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

        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    public void execute_transaction_when_balance_smaller_amount() {
        long accountSenderId = 1;
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
        long accountSenderId = 10;
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
        long accountSenderId = 1;
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




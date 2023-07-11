package com.fintech.demo.rest;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractIntegrationTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    protected void executeSqlScript(String scriptPath) {
        Resource resource = new ClassPathResource(scriptPath);
        try (InputStream inputStream = resource.getInputStream()) {
            String sqlScript = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            String[] queries = sqlScript.split(";");

            for (String query : queries) {
                jdbcTemplate.execute(query.trim());
            }
            System.out.println("Successfully executed Scripts");
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read SQL script: " + scriptPath, e);
        }
    }

    @BeforeEach
    public void setUp() {
        executeSqlScript("schema.sql");
        executeSqlScript("data.sql");
    }
}
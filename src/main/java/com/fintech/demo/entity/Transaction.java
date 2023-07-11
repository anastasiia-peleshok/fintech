package com.fintech.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.PropertySource;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Properties;

@Getter
@Setter
@ToString
@Entity
@PropertySource("classpath:application.properties")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @JsonProperty("id")
    private long id;
    @Column(name="account_sender_id")
    @JsonProperty("account_sender_id")
    private long accountSenderId;
    @Column(name="account_receiver_id")
//    @JsonProperty("account_receiver_id")
    private long accountReceiverId;
    @Column(name="description")
//    @JsonProperty("description")
    private String description;
    @Column(name="amount")
//    @JsonProperty("amount")
    private BigDecimal amount;
    @Column(name="stamp")
//    @JsonProperty("stamp")
    private Instant timestamp;

    public Transaction(long accountSenderId, long accountReceiverId, String description, BigDecimal amount) {
        this.accountSenderId = accountSenderId;
        this.accountReceiverId = accountReceiverId;
        this.description = description;
        this.amount = amount;
        this.timestamp=Instant.now();
    }

    public Transaction() {

    }

}

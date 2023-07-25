package com.fintech.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@ToString
@Entity
@Table(name="transaction_table")

public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "account_sender_id", referencedColumnName = "id")
    private Account accountSender;
    @ManyToOne
    @JoinColumn(name = "account_receiver_id", referencedColumnName = "id")
    private Account accountReceiver;
    @Column(name="description")
    private String description;
    @Column(name="amount")
    private BigDecimal amount;
    @Column(name="stamp")
    private Instant timestamp;


    public Transaction(Account accountSender, Account accountReceiver, String description, BigDecimal amount) {
        this.accountSender = accountSender;
        this.accountReceiver = accountReceiver;
        this.description = description;
        this.amount = amount;
        this.timestamp=Instant.now();
    }

    public Transaction() {

    }

}
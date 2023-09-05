package com.fintech.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private long id;
    @Column(name = "first_name")
    @JsonProperty("first_name")
    private String firstName;
    @Column(name = "last_name")
    @JsonProperty("last_name")
    private String lastName;
    @Column(name = "identifier")
    @JsonProperty("identifier")
    private long identifier;
    @Column(name = "passport_number")
    @JsonProperty("passport_number")
    private long passportNumber;
    @Column(name = "annual_income")
    @JsonProperty("annual_income")
    private BigDecimal annualIncome;
    @Column(name = "available_balance")
    @JsonProperty("available_balance")
    private BigDecimal availableBalance;
    @Column(name = "credit_balance")
    @JsonProperty("credit_balance")
    private BigDecimal creditBalance;
    @Column(name = "deposit_balance")
    @JsonProperty("deposit_balance")
    private BigDecimal depositBalance;
    @Column(name = "credit_limit")
    @JsonProperty("credit_limit")
    private BigDecimal creditLimit;

    @OneToMany(mappedBy = "accountSender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> sentTransactions;

    @OneToMany(mappedBy = "accountReceiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> receivedTransactions;


    public Account(String firstName, String lastName, long identifier, long passportNumber, BigDecimal annualIncome) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.identifier = identifier;
        this.passportNumber = passportNumber;
        this.annualIncome = annualIncome;
        this.availableBalance = BigDecimal.valueOf(500);
        this.creditBalance = BigDecimal.ZERO;
        this.depositBalance = BigDecimal.ZERO;
        this.creditLimit = BigDecimal.ZERO;
    }

}

package com.fintech.demo.entity;

import jakarta.persistence.*;
import lombok.Builder;

import java.math.BigDecimal;
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    private long identifier;
    private long passportNumber;
    private BigDecimal annualIncome;
    private BigDecimal availableBalance;
    private BigDecimal creditBalance;
    private BigDecimal depositBalance;
    private BigDecimal creditLimit;

    public Account() {

    }

    public Account(String firstName, String lastName, long identifier, long passportNumber, BigDecimal annualIncome) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.identifier = identifier;
        this.passportNumber = passportNumber;
        this.annualIncome = annualIncome;
        this.availableBalance = BigDecimal.ZERO;
        this.creditBalance = BigDecimal.ZERO;
        this.depositBalance = BigDecimal.ZERO;
        this.creditLimit = BigDecimal.ZERO;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(long identifier) {
        this.identifier = identifier;
    }

    public long getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(long passportNumber) {
        this.passportNumber = passportNumber;
    }

    public BigDecimal getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(BigDecimal annualIncome) {
        this.annualIncome = annualIncome;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

//    public void setAvailableBalance(BigDecimal availableBalance) {
//        this.availableBalance = availableBalance;
//    }

    public BigDecimal getCreditBalance() {
        return creditBalance;
    }

//    public void setCreditBalance(BigDecimal creditBalance) {
//        this.creditBalance = creditBalance;
//    }

    public BigDecimal getDepositBalance() {
        return depositBalance;
    }

//    public void setDepositBalance(BigDecimal depositBalance) {
//        this.depositBalance = depositBalance;
//    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", identifier=" + identifier +
                ", passportNumber=" + passportNumber +
                ", annualIncome=" + annualIncome +
                ", availableBalance=" + availableBalance +
                ", creditBalance=" + creditBalance +
                ", depositBalance=" + depositBalance +
                ", creditLimit=" + creditLimit +
                '}';
    }
}

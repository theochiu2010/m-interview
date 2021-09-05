package com.acmebank.accountmanager.service.dto;

public class AccountBalanceDto {
    private String acctNumber;
    private Double balance;

    public AccountBalanceDto(String acctNumber, Double balance) {
        this.acctNumber = acctNumber;
        this.balance = balance;
    }

    public String getAcctNumber() {
        return acctNumber;
    }

    public Double getBalance() {
        return balance;
    }
}

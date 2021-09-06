package com.acmebank.accountmanager.service.dto;

public class AccountBalanceDto {
    private String acctNumber;
    private Double balance;
    private String currency;

    public AccountBalanceDto(String acctNumber, Double balance, String currency) {
        this.acctNumber = acctNumber;
        this.balance = balance;
        this.currency = currency;
    }

    public String getAcctNumber() {
        return acctNumber;
    }

    public Double getBalance() {
        return balance;
    }

    public String getCurrency() { return currency; }
}

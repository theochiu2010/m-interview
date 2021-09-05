package com.acmebank.accountmanager.dao;

import javax.persistence.*;

@Entity
@Table(name = "account")
public class Account {
    public Account() {

    }

    public Account(String acctNumber, Double balance) {
        this.acctNumber = acctNumber;
        this.balance = balance;
    }

    private Account(AccountBuilder builder) {
        this.customerId = builder.customerId;
        this.acctNumber = builder.acctNumber;
        this.balance = builder.balance;
        this.currency = builder.currency;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "customerId", nullable = false)
    private String customerId;

    @Column(name = "acctNumber", nullable = false)
    private String acctNumber;

    @Column(name= "balance", precision=10, scale=2, nullable = false)
    private Double balance;

    @Column(name = "currency", nullable = false)
    private String currency;

    public Integer getId() {
        return id;
    }

    public String getAcctNumber() {
        return acctNumber;
    }

    public Double getBalance() {
        return balance;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCurrency() {
        return currency;
    }

    @Transient
    public void transfer(Account transferToAccount, Double transferAmount) throws Exception {
        if (this.balance < transferAmount) {
            throw new Exception("Account: " + acctNumber + " has insufficient fund");
        }

        debitFromAccount(this, transferAmount);
        creditToAccount(transferToAccount, transferAmount);
    }

    private void debitFromAccount(Account account, Double debitAmount) {
        account.balance -= debitAmount;
    }

    private void creditToAccount(Account account, Double creditAmount) {
        account.balance += creditAmount;
    }

    public static class AccountBuilder
    {
        private String customerId;
        private String acctNumber;
        private Double balance;
        private String currency;

        public AccountBuilder() {
        }

        public AccountBuilder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public AccountBuilder acctNumber(String acctNumber) {
            this.acctNumber = acctNumber;
            return this;
        }

        public AccountBuilder balance(Double balance) {
            this.balance = balance;
            return this;
        }

        public AccountBuilder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Account build() {
            return new Account(this);
        }
    }
}

package com.acmebank.accountmanager.dao;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AccountTest {
    @Test
    void able_to_build_account_test() {
        Account account = new Account.AccountBuilder()
                .customerId("1000001")
                .acctNumber("12345678")
                .balance(1000000.0)
                .currency("HKD")
                .build();

        assertThat(account.getCustomerId()).isEqualTo("1000001");
        assertThat(account.getAcctNumber()).isEqualTo("12345678");
        assertThat(account.getBalance()).isEqualTo(1000000.0);
        assertThat(account.getCurrency()).isEqualTo("HKD");

    }
}

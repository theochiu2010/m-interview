package com.acmebank.accountmanager.configuration;

import com.acmebank.accountmanager.dao.Account;
import com.acmebank.accountmanager.repository.AccountRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class dbConfiguration {
    @Bean
    public ApplicationRunner initializer(AccountRepository repository) {
        boolean hasDefaultData = repository.count() > 0;

        if (!hasDefaultData) {
            return args -> repository.saveAll(Arrays.asList(
                    new Account.AccountBuilder().customerId("1000001").acctNumber("12345678").balance(1000000.0).currency("HKD").build(),
                    new Account.AccountBuilder().customerId("1000001").acctNumber("88888888").balance(1000000.0).currency("HKD").build()
            ));
        }

        return args -> {};
    }
}

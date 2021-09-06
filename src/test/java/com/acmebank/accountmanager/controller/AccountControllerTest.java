package com.acmebank.accountmanager.controller;

import com.acmebank.accountmanager.controller.dto.TransferBalanceRequestDto;
import com.acmebank.accountmanager.dao.Account;
import com.acmebank.accountmanager.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void setup() {
        accountRepository.deleteAll();

        accountRepository.saveAll(Arrays.asList(
                new Account.AccountBuilder().customerId("1000001").acctNumber("12345678").balance(1000000.0).currency("HKD").build(),
                new Account.AccountBuilder().customerId("1000001").acctNumber("88888888").balance(1000000.0).currency("HKD").build()
        ));
    }

    @Test
    void should_be_able_to_get_account_balance_test() throws Exception {
        this.mockMvc.perform(get("/accounts/12345678/balance"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"acctNumber\":\"12345678\",\"balance\":1000000.0,\"currency\":\"HKD\"}"));
    }

    @Test
    void should_not_be_able_to_get_account_balance_when_account_not_exist_test() throws Exception {
        this.mockMvc.perform(get("/accounts/11111111/balance"))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("No account with account number: 11111111 exist"));
    }

    @Test
    void should_be_able_to_transfer_between_accounts_test() throws Exception {
        TransferBalanceRequestDto postRequest = new TransferBalanceRequestDto();
        postRequest.fromAcctNumber = "12345678";
        postRequest.toAcctNumber = "88888888";
        postRequest.amount = 5000.0;

        this.mockMvc.perform(post("/accounts/transfer")
                .content(asJsonString(postRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\":\"The transfer was successful\"}"));

        this.mockMvc.perform(get("/accounts/12345678/balance"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"acctNumber\":\"12345678\",\"balance\":995000.0,\"currency\":\"HKD\"}"));

        this.mockMvc.perform(get("/accounts/88888888/balance"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"acctNumber\":\"88888888\",\"balance\":1005000.0,\"currency\":\"HKD\"}"));
    }

    @Test
    void transfer_but_insufficient_fund_test() throws Exception {
        TransferBalanceRequestDto postRequest = new TransferBalanceRequestDto();
        postRequest.fromAcctNumber = "12345678";
        postRequest.toAcctNumber = "88888888";
        postRequest.amount = 1000001.0;

        this.mockMvc.perform(post("/accounts/transfer")
                .content(asJsonString(postRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Account: 12345678 has insufficient fund"));

        this.mockMvc.perform(get("/accounts/12345678/balance"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"acctNumber\":\"12345678\",\"balance\":1000000.0,\"currency\":\"HKD\"}"));

        this.mockMvc.perform(get("/accounts/88888888/balance"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"acctNumber\":\"88888888\",\"balance\":1000000.0,\"currency\":\"HKD\"}"));
    }

    private String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

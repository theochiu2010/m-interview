package com.acmebank.accountmanager.service;

import com.acmebank.accountmanager.dao.Account;
import com.acmebank.accountmanager.repository.AccountRepository;
import com.acmebank.accountmanager.service.dto.AccountBalanceDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {
    @InjectMocks
    AccountServiceImpl service;

    @Mock
    AccountRepository accountRepository;

    @Test
    void able_to_get_account_balance_test() throws Exception {
        String testAccountNumber = "12345678";
        Double testAccountBalance = 100.5;
        Account testAccount = new Account(testAccountNumber, testAccountBalance);

        when(accountRepository.getAccountByAcctNumber(testAccountNumber))
                .thenReturn(testAccount);

        AccountBalanceDto balance = service.getAccountBalance(testAccountNumber);
        assertThat(balance.getBalance()).isEqualTo(testAccountBalance);

        verify(accountRepository).getAccountByAcctNumber(testAccountNumber);
    }

    @Test
    void get_account_balance_but_account_not_exist_test() {
        String testAccountNumber = "12345678";

        assertThatThrownBy(() -> {
            service.getAccountBalance(testAccountNumber);
        }).isInstanceOf(Exception.class)
        .hasMessage("No account with account number: " + testAccountNumber + " exist");

        verify(accountRepository).getAccountByAcctNumber(testAccountNumber);
    }

    @Test
    void transfer_account_balance_test() throws Exception {
        String testTransferFromAccountNumber = "12345678";
        Double testTransferFromAccountBalance = 100.5;
        Account testTransferFromAccount = new Account(testTransferFromAccountNumber, testTransferFromAccountBalance);

        String testTransferToAccountNumber = "88888888";
        Double testTransferToAccountBalance = 5.0;
        Account testTransferToAccount = new Account(testTransferToAccountNumber, testTransferToAccountBalance);

        when(accountRepository.getAccountByAcctNumber(testTransferFromAccountNumber))
                .thenReturn(testTransferFromAccount);

        when(accountRepository.getAccountByAcctNumber(testTransferToAccountNumber))
                .thenReturn(testTransferToAccount);

        service.transferAccountBalance(testTransferFromAccountNumber, testTransferToAccountNumber, 50.5);

        assertThat(testTransferFromAccount.getBalance()).isEqualTo(50.0);
        assertThat(testTransferToAccount.getBalance()).isEqualTo(55.5);

        verify(accountRepository).getAccountByAcctNumber(testTransferFromAccountNumber);
        verify(accountRepository).getAccountByAcctNumber(testTransferToAccountNumber);
        verify(accountRepository).save(testTransferFromAccount);
        verify(accountRepository).save(testTransferToAccount);
    }

    @Test
    void transfer_account_balance_insufficient_fund_test() {
        String testTransferFromAccountNumber = "12345678";
        Double testTransferFromAccountBalance = 100.5;
        Account testTransferFromAccount = new Account(testTransferFromAccountNumber, testTransferFromAccountBalance);

        String testTransferToAccountNumber = "88888888";
        Double testTransferToAccountBalance = 5.0;
        Account testTransferToAccount = new Account(testTransferToAccountNumber, testTransferToAccountBalance);

        when(accountRepository.getAccountByAcctNumber(testTransferFromAccountNumber))
                .thenReturn(testTransferFromAccount);

        when(accountRepository.getAccountByAcctNumber(testTransferToAccountNumber))
                .thenReturn(testTransferToAccount);

        assertThatThrownBy(() -> {
            service.transferAccountBalance(testTransferFromAccountNumber, testTransferToAccountNumber, 101.0);
        }).isInstanceOf(Exception.class)
        .hasMessage("Account: " + testTransferFromAccountNumber + " has insufficient fund");

        assertThat(testTransferFromAccount.getBalance()).isEqualTo(100.5);
        assertThat(testTransferToAccount.getBalance()).isEqualTo(5.0);

        verify(accountRepository).getAccountByAcctNumber(testTransferFromAccountNumber);
        verify(accountRepository).getAccountByAcctNumber(testTransferToAccountNumber);
        verifyNoMoreInteractions(accountRepository);
    }
}

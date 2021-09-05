package com.acmebank.accountmanager.service;

import com.acmebank.accountmanager.service.dto.AccountBalanceDto;

public interface AccountService {
    AccountBalanceDto getAccountBalance(String acctNumber) throws Exception;
    void transferAccountBalance(String fromAcctNumber, String toAcctNumber, Double amount) throws Exception;
}

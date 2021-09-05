package com.acmebank.accountmanager.controller;

import com.acmebank.accountmanager.controller.dto.TransferBalanceRequestDto;
import com.acmebank.accountmanager.service.AccountService;
import com.acmebank.accountmanager.service.dto.AccountBalanceDto;
import com.acmebank.accountmanager.service.dto.AccountTransferDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping(path = "/accounts/{acctNumber}/balance", produces = "application/json")
    public AccountBalanceDto getAccountBalance(@PathVariable String acctNumber) {
        try {
            AccountBalanceDto balance = accountService.getAccountBalance(acctNumber);

            return balance;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PostMapping(path = "/accounts/transfer", produces = "application/json")
    public AccountTransferDto transferBalance(@RequestBody TransferBalanceRequestDto requestDto) {
        try {
            accountService.transferAccountBalance(requestDto.fromAcctNumber, requestDto.toAcctNumber, requestDto.amount);

            return new AccountTransferDto("The transfer was successful");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}

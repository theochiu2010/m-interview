package com.acmebank.accountmanager.service;

import com.acmebank.accountmanager.dao.Account;
import com.acmebank.accountmanager.repository.AccountRepository;
import com.acmebank.accountmanager.service.dto.AccountBalanceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public AccountBalanceDto getAccountBalance(String acctNumber) throws Exception {
        Account account = accountRepository.getAccountByAcctNumber(acctNumber);

        validateIfAccountExist(account, acctNumber);

        Double balance = account.getBalance();

        return new AccountBalanceDto(acctNumber, balance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transferAccountBalance(String fromAcctNumber, String toAcctNumber, Double amount) throws Exception {
        Account transferFromAccount = accountRepository.getAccountByAcctNumber(fromAcctNumber);
        Account transferToAccount = accountRepository.getAccountByAcctNumber(toAcctNumber);

        validateIfAccountExist(transferFromAccount, fromAcctNumber);
        validateIfAccountExist(transferToAccount, toAcctNumber);

        transferFromAccount.transfer(transferToAccount, amount);

        accountRepository.save(transferFromAccount);
        accountRepository.save(transferToAccount);
    }

    private void validateIfAccountExist(Account account, String acctNumber) throws Exception {
        if (account == null) {
            throw new Exception("No account with account number: " + acctNumber + " exist");
        }
    }
}

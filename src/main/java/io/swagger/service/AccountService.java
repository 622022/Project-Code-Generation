package io.swagger.service;

import io.swagger.dao.AccountRepository;
import io.swagger.model.AccountObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<AccountObject> getAllAccounts() {
        List<AccountObject> accounts = new ArrayList<>();

        accountRepository.findAll().forEach( accountObject -> {
            accounts.add(accountObject); // add all accounts to list
        });
        return accounts;
    }

    public AccountObject getSpecificAccount(String iBan) {
        AccountObject specificAccount = accountRepository.findById(iBan).get(); // get specific account
        return specificAccount;
    }

    public void deleteAccount(String iBan)
    {
        accountRepository.deleteById(iBan);
    }

    public AccountObject editAccount(String iBan, AccountObject updatedAccountObject) {
        accountRepository.save(updatedAccountObject); // update existing account

        return accountRepository.findById(iBan).get(); // return updated account
    }

}

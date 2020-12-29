package io.swagger.service;

import io.swagger.dao.AccountRepository;
import io.swagger.filter.Filter;
import io.swagger.model.Account;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class AccountService {
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    private static final Logger logger = Logger.getLogger(AccountService.class.getName());


    public Iterable<Account> getAllAccounts(Filter filter) {
        Iterable<Account> accounts = new ArrayList<>();
        try {
            accounts= fillResponse(filter);
        } catch (Exception e) {
            logger.warning("Failed to get accounts " + e.getMessage());
            e.getMessage();
        }
        return accounts;
    }


    public Account getSpecificAccount(String iBan) {
        if (iBan.equals(""))
        {
            logger.warning("Invalid IBAN provided.");
            throw new IllegalArgumentException("Invalid IBAN provided.");
        }

        Account account = accountRepository.findById(iBan).get();
        if (account == null)
        {
            logger.warning("Account "+ iBan + " does not exist.");
            throw new IllegalArgumentException("Account "+ iBan + " does not exist.");
        }
        return account;
    }

    public void deleteAccount(String iBan) {
        if (accountRepository.existsById(iBan))
        {
            accountRepository.deleteById(iBan);
        } else {
            logger.warning("Failed to delete account:  " + iBan + " does not exist.");
            throw new IllegalArgumentException("Failed to delete account: " + iBan + " does not exist.");
        }
    }

    public Account editAccount(String iBan, Account updatedAccount) {
        if (iBan.equals("") || updatedAccount == null)
        {
            logger.warning("Invalid IBAN or account provided.");
            throw new IllegalArgumentException("Invalid IBAN or account provided.");
        }
        accountRepository.save(updatedAccount);
        return accountRepository.findById(iBan).get();
    }

    private Iterable<Account> fillResponse(Filter filter) {
        Iterable<Account> accounts = new ArrayList<>();
        if (filter.accountOwnerId != null) {
            accounts = accountRepository.getAccountsByOwnerId(filter.accountOwnerId);
        }
        if (filter.status != null) {
            accounts = accountRepository.getAccountsByStatus(filter.status);
        }
        if (filter.type != null) {
            accounts = accountRepository.getAccountsByType(filter.type);
        }
        if (filter.limit != null) {
            accounts = accountRepository.findAll();
            List<Account> result = new ArrayList<Account>();
            accounts.forEach(result::add);
            result = result.subList(0, filter.limit);
            accounts = result;
        }
        if (filter.offset != null) {
            accounts = accountRepository.findAll();
            List<Account> result = new ArrayList<Account>();
            accounts.forEach(result::add);
            result = result.subList(filter.offset, result.size());
            accounts = result;
        }
        if (accounts == null) {
            accounts = accountRepository.findAll();
        }
        return accounts;
    }

}
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
            accounts = fillResponse(filter);
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
        List<Account> result = new ArrayList<Account>();
        Iterable<Account> accounts = new ArrayList<Account>();
        if (filter.accountOwnerId != null) {
            accounts = accountRepository.getAccountObjectByOwnerId(filter.accountOwnerId);
            accounts.forEach(result::add);
        }
        if (filter.status != null) {
            if (result.size() == 0) {
                accounts = accountRepository.getAccountObjectByStatus(filter.status);
                accounts.forEach(result::add);
            } else {
                List<Account> temp = new ArrayList<Account>();
                accounts.forEach(account -> { if (account.getStatus()== filter.status )  temp.add(account) ; } );
                temp.forEach(result::add);
            }
        }
        if (filter.type != null) {
            if (result.size() == 0){
                accounts = accountRepository.getAccountObjectByType(filter.type);
                accounts.forEach(result::add);
            }
            else{
                List<Account> temp = new ArrayList<Account>();
                accounts.forEach(account -> { if (account.getType()== filter.type )  temp.add(account) ; } );
                temp.forEach(result::add);
            }
        }
        if (filter.limit != null) {
            accounts = accountRepository.GetAllLimit(filter.limit);
            accounts.forEach(result::add);
            result = result.subList(0, filter.limit);
        }
        if (filter.offset != null) {
            accounts = accountRepository.findAll();
            accounts.forEach(result::add);
            result = result.subList(filter.offset, result.size());
            accounts = result;
        }
        return result;
    }

}
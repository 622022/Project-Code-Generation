package io.swagger.service;

import io.swagger.dao.AccountRepository;
import io.swagger.filter.Filter;
import io.swagger.model.AccountObject;
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


    public Iterable<AccountObject> getAllAccounts(Filter filter) {
        Iterable<AccountObject> accounts = new ArrayList<>();
        try {
            accounts= fillResponse(filter);
        } catch (Exception e) {
            logger.warning("Failed to get accounts " + e.getMessage());
            e.getMessage();
        }
        return accounts;
    }


    public AccountObject getSpecificAccount(String iBan) {
        AccountObject specificAccount;
        try {
            specificAccount = accountRepository.findById(iBan).get();
            return specificAccount;
        } catch (Exception e) {
            logger.warning("Failed to get accounts " + e.getMessage());
        }
        return new AccountObject();
    }

    public void deleteAccount(String iBan) {
        try {
            accountRepository.deleteById(iBan);
        } catch (Exception e) {
            logger.warning("Failed to delete account " + e.getMessage());
        }
    }

    public AccountObject editAccount(String iBan, AccountObject updatedAccountObject) {
        try {
            accountRepository.save(updatedAccountObject);
            return accountRepository.findById(iBan).get();
        } catch (Exception e) {
            logger.warning("Failed to edit account " + e.getMessage());
        }
        return new AccountObject();
    }

    private Iterable<AccountObject> fillResponse(Filter filter) {
        Iterable<AccountObject> accounts = new ArrayList<>();
        if (filter.accountOwnerId != null) {
            accounts = accountRepository.getAccountObjectByOwnerId(filter.accountOwnerId);
        }
        if (filter.status != null) {
            accounts = accountRepository.getAccountObjectByStatus(filter.status);
        }
        if (filter.type != null) {
            accounts = accountRepository.getAccountObjectByType(filter.type);
        }
        if (filter.limit != null) {
            accounts = accountRepository.findAll();
            List<AccountObject> result = new ArrayList<AccountObject>();
            accounts.forEach(result::add);
            result = result.subList(0, filter.limit);
            accounts = result;
        }
        if (filter.offset != null) {
            accounts = accountRepository.findAll();
            List<AccountObject> result = new ArrayList<AccountObject>();
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
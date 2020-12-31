package io.swagger.service;

import io.swagger.dao.AccountRepository;
import io.swagger.filter.Filter;
import io.swagger.model.Account;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
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
        if (iBan.equals("")) {
            logger.warning("Invalid IBAN provided.");
            throw new IllegalArgumentException("Invalid IBAN provided.");
        }

        Account account = accountRepository.findById(iBan).get();
        if (account == null) {
            logger.warning("Account " + iBan + " does not exist.");
            throw new IllegalArgumentException("Account " + iBan + " does not exist.");
        }
        return account;
    }

    public void deleteAccount(String iBan) {
        if (accountRepository.existsById(iBan)) {
            accountRepository.deleteById(iBan);
        } else {
            logger.warning("Failed to delete account:  " + iBan + " does not exist.");
            throw new IllegalArgumentException("Failed to delete account: " + iBan + " does not exist.");
        }
    }

    public Account editAccount(String iBan, Account updatedAccount) {
        if (iBan.equals("") || updatedAccount == null) {
            logger.warning("Invalid IBAN or account provided.");
            throw new IllegalArgumentException("Invalid IBAN or account provided.");
        }
        accountRepository.save(updatedAccount);
        return accountRepository.findById(iBan).get();
    }

    private Iterable<Account> fillResponse(Filter filter) {
        List<Account> result = new ArrayList<>();
        List<Account> tempResult = new ArrayList<>();
        Iterator<Account> tempIterator ;
        if (filter.accountOwnerId != null) {
            accountRepository.getAccountsByOwnerId(filter.accountOwnerId).forEach(tempResult::add);
        }
        if (filter.type != null) {
            if (tempResult.isEmpty()) {
                accountRepository.getAccountsByType(filter.type).forEach(tempResult::add);
            } else {
                tempIterator = tempResult.iterator();
                while (tempIterator.hasNext()){
                    Account a = tempIterator.next();
                    if (a.getType()!=filter.type){
                        tempIterator.remove();
                    }
                }
            }
        }
        if (filter.status != null) {
            if (tempResult.isEmpty()) {
                accountRepository.getAccountsByStatus(filter.status).forEach(tempResult::add);
            } else {
                tempResult.forEach(account -> {
                    if (account.getStatus() != filter.status) {
                        tempResult.remove(account);
                    }
                });
            }
        }
        tempResult.forEach(result::add);
        if (filter.offset != 0) {
            if (result.isEmpty()) {
                accountRepository.GetAllLimit(filter.limit+filter.offset).forEach(result::add);
                result = result.subList(filter.offset,result.size());
            } else {
                result = result.subList(filter.offset, result.size());
            }
        }
        if (result.isEmpty()) {
            accountRepository.GetAllLimit(filter.limit).forEach(result::add);
        } else {
            result = result.subList(0, filter.limit);
        }
        return result;
    }

}
package io.swagger.service;

import io.swagger.dao.AccountRepository;
import io.swagger.utils.Filter;
import io.swagger.model.content.Account;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }



    public Iterable<Account> getAllAccounts(Filter filter) throws Exception {
        Iterable<Account> accounts ;
        accounts = fillResponse(filter);
        return accounts;
    }


    public Account getSpecificAccount(String iBan) {
        if (iBan.equals("")) {
            throw new IllegalArgumentException("Invalid IBAN provided.");
        }

        Account account = accountRepository.findById(iBan).get();
        if (account == null) {
            throw new IllegalArgumentException("Account " + iBan + " does not exist.");
        }
        return account;
    }

    public void deleteAccount(String iBan) {
        if (accountRepository.existsById(iBan)) {
            accountRepository.deleteById(iBan);
        } else {
            throw new IllegalArgumentException("Failed to delete account: " + iBan + " does not exist.");
        }
    }

    public Account editAccount(String iBan, Account updatedAccount) {
        if (iBan.equals("") || updatedAccount == null) {
            throw new IllegalArgumentException("Invalid IBAN or account provided.");
        }
        accountRepository.save(updatedAccount);
        return accountRepository.findById(iBan).get();
    }

    private Iterable<Account> fillResponse(Filter filter) {
        List<Account> result = new ArrayList<>();
        if (filter.onlyLimit()) {
            accountRepository.GetAllLimit(filter.limit, filter.offset).forEach(result::add);
            return result;
        } else if (filter.onlyAccountOwnerId()) {
            accountRepository.getAccountsByOwnerId(filter.accountOwnerId, filter.limit, filter.offset).forEach(result::add);
            return result;
        } else if (filter.onlyStatus()) {
            accountRepository.getAccountsByStatus(filter.getStatus(), filter.limit, filter.offset).forEach(result::add);
            return result;
        } else if (filter.onlyType()) {
            accountRepository.getAccountsByType(1, filter.limit, filter.offset).forEach(result::add);
            return result;
        } else if (filter.ownerIdWStatus()) {
            accountRepository.getAccountByOwnerIdAndStatusCustom(filter.getStatus(), filter.accountOwnerId, filter.limit, filter.offset).forEach(result::add);
            return result;
        } else if (filter.ownerIdWType()) {
            accountRepository.getAccountByOwnerIdAndTypeCustom(filter.getType(), filter.accountOwnerId, filter.limit, filter.offset).forEach(result::add);
            return result;
        } else if (filter.statusWType()) {
            accountRepository.getAccountByStatusAndTypeCustom(filter.getStatus(), filter.getType(), filter.limit, filter.offset).forEach(result::add);
            return result;
        } else if (filter.ownerIdWStatusWType()) {
            accountRepository.getAccountByOwnerIdAndStatusAndTypeCustom(filter.accountOwnerId, filter.getStatus(), filter.getType(), filter.limit, filter.offset).forEach(result::add);
            return result;
        }
        return result;
    }

}
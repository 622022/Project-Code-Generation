package io.swagger.service;

import io.swagger.dao.AccountRepository;
import io.swagger.dao.UserRepository;
import io.swagger.model.content.Account;
import io.swagger.model.content.Role;
import io.swagger.model.content.User;
import io.swagger.utils.Filter;
import io.swagger.utils.JwtTokenUtil;
import io.swagger.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private JwtTokenUtil jwtUtil ;
    private Utils utils = new Utils();

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.jwtUtil = new JwtTokenUtil(userRepository);
    }


    public Iterable<Account> getAllAccounts(Filter filter) throws Exception {
        Iterable<Account> accounts;
        accounts = fillResponse(filter);
        return accounts;
    }


    public Account getSpecificAccount(String iBan, String token) {
        if (!accountRepository.existsById(iBan)) {
            throw new IllegalArgumentException("Invalid IBAN provided. Account does not exist.");
        }
        User userPerformingAction = jwtUtil.getUserFromToken(token);
        Role userRole = jwtUtil.getRoleFromToken(token);
        Account account = accountRepository.findById(iBan).get();
        if (userRole == Role.EMPLOYEE){
            return account;
        }
        else if (userRole == Role.CUSTOMER){
            if (utils.authorizeAccount(account,userPerformingAction)){
                return account;
            }
            else {
                throw new SecurityException("Can not access accounts of the provided userid");
            }
        }
        return null;
    }

    public void deleteAccount(String iBan) {
        if (accountRepository.existsById(iBan)) {
            accountRepository.deleteById(iBan);
        } else {
            throw new IllegalArgumentException("Failed to delete account: " + iBan + " does not exist.");
        }
    }

    public Account editAccount(String iBan, Account updatedAccount) {
        if (!accountRepository.existsById(iBan) || updatedAccount == null) {
            throw new IllegalArgumentException("Invalid IBAN or account provided.");
        }
        Account account = accountRepository.getAccountByIBAN(iBan);
        if ( !updatedAccount.getIBAN().equals(iBan) ){
            throw new IllegalArgumentException("You can not update the account iban");
            }
        else if (!account.getOwnerId().equals(updatedAccount.getOwnerId()) ){
            throw new IllegalArgumentException("You can not update the account ownerId");
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
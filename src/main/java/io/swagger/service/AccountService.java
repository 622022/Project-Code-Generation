package io.swagger.service;

import io.swagger.dao.AccountRepository;
import io.swagger.filter.Filter;
import io.swagger.model.AccountObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    private Iterable<AccountObject> response;
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Iterable<AccountObject> getAllAccounts(Filter filter) {

        fillResponse(filter);
        return this.response;
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
    private void fillResponse(Filter filter){
        if (filter.accountOwnerId!=null)
        {
            this.response= accountRepository.getAccountObjectByOwnerId(filter.accountOwnerId) ;// add all accounts to list
        }
        if (filter.type!=null)
        {
            this.response= accountRepository.getAccountObjectByType(filter.type) ;
        }
        if (filter.status!=null){
            this.response= accountRepository.getAccountObjectByStatus(filter.status) ;
        }
        if (filter.limit!=null){
            this.response=accountRepository.findAll();
            List<AccountObject> result = new ArrayList<AccountObject>();
            this.response.forEach(result::add);
           result= result.subList(0,filter.limit);
            this.response=result;
        }
        if (filter.offset!=null){
            this.response=accountRepository.findAll();
            List<AccountObject> result = new ArrayList<AccountObject>();
            this.response.forEach(result::add);
            result= result.subList(filter.offset,result.size());
            this.response=result;
        }
        if (this.response==null)
        {
            this.response= accountRepository.findAll();
        }
    }

}
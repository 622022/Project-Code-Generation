package io.swagger.service;

import io.swagger.dao.AccountRepository;
import io.swagger.model.AccountObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService implements IFilter{
    private AccountRepository accountRepository;
    private List<AccountObject> response;
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<AccountObject> getAllAccounts() {

        List<AccountObject> accounts = new ArrayList<>();
        accountRepository.findAll().forEach( accountObject -> {
            accounts.add(accountObject); // add all accounts to list
        });
         this .response= accounts;
        return response;
    }
    //overload method.
    public List<AccountObject> getAllAccounts(Integer limit) {
        List<AccountObject> accounts = new ArrayList<>();
        accountRepository.findAll().forEach( accountObject -> {
            accounts.add(accountObject) ; // add all accounts to list
        });
        if (limit>accounts.size())
        {
           return getAllAccounts();
        }
        List<AccountObject>  limitedAccounts= accounts.subList(0,limit);
        return limitedAccounts;
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

    @Override
    public void filterOffset(Integer offset) {
         this.response= this.response.subList(offset,response.size());
    }

    @Override
    public void filterLimit(Integer limit) {
         this.response=this.response.subList(0,limit);
    }

    @Override
    public void filterUser(Integer id) {
        this.response= this.response.stream()
                .filter(p -> p.getOwnerId() == id).collect(Collectors.toList());
    }

    @Override
    public void filterType(String type) {
        this.response= this.response.stream()
                .filter(p -> p.getType().toString() == type).collect(Collectors.toList());
    }

    @Override
    public void filterStatus(String status) {
        this.response= this.response.stream()
                .filter(p -> p.getStatus().toString() == status).collect(Collectors.toList());
    }

    @Override
    public void filterReceiver(String receiverName) {
       //doesn't have implementation here
    }

    @Override
    public void filterIBAN(String IBAN) {
        this.response= this.response.stream()
                .filter(p -> p.getIBAN() == IBAN).collect(Collectors.toList());
    }
}
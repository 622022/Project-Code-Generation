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
    private Iterable<AccountObject> response;
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    private static final Logger LOGGER = Logger.getLogger(AccountService.class.getName());



    public Iterable<AccountObject> getAllAccounts(Filter filter) {
        try{
            fillResponse(filter);
            return this.response;
        }
        catch (Exception e)
        {
            LOGGER.warning("Failed to get accounts"+e.getMessage());
            e.getMessage();
        }
        return this.response;

    }


    public AccountObject getSpecificAccount(String iBan) {
        AccountObject specificAccount;
       try {
           specificAccount= accountRepository.findById(iBan).get(); // get specific account
            return  specificAccount;
       }
        catch(Exception error){
            LOGGER.warning("Failed to get accounts"+error.getMessage());
            System.out.println(error.getMessage());
        }
        return new AccountObject();
    }

    public void deleteAccount(String iBan)
    {
        try {
            accountRepository.deleteById(iBan);
        } catch (Exception e) {
            LOGGER.warning("Failed to delete account"+e.getMessage());
            e.printStackTrace();
        }

    }

    public AccountObject editAccount(String iBan, AccountObject updatedAccountObject) {
        try{
            accountRepository.save(updatedAccountObject); // update existing account

            return accountRepository.findById(iBan).get(); // return updated account
        }catch (Exception e)
        {
            LOGGER.warning("Failed to edit account"+e.getMessage());
            System.out.println(e.getMessage());
        }
        return new AccountObject();

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
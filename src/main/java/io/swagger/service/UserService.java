package io.swagger.service;

import io.swagger.dao.AccountRepository;
import io.swagger.dao.UserRepository;
import io.swagger.filter.Filter;
import io.swagger.model.AccountObject;
import io.swagger.model.Body;
import io.swagger.model.InlineResponse200;
import io.swagger.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService{

    private  AccountRepository accountRepository;
    private UserRepository userRepository;
    private List response;

    public UserService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public List<InlineResponse200> getAllUsers(Filter filter) {
        List<InlineResponse200> userIdList = new ArrayList<>();

        userRepository.findAll().forEach( user -> {
            InlineResponse200 getUsersResponse = new InlineResponse200(); // create get users response
            getUsersResponse.userId(user.getUserId());

            userIdList.add(getUsersResponse);
        });
         this.response = userIdList;
        return this.response;
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(Integer userId) {
      userRepository.deleteById(userId);
    }

    public User editUser(Integer userId,User updatedUser) {
        userRepository.save(updatedUser); // update existing user
        return userRepository.findById(userId).get();
    }

    public List<AccountObject> getAccountsByUserId(Integer userId) {
        List<AccountObject> accountList = new ArrayList<>();

        accountRepository.findAll().forEach( accountObject -> {
            if (userId == accountObject.getOwnerId()) {
                accountList.add(accountObject);
            }
        });

        return accountList;
    }

    public AccountObject createAccount(int userId, Body jsonInput) {
        AccountObject.TypeEnum accountType = AccountObject.TypeEnum.fromValue(jsonInput.getAccountType());
        AccountObject newAccount = new AccountObject(userId, accountType);
        accountRepository.save(newAccount);

        return newAccount;
    }
    private void filterResponse(Filter filter){
        if (filter.limit!=null)
        this.response.subList(0, filter.limit);
        if (filter.offset!=null)
        this.response.subList(filter.offset, this.response.size());
    }

}

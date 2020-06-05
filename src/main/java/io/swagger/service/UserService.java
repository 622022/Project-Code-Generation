package io.swagger.service;

import io.swagger.dao.AccountRepository;
import io.swagger.dao.UserRepository;
import io.swagger.model.AccountObject;
import io.swagger.model.Body;
import io.swagger.model.InlineResponse200;
import io.swagger.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements IFilter{

    private  AccountRepository accountRepository;
    private UserRepository userRepository;

    public UserService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public List<InlineResponse200> getAllUsers() {
        List<InlineResponse200> userIdList = new ArrayList<>();

        userRepository.findAll().forEach( user -> {
            InlineResponse200 getUsersResponse = new InlineResponse200(); // create get users response
            getUsersResponse.userId(user.getUserId());

            userIdList.add(getUsersResponse);
        });
        return userIdList;
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(Integer id) {
      userRepository.deleteById(id);
    }

    public User editUser(User updatedUser) {
        userRepository.save(updatedUser); // update existing user
        return updatedUser;
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
        AccountObject newAccount = new AccountObject();
        newAccount.setIBAN("NL02ABNA0123456789" + userId * 3);
        newAccount.setOwnerId(userId);
        newAccount.setType(AccountObject.TypeEnum.fromValue(jsonInput.getAccountType()));
        accountRepository.save(newAccount);

        return newAccount;
    }

    @Override
    public void filterOffset(Integer offset) {
    }

    @Override
    public void filterLimit(Integer limit) {
    }

    @Override
    public void filterUser(Integer id) {

    }

    @Override
    public void filterType(String type) {
    }

    @Override
    public void filterStatus(String status) {
    }

    @Override
    public void filterReceiver(String receiverName) {
    }

    @Override
    public void filterIBAN(String IBAN) {
    }
}

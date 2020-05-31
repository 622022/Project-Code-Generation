package io.swagger.service;

import io.swagger.dao.AccountRepository;
import io.swagger.dao.UserRepository;
import io.swagger.model.AccountObject;
import io.swagger.model.InlineResponse200;
import io.swagger.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

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

}

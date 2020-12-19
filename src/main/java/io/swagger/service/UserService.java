package io.swagger.service;

import io.swagger.dao.AccountRepository;
import io.swagger.dao.UserRepository;
import io.swagger.filter.Filter;
import io.swagger.model.Account;
import io.swagger.model.Body;
import io.swagger.model.User;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class UserService {

    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private static final Logger logger = Logger.getLogger(UserService.class.getName());


    public UserService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public List<InlineResponse200> getAllUsers(Filter filter) {
        List<InlineResponse200> userIdList = new ArrayList<>();

        try {
                userRepository.findAll().forEach(user -> {
                InlineResponse200 getUsersResponse = new InlineResponse200(); // create get users response
                getUsersResponse.userId(user.getUserId());
                userIdList.add(getUsersResponse);
            });
            return userIdList;
        }
        catch (Exception e) {
            logger.warning("Can not get users" + e.getMessage());
            return null;
        }
    }

    public void createUser(User user) {
        try {
            userRepository.save(user);
        }
        catch (Exception e) {
            logger.warning("Could not create user" + e.getMessage());
        }
    }

    public void deleteUser(Integer userId) {
        try {
            userRepository.deleteById(userId);
        }
        catch (Exception e) {
            logger.warning("Could not delete user" + e.getMessage());
        }
    }

    public User editUser(Integer userId, User updatedUser) {

        try {
            updatedUser.setUserId(userId);
            User oldUser = userRepository.findById(userId).get();

            updatedUser.setRole(oldUser.getRole());
            userRepository.save(updatedUser); // update existing user

            return userRepository.findById(userId).get();
        }
        catch (Exception e) {
            logger.warning("Could not edit user" + e.getMessage());
            return userRepository.findById(userId).get();
        }
    }

    public List<Account> getAccountsByUserId(Integer userId) {
        List<Account> accountList = new ArrayList<>();

        try {
            accountRepository.findAll().forEach(accountObject -> {
                if (userId == accountObject.getOwnerId()) {
                    accountList.add(accountObject);
                }
            });
            return accountList;
        }
        catch (Exception e) {
            logger.warning("Could not get users" + e.getMessage());
            return accountList;
        }
    }

    public Account createAccount(int userId, Body jsonInput) {
        try {
            JSONObject jsonObj = new JSONObject(jsonInput);
            String type = jsonObj.getString("accountType");
            Account.TypeEnum accountType = null;

            if (type.equals(Account.TypeEnum.CHECKING.toString())) {
                accountType = Account.TypeEnum.CHECKING;
            } else if (type.equals(Account.TypeEnum.SAVING.toString())) {
                accountType = Account.TypeEnum.SAVING;
            }

            //AccountObject.TypeEnum accountType = AccountObject.TypeEnum.fromValue(jsonInput.getClass().getName());
            Account newAccount = new Account(userId, accountType);
            accountRepository.save(newAccount);

            return newAccount;
        }
        catch (Exception e) {
            logger.warning("Could not create user" + e.getMessage());
            return new Account();
        }
    }
}

package io.swagger.service;

import io.swagger.dao.AccountRepository;
import io.swagger.dao.UserRepository;
import io.swagger.filter.Filter;
import io.swagger.model.Account;
import io.swagger.model.UserCredentials;
import io.swagger.model.User;
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

    public List<UserCredentials> getAllUsers(Filter filter) {
        List<UserCredentials> userIdList = new ArrayList<>();

        try {
                userRepository.findAll().forEach(user -> {
                UserCredentials getUsersResponse = new UserCredentials(); // create get users response
                getUsersResponse.userId(user.getUserId().toString());
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
        if (user == null)
        {
            logger.warning("User can not be null");
            throw new IllegalArgumentException("User can not be null.");
        }
        userRepository.save(user);
    }

    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId))
        {
            logger.warning("User: " + userId + " does not exist");
            throw new IllegalArgumentException("User: " + userId + " does not exist");
        }
        userRepository.deleteById(userId);
    }

    public User editUser(Integer userId, User updatedUser) {
        User user = userRepository.findById(userId).get();
        if (user == null) {
            logger.warning("User: " + userId + " does not exist");
            throw new IllegalArgumentException("User: " + userId + " does not exist");
        }

        updatedUser.setUserId(userId);
        updatedUser.setRole(user.getRole());
        userRepository.save(updatedUser); // update existing user

        return userRepository.findById(userId).get();
    }

    public List<Account> getAccountsByUserId(Integer userId) {
        List<Account> accountList = new ArrayList<>();

        if (userId == null) {
            logger.warning("Invalid user provided.");
            throw new IllegalArgumentException("Invalid user provided.");
        }
        accountList = (List<Account>) accountRepository.getAccountsByOwnerId(userId);
/*
        accountRepository.findAll().forEach(accountObject -> {
            if (userId == accountObject.getOwnerId()) {
                accountList.add(accountObject);
            }
        });
*/
        return accountList;
    }

    public Account createAccount(int userId, String typeAccount) {
            Account.TypeEnum accountType = null;

            if (typeAccount.equals(Account.TypeEnum.CHECKING.toString()))
            {
                accountType = Account.TypeEnum.CHECKING;
            }
            else if (typeAccount.equals(Account.TypeEnum.SAVING.toString()))
            {
                accountType = Account.TypeEnum.SAVING;
            }
            else {
                logger.warning("Invalid account type");
                throw new IllegalArgumentException("Invalid account type");
            }

            if (!userRepository.existsById(userId))
            {
                logger.warning("User does not exist");
                throw new IllegalArgumentException("User does not exist");
            }

            Account newAccount = new Account(userId, accountType);
            accountRepository.save(newAccount);
            return newAccount;
    }
}

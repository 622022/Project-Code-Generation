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

    public Account createAccount(int userId, String requestType) {
            Account.TypeEnum accountType = null;

            if (requestType.equals(Account.TypeEnum.CHECKING.toString()))
            {
                accountType = Account.TypeEnum.CHECKING;
            }
            else if (requestType.equals(Account.TypeEnum.SAVING.toString()))
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

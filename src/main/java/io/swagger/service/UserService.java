package io.swagger.service;

import io.swagger.configuration.JwtTokenUtil;
import io.swagger.dao.AccountRepository;
import io.swagger.dao.UserRepository;
import io.swagger.model.content.Role;
import io.swagger.utils.Filter;
import io.swagger.model.content.Account;
import io.swagger.model.api.UserCredentials;
import io.swagger.model.content.User;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private AccountRepository accountRepository;
    private UserRepository userRepository;


    public UserService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Iterable<Integer> getAllUsers(Filter filter) throws Exception {
        return userRepository.getallUsersId(filter.limit, filter.offset);

    }

    public void createUser(User user) throws Exception {
        if (user == null) {
            throw new IllegalArgumentException("User can not be null.");
        }
        userRepository.save(user);
    }

    public void deleteUser(Integer userId) throws Exception {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User: " + userId + " does not exist");
        }
        userRepository.deleteById(userId);
    }

    public User editUser(Integer userId, User updatedUser) throws Exception {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User: " + userId + " does not exist");
        }

        if (updatedUser == null) {
            throw new IllegalArgumentException("Request body can not be null");
        }

        User user = userRepository.findById(userId).get();

        updatedUser.setUserId(userId);
        updatedUser.setRole(user.getRole());
        userRepository.save(updatedUser); // update existing user
        return userRepository.findById(userId).get();
    }

    public List<Account> getAccountsByUserId(String token, Integer userId) throws Exception {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("Invalid user ID provided.");
        }
        JwtTokenUtil j = new JwtTokenUtil(userRepository);
        User user = j.getUserFromToken(token);
        List<Account> accountList = new ArrayList<>();
        if (user.getRole() == Role.EMPLOYEE) {
            accountList = (List<Account>) accountRepository.getAccountsByOwnerId(userId);
        } else if (user.getRole() == Role.CUSTOMER) {
            userId = user.getUserId();
            accountList = (List<Account>) accountRepository.getAccountsByOwnerId(userId);
        }
        return accountList;
    }

    public Account createAccount(int userId, String typeAccount) {
        Account.TypeEnum accountType = null;
        accountType = Account.TypeEnum.valueOf(typeAccount.toUpperCase());
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User does not exist");
        }
        Account newAccount = new Account(userId, accountType);
        accountRepository.save(newAccount);
        return newAccount;
    }
}

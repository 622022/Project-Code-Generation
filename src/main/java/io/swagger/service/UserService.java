package io.swagger.service;

import io.swagger.dao.AccountRepository;
import io.swagger.dao.UserRepository;
import io.swagger.model.content.Account;
import io.swagger.model.content.Role;
import io.swagger.model.content.User;
import io.swagger.utils.Filter;
import io.swagger.utils.JwtTokenUtil;
import io.swagger.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private JwtTokenUtil jwtUtil ;
    private Utils utils = new Utils();

    public UserService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.jwtUtil = new JwtTokenUtil(userRepository);
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

    public User editUser(Integer userId, User updatedUser , String token) throws Exception {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User: " + userId + " does not exist");
        }
        if (updatedUser == null) {
            throw new IllegalArgumentException("Request body can not be null");
        }
        User userPerformingAction = jwtUtil.getUserFromToken(token);
        Role userRole = jwtUtil.getRoleFromToken(token);
        User editedUser = userRepository.findById(userId).get();
        updatedUser.setUserId(userId);
        if (userRole == Role.EMPLOYEE){
            userRepository.save(updatedUser);
        } else if (userRole == Role.CUSTOMER){
            if (utils.authorizeEdit(userPerformingAction,userId)){
                updatedUser.setRole(editedUser.getRole());
                userRepository.save(updatedUser);
            }else {
                throw new SecurityException("Can not access accounts of the provided userid");
            }
        }
        //we can simply return the created user from the previous lines but we rather retrieved from the repo because we wanted to make sure the new user is indeed saved
        return userRepository.findById(userId).get();
    }

    public List<Account> getAccountsByUserId(String token, Integer userId) throws Exception {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("Invalid user ID provided.");
        }

        User userPerformingAction = jwtUtil.getUserFromToken(token);
        Role userRole = jwtUtil.getRoleFromToken(token);
        List<Account> accountList = new ArrayList<>();
        if (userRole == Role.EMPLOYEE) {
            accountList = (List<Account>) accountRepository.getAccountsByOwnerId(userId);
        } else if (userRole == Role.CUSTOMER) {
           if (utils.authorizeEdit(userPerformingAction,userId)) {
               accountList = (List<Account>) accountRepository.getAccountsByOwnerId(userId);
           }
           else{
               throw new SecurityException("Can not access accounts of the provided userid");
           }
        }
        return accountList;
    }

    public Account createAccount(int userId, String typeAccount) {
        Account.TypeEnum accountType = null;
        accountType = Account.TypeEnum.valueOf(typeAccount.toUpperCase());

        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User does not exist");
        }

        if (accountType == null) {
            throw new IllegalArgumentException("Invalid account type provided.");
        }

        Account newAccount = new Account(userId, accountType);
        accountRepository.save(newAccount);
        return newAccount;
    }
}

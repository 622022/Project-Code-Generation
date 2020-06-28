package io.swagger.service;

import io.swagger.dao.AccountRepository;
import io.swagger.dao.UserRepository;
import io.swagger.filter.Filter;
import io.swagger.model.AccountObject;
import io.swagger.model.Body;
import io.swagger.model.InlineResponse200;
import io.swagger.model.User;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class UserService{

    private  AccountRepository accountRepository;
    private UserRepository userRepository;
    private List response;
    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());


    public UserService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public List<InlineResponse200> getAllUsers(Filter filter) {
        List<InlineResponse200> userIdList = new ArrayList<>();
        try{
            userRepository.findAll().forEach( user -> {
                InlineResponse200 getUsersResponse = new InlineResponse200(); // create get users response
                getUsersResponse.userId(user.getUserId());

                userIdList.add(getUsersResponse);
            });
            this.response = userIdList;
            return this.response;
        }catch (Exception e)
        {
            LOGGER.warning("Can not get users"+e.getMessage());
            System.out.println(e.getMessage());
        }
        return this.response;
    }

    public void createUser(User user) {
        try{
            userRepository.save(user);
        }catch (Exception e)
        {
            LOGGER.warning("Could not create user"+e.getMessage());
            System.out.println(e.getMessage());
        }

    }

    public void deleteUser(Integer userId) {
        try{
            userRepository.deleteById(userId);
        }catch (Exception e)
        {
            LOGGER.warning("Could not delete user"+e.getMessage());
            System.out.println(e.getMessage());
        }

    }

    public User editUser(Integer userId, User updatedUser) {
        try{
            updatedUser.setUserId(userId);
            User oldUser=userRepository.findById(userId).get();
            updatedUser.setRole(oldUser.getRole());
            userRepository.save(updatedUser); // update existing user
            return userRepository.findById(userId).get();
        }catch (Exception e)
        {
            LOGGER.warning("Could not edit user"+e.getMessage());
            System.out.println(e.getMessage());
        }
        return userRepository.findById(userId).get();
    }

    public List<AccountObject> getAccountsByUserId(Integer userId) {
        List<AccountObject> accountList = new ArrayList<>();
        try{
            accountRepository.findAll().forEach( accountObject -> {
                if (userId == accountObject.getOwnerId()) {
                    accountList.add(accountObject);
                }
            });

            return accountList;
        }catch (Exception e)
        {
            LOGGER.warning("Could not get users"+e.getMessage());
            System.out.println(e.getMessage());
        }
        return accountList;
    }

    public AccountObject createAccount(int userId, Body jsonInput) {
        try{
            JSONObject jsonObj = new JSONObject(jsonInput);
            String type = jsonObj.getString("accountType");
            AccountObject.TypeEnum accountType = null;
            if(type.equals(AccountObject.TypeEnum.CHECKING.toString()))
            {
                accountType=AccountObject.TypeEnum.CHECKING;
            }
            else if (type.equals(AccountObject.TypeEnum.SAVING.toString()))
            {
                accountType=AccountObject.TypeEnum.SAVING;
            }
            //AccountObject.TypeEnum accountType = AccountObject.TypeEnum.fromValue(jsonInput.getClass().getName());
            AccountObject newAccount = new AccountObject(userId, accountType);
            accountRepository.save(newAccount);

            return newAccount;
        }catch (Exception e)
        {
            LOGGER.warning("Could not create user"+e.getMessage());
            System.out.println(e.getMessage());
        }
        return new AccountObject();
    }
    private void filterResponse(Filter filter){
        if (filter.limit!=null)
        this.response.subList(0, filter.limit);
        if (filter.offset!=null)
        this.response.subList(filter.offset, this.response.size());
    }

}

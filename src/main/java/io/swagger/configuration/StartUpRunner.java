package io.swagger.configuration;

import io.swagger.dao.AccountRepository;
import io.swagger.dao.TransactionRepository;
import io.swagger.dao.UserRepository;
import io.swagger.model.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class StartUpRunner implements ApplicationRunner {

    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private TransactionRepository transactionRepository;

    public StartUpRunner(AccountRepository accountRepository, UserRepository userRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        List<User> userList = new ArrayList<>();
        List<Transaction> transactionList = new ArrayList<>();

        for (int i = 1; i < 6; i++) {
            User user = new User(i, "username_" + i, "password_" + i, "email_" + i);
            userList.add(user);

            Transaction transaction = new Transaction("NL12ING0123456789" + i, "NL02ABNA728391237"+ i, user.getUsername(),
                    500.00 * i, Role.Employee);
            transactionList.add(transaction);
        }
        userList.forEach( user -> userRepository.save(user) );
        transactionList.forEach( transaction -> transactionRepository.save(transaction) );

        userRepository
                .findAll()
                .forEach(user -> accountRepository.save(new AccountObject( 1000,
                        user.getUserId(), AccountObject.TypeEnum.SAVING, AccountObject.StatusEnum.ACTIVE, 500.00,
                        200, 600)
                ));
        userRepository
                .findAll()
                .forEach(user -> accountRepository.save(new AccountObject( 1000*2,
                        user.getUserId(), AccountObject.TypeEnum.CHECKING, AccountObject.StatusEnum.ACTIVE, 500.00*2,
                        200*2, 600*2)
                ));

        transactionRepository.findAll().forEach(System.out::println);



    }
}
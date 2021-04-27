package io.swagger.configuration;

import io.swagger.dao.AccountRepository;
import io.swagger.dao.TransactionRepository;
import io.swagger.dao.UserRepository;
import io.swagger.model.api.JwtUserDetails;
import io.swagger.model.content.Account;
import io.swagger.model.content.Role;
import io.swagger.model.content.Transaction;
import io.swagger.model.content.User;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.logging.Logger;

@Component
public class StartUpRunner implements ApplicationRunner {

    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private TransactionRepository transactionRepository;
    // assumes the current class is called MyLogger
    private final static Logger LOGGER = Logger.getLogger(StartUpRunner.class.getName());

    public StartUpRunner(AccountRepository accountRepository, UserRepository userRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        List<User> userList = new ArrayList<>();
        List<Transaction> transactionList = new ArrayList<>();
        try {
            for (int i = 1; i < 13; i++) {
                Role r = i > 6 ? Role.CUSTOMER : Role.EMPLOYEE;
                User user = new User("username_" + i, "password_" + i, "email_" + i, r);
                userList.add(user);
                Transaction transaction = new Transaction("NL12INHO0123456789" + i, "NL02INHO728391237" + i, user.getUsername(),
                        500.00 * i, r);
                transactionList.add(transaction);
            }

            userList.forEach(user -> userRepository.save(user));
            transactionList.forEach(transaction -> transactionRepository.save(transaction));

            accountRepository.save(new Account().createBank());

            userRepository
                    .findAll()
                    .forEach(user -> accountRepository.save(new Account(1000d,
                            user.getUserId(), Account.TypeEnum.SAVING, Account.StatusEnum.ACTIVE, 500.00,
                            4, 60d)
                    ));
            userRepository
                    .findAll()
                    .forEach(user -> accountRepository.save(new Account(1000 * 2d,
                            user.getUserId(), Account.TypeEnum.CHECKING, Account.StatusEnum.ACTIVE, 500.00 * 2,
                            200 * 2, 600 * 2d)
                    ));

            transactionRepository.findAll().forEach(System.out::println);
            userRepository.findAll().forEach(System.out::println);
            accountRepository.findAll().forEach(System.out::println);
        } catch (Exception e) {
            LOGGER.warning("" + e.getMessage());
            System.out.println(e.getMessage());
        }


    }
}
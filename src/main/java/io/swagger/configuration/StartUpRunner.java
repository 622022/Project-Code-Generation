package io.swagger.configuration;

import io.swagger.dao.AccountRepository;
import io.swagger.dao.ApiKeyRepository;
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
    private ApiKeyRepository apiKeyRepository;
    private TransactionRepository transactionRepository;

    public StartUpRunner(AccountRepository accountRepository, UserRepository userRepository, ApiKeyRepository apiKeyRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.apiKeyRepository = apiKeyRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        List<User> userList = new ArrayList<>();
        List<Transaction> transactionList = new ArrayList<>();

            User user = new User("username_" + i, "password_" + i, "email_" + i);
            userList.add(user);
        }
        userList.forEach( user -> userRepository.save(user) );
        transactionList.forEach( transaction -> transactionRepository.save(transaction) );

        userRepository
                .findAll()
                .forEach(user -> accountRepository.save(new AccountObject(1000,
                                user.getUserId(), AccountObject.TypeEnum.SAVING, AccountObject.StatusEnum.ACTIVE, 500.00,
                                200, 600)
                        ));
        userRepository
                .findAll()
                .forEach(user -> accountRepository.save(new AccountObject(1000*2,
                        user.getUserId(), AccountObject.TypeEnum.CHECKING, AccountObject.StatusEnum.ACTIVE, 500.00*2,
                        200*2, 600*2)
                ));

        transactionRepository.findAll().forEach(System.out::println);


        /*
    Generate random API Keys
     */
        for (int i = 0; i < 5; i++) {
            UUID uuid = UUID.randomUUID();
            apiKeyRepository.save(new ApiKey(uuid.toString()));
        }

//        apiKeyRepository.findAll().forEach(System.out::println);
//        apiKeyRepository.save(new ApiKey("44272285-2c48-4ee9-9c52-bff1b8bf2bbb"));
        apiKeyRepository.findAll().forEach(System.out::println);
//        apiKeyRepository.save(new InlineResponse2002("U200","User","44272285-2c48-4ee9-9c52-bff1b8bf2bbb"));
//        apiKeyRepository.findAll().forEach(System.out::println);
    }
}

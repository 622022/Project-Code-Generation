package io.swagger.configuration;

import io.swagger.dao.AccountRepository;
import io.swagger.dao.ApiKeyRepository;
import io.swagger.dao.UserRepository;
import io.swagger.model.AccountObject;
import io.swagger.model.ApiKey;
import io.swagger.model.InlineResponse2002;
import io.swagger.model.User;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class StartUpRunner implements ApplicationRunner {

    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private ApiKeyRepository apiKeyRepository;

    public StartUpRunner(AccountRepository accountRepository, UserRepository userRepository, ApiKeyRepository apiKeyRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        List<User> userList = new ArrayList<>();

        for (int i = 11; i < 16; i++) {
            User user = new User("username_" + i, "password_" + i, "email_" + i);
            userList.add(user);
        }
        userList.forEach( user -> userRepository.save(user) );
//        accountList.forEach( accountObject -> accountRepository.save(accountObject) );

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

        accountRepository.findAll().forEach(System.out::println);


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

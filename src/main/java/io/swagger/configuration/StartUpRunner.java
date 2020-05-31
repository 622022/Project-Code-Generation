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

import java.util.ArrayList;
import java.util.List;

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
        List<AccountObject> accountList = new ArrayList<>();

        for (int i = 10; i < 16; i++) {
            User user = new User(i, "username_" + i, "password_" + i, "email_" + i);
            userList.add(user);

            AccountObject account = new AccountObject("NL02ABNA0123456789___ " + i, i * 1000, i, AccountObject.TypeEnum.SAVING,
                    AccountObject.StatusEnum.ACTIVE, i * 500.00, i * 200, i * 600);
            accountList.add(account);
            accountList.add(account);
        }

        userList.forEach( user -> userRepository.save(user) );
        accountList.forEach( accountObject -> accountRepository.save(accountObject) );

        accountRepository.findAll().forEach(System.out::println);
//        userRepository.findAll().forEach(System.out::println);


        apiKeyRepository.save(new ApiKey("44272285-2c48-4ee9-9c52-bff1b8bf2bbb"));
        apiKeyRepository.findAll().forEach(System.out::println);
//        apiKeyRepository.save(new InlineResponse2002("U200","User","44272285-2c48-4ee9-9c52-bff1b8bf2bbb"));
//        apiKeyRepository.findAll().forEach(System.out::println);
    }
}

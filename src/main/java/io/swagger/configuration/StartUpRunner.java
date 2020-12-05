package io.swagger.configuration;

import io.swagger.dao.AccountRepository;
import io.swagger.dao.TransactionRepository;
import io.swagger.dao.UserRepository;
import io.swagger.model.*;
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
    private JwtUserDetails jwtUserDetails;
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
        try{
            for (int i = 1; i < 6; i++) {
                User user = new User( "username_" + i, "password_" + i, "email_" + i,Role.EMPLOYEE);
                userList.add(user);

                Transaction transaction = new Transaction("NL12ING0123456789" + i, "NL02ABNA728391237"+ i, user.getUsername(),
                        500.00 * i, Role.EMPLOYEE);
                transactionList.add(transaction);
            }
            //ANOTHER TESTING LOOP TO CHECK ROLES
            for (int x = 7; x < 10; x++) {
                User user1 = new User( "username_" + x, "password_" + x, "email_" + x,Role.CUSTOMER);
                userList.add(user1);
            }
            userList.forEach( user -> userRepository.save(user) );
            transactionList.forEach( transaction -> transactionRepository.save(transaction) );

            userRepository
                    .findAll()
                    .forEach(user -> accountRepository.save(new AccountObject( 1000d,
                            user.getUserId(), AccountObject.TypeEnum.SAVING, AccountObject.StatusEnum.ACTIVE, 500.00,
                            2, 6d)
                    ));
            userRepository
                    .findAll()
                    .forEach(user -> accountRepository.save(new AccountObject( 1000*2d,
                            user.getUserId(), AccountObject.TypeEnum.CHECKING, AccountObject.StatusEnum.ACTIVE, 500.00*2,
                            200*2, 600*2d)
                    ));
            userRepository
                    .findAll()
                    .forEach(user -> accountRepository.save(new AccountObject(9, AccountObject.TypeEnum.CHECKING)
                    ));

            transactionRepository.findAll().forEach(System.out::println);
            userRepository.findAll().forEach(System.out::println);
            accountRepository.findAll().forEach(System.out::println);
        }catch (Exception e)
        {
            LOGGER.warning(""+e.getMessage());
            System.out.println(e.getMessage());
        }


    }
}
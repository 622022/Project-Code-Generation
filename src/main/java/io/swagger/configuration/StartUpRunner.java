package io.swagger.configuration;

import io.swagger.dao.UserRepository;
import io.swagger.model.User;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class StartUpRunner implements ApplicationRunner {

    private UserRepository userRepository;

    public StartUpRunner(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        List<User> userList = new ArrayList<>();

        for (int i = 1; i < 6; i++) {
            User user = new User(i, "username_" + i, "password_" + i, "email_" + i);
            userList.add(user);
        }

        userList.forEach( user -> userRepository.save(user) );

        userRepository.findAll().forEach(System.out::println);
    }
}

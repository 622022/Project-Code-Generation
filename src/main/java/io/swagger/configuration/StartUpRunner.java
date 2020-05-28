package io.swagger.configuration;

import io.swagger.dao.UserRepository;
import io.swagger.model.User;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StartUpRunner implements ApplicationRunner {

    private UserRepository userRepository;

    public StartUpRunner(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {

        for (int i = 1; i < 6; i++) {
            User user = new User(i, "username_" + i, "password_" + i, "email_" + i);
            userRepository.save(user);
        }
        userRepository.findAll()
                .forEach(System.out::println);
    }
}

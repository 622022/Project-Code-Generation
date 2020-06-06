package io.swagger.service;

import io.swagger.dao.UserRepository;
import io.swagger.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public  JwtUserDetailsService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user1 = userRepository.findUserByUsername(username);

        if (user1.getUsername().equals(username))
        {
            return new org.springframework.security.core.userdetails.User(user1.getUsername(), user1.getPassword(),
                    new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }


    public UserDetails loadUserByUsername(String username, String password) {
        User user = userRepository.findUserByUsername(username);

        if (user.getUsername().equals(username) && user.getPassword().equals(password))
        {
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                    new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}

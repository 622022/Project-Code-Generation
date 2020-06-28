package io.swagger.service;

import io.swagger.dao.UserRepository;
import io.swagger.model.JwtUserDetails;
import io.swagger.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.logging.Logger;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public  JwtUserDetailsService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }
    private static final Logger LOGGER = Logger.getLogger(JwtUserDetailsService.class.getName());


    @Override
    public JwtUserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user1 = userRepository.findUserByUsername(username);

        if (user1.getUsername().equals(username))
        {
            return new JwtUserDetails(user1);
        } else {
            LOGGER.warning("User not found with username: " + username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }


    //to get a user and put it in for userdetailsservice after checking username and password and comparing it with the repository
    public JwtUserDetails loadUserByUsername(String username, String password) {
        User user = userRepository.findUserByUsername(username);

        if (user.getUsername().equals(username) && user.getPassword().equals(password))
        {
            return new JwtUserDetails(user);
        } else {
            LOGGER.warning("User not found with username: " + username);
            throw new UsernameNotFoundException("Username and password combination not found" + username);
        }
    }
}

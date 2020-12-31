package io.swagger.api;

import io.swagger.configuration.JwtTokenUtil;
import io.swagger.dao.UserRepository;
import io.swagger.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import io.swagger.service.JwtUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-05-21T18:10:30.703Z[GMT]")
@Controller
@RestController
@CrossOrigin
public class LoginApiController implements ILoginApi {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    private JwtUserDetails jwtUserDetails;

    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(LoginApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public LoginApiController(ObjectMapper objectMapper, HttpServletRequest request, UserRepository userRepository) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.userRepository = userRepository;
    }

    public ResponseEntity<UserCredentials> loginUser(@ApiParam(value = "") @Valid @RequestBody LoginDetails loginDetails
    ) {
        try {
            final JwtUserDetails userDetails = userDetailsService.loadUserByUsername(loginDetails.getUsername(), loginDetails.getPassword());
            final String token = jwtTokenUtil.generateToken(userDetails);

            UserCredentials userCredentials = new UserCredentials(userDetails.getUser().getUserId().toString(), "Bearer", token,userDetails.getUser().getRole() );
            return new ResponseEntity<UserCredentials>(userCredentials, HttpStatus.OK);
        }
        catch(IllegalArgumentException e) {
            return new ResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        catch(UsernameNotFoundException e) {
            return new ResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        catch(Exception e) {
            return new ResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //this is not being used
    // we should make sure if we need this method and how can we use it
    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}

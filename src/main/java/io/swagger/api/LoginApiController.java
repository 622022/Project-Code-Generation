package io.swagger.api;

import io.swagger.configuration.JwtTokenUtil;
import io.swagger.dao.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import io.swagger.model.api.JsonResponse;
import io.swagger.model.api.JwtUserDetails;
import io.swagger.model.api.LoginDetails;
import io.swagger.model.api.UserCredentials;
import io.swagger.service.JwtUserDetailsService;

import java.util.logging.Logger;
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

    private static final Logger logger = Logger.getLogger(LoginApiController.class.getName());

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public LoginApiController(ObjectMapper objectMapper, HttpServletRequest request, UserRepository userRepository) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.userRepository = userRepository;
    }

    public ResponseEntity<JsonResponse> loginUser(@ApiParam(value = "") @Valid @RequestBody LoginDetails loginDetails
    ) {
        try {
            final JwtUserDetails userDetails = userDetailsService.loadUserByUsername(loginDetails.getUsername(), loginDetails.getPassword());
            final String token = jwtTokenUtil.generateToken(userDetails);

            UserCredentials userCredentials = new UserCredentials(userDetails.getUser().getUserId().toString(), "Bearer", token);
            JsonResponse response = new JsonResponse(userCredentials , new JsonResponse.UserMessage("Handled", HttpStatus.OK, true));

            return new ResponseEntity<JsonResponse>(response, HttpStatus.OK);
        }
        catch(IllegalArgumentException e) {
            JsonResponse response = new JsonResponse(null, new JsonResponse.UserMessage(e.getMessage(), HttpStatus.BAD_REQUEST, false));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.BAD_REQUEST);
        }
        catch(UsernameNotFoundException e) {
            JsonResponse response = new JsonResponse(null, new JsonResponse.UserMessage(e.getMessage(), HttpStatus.BAD_REQUEST, false));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.BAD_REQUEST);
        }
        catch(Exception e) {
            logger.warning("LoginUser: " + e.getMessage());
            JsonResponse response = new JsonResponse(null, new JsonResponse.UserMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, false));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
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

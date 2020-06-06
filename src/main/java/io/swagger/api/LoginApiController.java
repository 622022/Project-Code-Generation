package io.swagger.api;

import io.swagger.configuration.JwtTokenUtil;
import io.swagger.dao.UserRepository;
import io.swagger.model.Body1;
import io.swagger.model.InlineResponse2002;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import io.swagger.model.JwtResponse;
import io.swagger.model.User;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-05-21T18:10:30.703Z[GMT]")
@Controller
@RestController
@CrossOrigin
public class LoginApiController implements LoginApi {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(LoginApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public LoginApiController(ObjectMapper objectMapper, HttpServletRequest request, UserRepository userRepository) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.userRepository=userRepository;
    }

    public ResponseEntity<InlineResponse2002> loginUser(@ApiParam(value = ""  )  @Valid @RequestBody Body1 body
)   {
        //authenticate(authenticationRequest.getUsername(),
        body.getPassword();
        User user =userRepository.findUserByUsername(body.getUsername());

        final UserDetails userDetails =
                userDetailsService.loadUserByUsername(body.getUsername(), body.getPassword());


        //JwtUserDetails userDetails = new JwtUserDetails();
        //userDetails.setUsername(authenticationRequest.getUsername());


        final String token = jwtTokenUtil.generateToken(userDetails);
//        final String finalToken = new JwtResponse(token);
        InlineResponse2002 response2002 = new InlineResponse2002(user.getUserId().toString(),"Bearer",token);
        return new ResponseEntity<InlineResponse2002>(response2002,HttpStatus.OK);
//        return ResponseEntity.ok(new JwtResponse(token));
    }

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

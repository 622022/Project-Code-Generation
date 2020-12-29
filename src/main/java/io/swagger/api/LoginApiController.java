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

    public ResponseEntity<InlineResponse200> loginUser(@ApiParam(value = "") @Valid @RequestBody Body1 body
    ) {
        User user = userRepository.findUserByUsername(body.getUsername());
        final JwtUserDetails userDetails = userDetailsService.loadUserByUsername(body.getUsername(), body.getPassword());
        final String token = jwtTokenUtil.generateToken(userDetails);
        InlineResponse200 response200 = new InlineResponse200(userDetails.getUser().getUserId().toString(), "Bearer", token, userDetails.getUser().getRole());
        return new ResponseEntity<InlineResponse200>(response200, HttpStatus.OK);
    }

}

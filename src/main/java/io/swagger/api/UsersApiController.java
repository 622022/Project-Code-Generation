package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import io.swagger.filter.Filter;
import io.swagger.model.*;
import io.swagger.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-05-21T18:10:30.703Z[GMT]")
@Controller
public class UsersApiController implements UsersApi {

    @Autowired
    private UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<AccountObject> createAccount(@ApiParam(value = "the userid of the user who owns these accounts",required=true) @PathVariable("userId") Integer userId
,@ApiParam(value = "The account to create."  )  @Valid @RequestBody Body body
)   {
        try{
            return new ResponseEntity<AccountObject>(userService.createAccount(userId, body), HttpStatus.OK);
        }catch (Exception e)
        {
            log.warn("Account creation failed");
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<AccountObject>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<InlineResponse2001> createUser(@ApiParam(value = ""  )  @Valid @RequestBody User body
) {
        InlineResponse2001 createdUserResponse = new InlineResponse2001(); // new user created response

        try{
            userService.createUser(body); // saves new user in database

            createdUserResponse.userId(body.getUserId()); // assigns new user ID to response

            return new ResponseEntity<InlineResponse2001>(createdUserResponse, HttpStatus.CREATED);
        }catch (Exception e)
        {
            log.warn("User creation failed");
            System.out.println(e.getMessage());
        }

        return new ResponseEntity<InlineResponse2001>(HttpStatus.NO_CONTENT);

    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<Void> deleteUser(@ApiParam(value = "",required=true) @PathVariable("userid") Integer userid
)   {
        try{
            String accept = request.getHeader("Accept");
            userService.deleteUser(userid); // delete user from database
            return new ResponseEntity<Void>(HttpStatus.OK);
        }catch (Exception e)
        {
            log.warn("User deletion failed"+e.getMessage());
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);

    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE','CUSTOMER')")
    public ResponseEntity<User> editUser(@ApiParam(value = "",required=true) @PathVariable("userid") Integer userId
,@ApiParam(value = ""  )  @Valid @RequestBody User body
)   {
        try{
            return new ResponseEntity<User>(userService.editUser(userId, body), HttpStatus.OK);

        }catch (Exception e)
        {
            log.warn("User editing failed");
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<User>(HttpStatus.NOT_FOUND);

    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE','CUSTOMER')")
    public ResponseEntity<List<AccountObject>> getAccountsByUserId(@ApiParam(value = "the user who ownes these accounts",required=true) @PathVariable("userId") Integer userId
)   {
        try{
            return new ResponseEntity<List<AccountObject>>(userService.getAccountsByUserId(userId), HttpStatus.OK);

        }catch (Exception e)
        {
            log.warn("User getting failed");
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<List<AccountObject>>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<List<InlineResponse200>> getAllUsers(@ApiParam(value = "Limit the number of users to display.", defaultValue = "20") @Valid @RequestParam(value = "limit", required = false, defaultValue="20") Integer limit
,@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset
)   {

        try{
            return new ResponseEntity<List<InlineResponse200>>(userService.getAllUsers(new Filter(limit==null?0:limit, offset==null?0:offset)), HttpStatus.OK);

        }catch (Exception e)
        {
            log.warn("Users getting failed");
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<List<InlineResponse200>>(HttpStatus.NOT_FOUND);
    }

}

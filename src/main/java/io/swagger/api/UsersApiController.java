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
public class UsersApiController implements IUsersApi {

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
    public ResponseEntity<Account> createAccount(@ApiParam(value = "the userId of the user who owns these accounts", required = true) @PathVariable("userId") Integer userId
            , @Valid @RequestParam(value = "accountType", required = true) String accountType
    ) {
        try {
            return new ResponseEntity<Account>(userService.createAccount(userId, accountType), HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            log.warn("Could not create account. " + e.getMessage());
            return new ResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<UserCredentials> createUser(@ApiParam(value = "") @Valid @RequestBody User user
    ) {
        try {
            UserCredentials createdUserResponse = new UserCredentials();
            userService.createUser(user);
            createdUserResponse.userId(user.getUserId().toString());

            return new ResponseEntity<UserCredentials>(createdUserResponse, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            log.warn("Could not create user. " + e.getMessage());
            return new ResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<Void> deleteUser(@ApiParam(value = "", required = true) @PathVariable("userid") Integer userId
    ) {
        try {
            userService.deleteUser(userId); // delete user from database
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            log.warn("Could not delete user. " + e.getMessage());
            return new ResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE','CUSTOMER')")
    public ResponseEntity<User> editUser(@ApiParam(value = "", required = true) @PathVariable("userid") Integer userId
            , @ApiParam(value = "") @Valid @RequestBody User user
    ) {
        try {
            return new ResponseEntity<User>(userService.editUser(userId, user), HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            log.warn("Could not update user. " +  e.getMessage());
            return new ResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE','CUSTOMER')")
    public ResponseEntity<List<Account>> getAccountsByUserId(@ApiParam(value = "the user who ownes these accounts", required = true) @PathVariable("userId") Integer userId
    ) {
        try {
            return new ResponseEntity<List<Account>>(userService.getAccountsByUserId(userId), HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            log.warn("Could not find accounts");
            return new ResponseEntity(new ApiError(HttpStatus.NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<List<UserCredentials>> getAllUsers(@ApiParam(value = "Limit the number of users to display.", defaultValue = "20") @Valid @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit
            , @ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset
    ) {
        try {
            return new ResponseEntity<List<UserCredentials>>(userService.getAllUsers(new Filter(limit == null ? 0 : limit, offset == null ? 0 : offset)), HttpStatus.OK);
        } catch (Exception e) {
            log.warn("Users getting failed");
            return new ResponseEntity<List<UserCredentials>>(HttpStatus.BAD_REQUEST);
        }

    }

}

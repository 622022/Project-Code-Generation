package io.swagger.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import io.swagger.api.interfaces.IUsersApi;
import io.swagger.model.api.JsonResponse;
import io.swagger.model.api.UserCredentials;
import io.swagger.model.content.User;
import io.swagger.utils.Filter;
import io.swagger.service.UserService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
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


@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-05-21T18:10:30.703Z[GMT]")
@Controller
public class UsersApiController implements IUsersApi {

    @Autowired
    private UserService userService;


    private static final Logger logger = (Logger) LoggerFactory.getLogger(UsersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<JsonResponse> createAccount(@ApiParam(value = "the userId of the user who owns these accounts", required = true) @PathVariable("userId") Integer userId
            , @Valid @RequestParam(value = "accountType", required = true) String accountType
    ) {
        try {
            JsonResponse response = new JsonResponse(userService.createAccount(userId, accountType), new JsonResponse.UserMessage("Handled", HttpStatus.CREATED, true));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.warn("UserController:CreateAccount: " + e.getMessage() + e.getStackTrace());
            JsonResponse response = new JsonResponse(null, new JsonResponse.UserMessage(e.getMessage(), HttpStatus.BAD_REQUEST, false));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {

            logger.warn("UserController:CreateAccount: " + e.getMessage() + e.getStackTrace());
            JsonResponse response = new JsonResponse(null, new JsonResponse.UserMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, false));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<JsonResponse> createUser(@ApiParam(value = "") @Valid @RequestBody User user
    ) {
        try {
            UserCredentials createdUserResponse = new UserCredentials();
            userService.createUser(user);
            createdUserResponse.userId(user.getUserId().toString());

            JsonResponse response = new JsonResponse(createdUserResponse, new JsonResponse.UserMessage("Handled", HttpStatus.CREATED, true));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.warn("UserController:CreateUser: " + e.getMessage() + e.getStackTrace());
            JsonResponse response = new JsonResponse(null, new JsonResponse.UserMessage(e.getMessage(), HttpStatus.BAD_REQUEST, false));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.warn("UserController:CreateUser: " + e.getMessage() + e.getStackTrace());
            JsonResponse response = new JsonResponse(null, new JsonResponse.UserMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, false));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<JsonResponse> deleteUser(@ApiParam(value = "", required = true) @PathVariable("userid") Integer userId
    ) {
        try {
            userService.deleteUser(userId); // delete user from database
            JsonResponse response = new JsonResponse(null, new JsonResponse.UserMessage("Handled", HttpStatus.OK, true));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.warn("UserController:DeleteUser: " + e.getMessage() + e.getStackTrace());
            JsonResponse response = new JsonResponse(null, new JsonResponse.UserMessage(e.getMessage(), HttpStatus.BAD_REQUEST, false));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.warn("UserController:DeleteUser: " + e.getMessage() + e.getStackTrace());
            JsonResponse response = new JsonResponse(null, new JsonResponse.UserMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, false));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE','CUSTOMER')")
    public ResponseEntity<JsonResponse> editUser(@ApiParam(value = "", required = true) @PathVariable("userid") Integer userId
            , @ApiParam(value = "") @Valid @RequestBody User user
    ) {
        try {
            JsonResponse response = new JsonResponse(userService.editUser(userId, user), new JsonResponse.UserMessage("Handled", HttpStatus.OK, true));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.warn("UserController:EditUser: " + e.getMessage() + e.getStackTrace());
            JsonResponse response = new JsonResponse(null, new JsonResponse.UserMessage(e.getMessage(), HttpStatus.BAD_REQUEST, false));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.warn("UserController:EditUser: " + e.getMessage() + e.getStackTrace());
            JsonResponse response = new JsonResponse(null, new JsonResponse.UserMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, false));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE','CUSTOMER')")
    public ResponseEntity<JsonResponse> getAccountsByUserId(@ApiParam(value = "the user who ownes these accounts", required = true) @PathVariable("userId") Integer userId

    ) {
        try {
            String token = request.getHeader("Authorization");
            JsonResponse response = new JsonResponse(userService.getAccountsByUserId(token, userId), new JsonResponse.UserMessage("Handled", HttpStatus.OK, true));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.warn("UserController:GetAccountsByUserId: " + e.getMessage() + e.getStackTrace());
            JsonResponse response = new JsonResponse(null, new JsonResponse.UserMessage(e.getMessage(), HttpStatus.BAD_REQUEST, false));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.warn("UserController:GetAccountsByUserId: " + e.getMessage() + e.getStackTrace());
            JsonResponse response = new JsonResponse(null, new JsonResponse.UserMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, false));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<JsonResponse> getAllUsers(@ApiParam(value = "Limit the number of users to display.", defaultValue = "20") @Valid @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit
            , @ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset
    ) {
        try {
            Filter filter = new Filter(limit, offset);
            JsonResponse response = new JsonResponse(userService.getAllUsers(filter), new JsonResponse.UserMessage("Handled", HttpStatus.OK, true));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.warn("UserController:GetAllUsers: " + e.getMessage() + e.getStackTrace());
            JsonResponse response = new JsonResponse(null, new JsonResponse.UserMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, false));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}

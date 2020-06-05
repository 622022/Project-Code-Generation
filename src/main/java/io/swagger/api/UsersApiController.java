package io.swagger.api;

import io.swagger.model.AccountObject;
import io.swagger.filter.Filter;
import io.swagger.model.Body;
import io.swagger.model.InlineResponse200;
import io.swagger.model.InlineResponse2001;
import io.swagger.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import io.swagger.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public ResponseEntity<AccountObject> createAccount(@ApiParam(value = "the userid of the user who owns these accounts",required=true) @PathVariable("userId") Integer userId
,@ApiParam(value = "The account to create."  )  @Valid @RequestBody Body body
) {
//        String accept = request.getHeader("Accept");
//        if (accept != null && accept.contains("application/json")) {
//            try {
//                return new ResponseEntity<AccountObject>(objectMapper.readValue("{\n  \"amount\" : 0,\n  \"dayLimit\" : 5,\n  \"IBAN\" : \"IBAN\",\n  \"absolutelimit\" : 5,\n  \"transactionLimit\" : 1.4658129805029452,\n  \"ownerId\" : 6,\n  \"type\" : \"Checking\",\n  \"status\" : \"Active\"\n}", AccountObject.class), HttpStatus.NOT_IMPLEMENTED);
//            } catch (IOException e) {
//                log.error("Couldn't serialize response for content type application/json", e);
//                return new ResponseEntity<AccountObject>(HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        }
        return new ResponseEntity<AccountObject>(userService.createAccount(userId, body), HttpStatus.OK);
    }

    public ResponseEntity<InlineResponse2001> createUser(@ApiParam(value = ""  )  @Valid @RequestBody User body
) {
//        String accept = request.getHeader("Accept");
//        if (accept != null && accept.contains("application/json")) {
//            try {
//                return new ResponseEntity<InlineResponse2001>(objectMapper.readValue("{\n  \"userId\" : 0\n}", InlineResponse2001.class), HttpStatus.NOT_IMPLEMENTED);
//            } catch (IOException e) {
//                log.error("Couldn't serialize response for content type application/json", e);
//                return new ResponseEntity<InlineResponse2001>(HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        }
        userService.createUser(body); // saves new user in database

        InlineResponse2001 createdUserResponse = new InlineResponse2001(); // new user created response
        createdUserResponse.userId(body.getUserId()); // assigns new user ID to response

        return new ResponseEntity<InlineResponse2001>(createdUserResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<Void> deleteUser(@ApiParam(value = "",required=true) @PathVariable("userid") Integer userid
) {
        String accept = request.getHeader("Accept");
        userService.deleteUser(userid); // delete user from database
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<User> editUser(@ApiParam(value = "",required=true) @PathVariable("userid") Integer userId
,@ApiParam(value = ""  )  @Valid @RequestBody User body
) {
//        String accept = request.getHeader("Accept");
//        if (accept != null && accept.contains("application/json")) {
//            try {
//                return new ResponseEntity<User>(objectMapper.readValue("{\n  \"password\" : \"password\",\n  \"userId\" : 0,\n  \"email\" : \"email\",\n  \"username\" : \"username\"\n}", User.class), HttpStatus.NOT_IMPLEMENTED);
//            } catch (IOException e) {
//                log.error("Couldn't serialize response for content type application/json", e);
//                return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        }
        return new ResponseEntity<User>(userService.editUser(userId, body), HttpStatus.OK);
    }

    public ResponseEntity<List<AccountObject>> getAccountsByUserId(@ApiParam(value = "the user who ownes these accounts",required=true) @PathVariable("userId") Integer userId
) {
//        String accept = request.getHeader("Accept");
//        if (accept != null && accept.contains("application/json")) {
//            try {
//                return new ResponseEntity<List<AccountObject>>(objectMapper.readValue("[ {\n  \"amount\" : 0,\n  \"dayLimit\" : 5,\n  \"IBAN\" : \"IBAN\",\n  \"absolutelimit\" : 5,\n  \"transactionLimit\" : 1.4658129805029452,\n  \"ownerId\" : 6,\n  \"type\" : \"Checking\",\n  \"status\" : \"Active\"\n}, {\n  \"amount\" : 0,\n  \"dayLimit\" : 5,\n  \"IBAN\" : \"IBAN\",\n  \"absolutelimit\" : 5,\n  \"transactionLimit\" : 1.4658129805029452,\n  \"ownerId\" : 6,\n  \"type\" : \"Checking\",\n  \"status\" : \"Active\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
//            } catch (IOException e) {
//                log.error("Couldn't serialize response for content type application/json", e);
//                return new ResponseEntity<List<AccountObject>>(HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        }
        return new ResponseEntity<List<AccountObject>>(userService.getAccountsByUserId(userId), HttpStatus.OK);
    }

    public ResponseEntity<List<InlineResponse200>> getAllUsers(@ApiParam(value = "Limit the number of users to display.", defaultValue = "20") @Valid @RequestParam(value = "limit", required = false, defaultValue="20") Integer limit
,@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset
) {
//        String accept = request.getHeader("Accept");
//        if (accept != null && accept.contains("application/json")) {
//            try {
//                return new ResponseEntity<List<InlineResponse200>>(objectMapper.readValue("[ {\n  \"userid\" : 0\n}, {\n  \"userid\" : 0\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
//            } catch (IOException e) {
//                log.error("Couldn't serialize response for content type application/json", e);
//                return new ResponseEntity<List<InlineResponse200>>(HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        }
        return new ResponseEntity<List<InlineResponse200>>(userService.getAllUsers(new Filter(limit==null?0:limit, offset==null?0:offset)), HttpStatus.OK);
    }

}

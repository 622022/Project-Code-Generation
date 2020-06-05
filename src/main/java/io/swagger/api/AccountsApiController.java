package io.swagger.api;

import io.swagger.filter.Filter;
import io.swagger.model.AccountObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import io.swagger.service.AccountService;
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
import java.util.List;
import java.util.Map;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-05-21T18:10:30.703Z[GMT]")
@Controller
public class AccountsApiController implements AccountsApi {

    @Autowired
    private AccountService accountService;

    private static final Logger log = LoggerFactory.getLogger(AccountsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public AccountsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Void> deleteAccount(@ApiParam(value = "the user who ownes these accounts",required=true) @PathVariable("IBAN") String IBAN
) {
        String accept = request.getHeader("Accept");
        accountService.deleteAccount(IBAN); // delete account
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<AccountObject> editAccount(@ApiParam(value = "" ,required=true )  @Valid @RequestBody AccountObject body
,@ApiParam(value = "the IBAN of the account.",required=true) @PathVariable("IBAN") String IBAN
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
        return new ResponseEntity<AccountObject>(accountService.editAccount(IBAN, body), HttpStatus.OK);
    }

    public ResponseEntity<List<AccountObject>> getAllAccounts(@ApiParam(value = "returns all accounts of the bank with their details.", defaultValue = "20") @Valid @RequestParam(value = "limit", required = false, defaultValue="0") Integer limit
,@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset
,@ApiParam(value = "returns account(s) based on the account's holder name") @Valid @RequestParam(value = "accountOwner", required = false) Integer accountOwner
,@ApiParam(value = "type of the requested accounts.") @Valid @RequestParam(value = "type", required = false) String type
,@ApiParam(value = "type of the requested accounts.") @Valid @RequestParam(value = "status", required = false) String status
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
        Filter filter= new Filter(limit,offset==null?0:offset,accountOwner==null?0:accountOwner,type==null?"":type,status==null?"":status);
        return new ResponseEntity<List<AccountObject>>((List<AccountObject>) accountService.getAllAccounts(filter), HttpStatus.OK); // return all accounts
    }

    public ResponseEntity<AccountObject> getSpecificAccount(@ApiParam(value = "the iban of the requested account.",required=true) @PathVariable("IBAN") String IBAN
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
        return new ResponseEntity<AccountObject>(accountService.getSpecificAccount(IBAN), HttpStatus.OK); // get specific account
    }

}

package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import io.swagger.filter.Filter;
import io.swagger.model.Account;
import io.swagger.service.AccountService;
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
import java.util.logging.Logger;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-05-21T18:10:30.703Z[GMT]")
@Controller
public class AccountsApiController implements IAccountsApi {

    @Autowired
    private AccountService accountService;
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;
    private static final Logger LOGGER = Logger.getLogger(AccountsApiController.class.getName());

    @org.springframework.beans.factory.annotation.Autowired
    public AccountsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<Void> deleteAccount(@ApiParam(value = "the user who ownes these accounts", required = true) @PathVariable("IBAN") String IBAN
    ) {
        try {
            String accept = request.getHeader("Accept");
            accountService.deleteAccount(IBAN); // delete account
            return new ResponseEntity<Void>(HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.warning("Could not delete account" + e.getMessage());
            return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
        }


    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<Account> editAccount(@ApiParam(value = "The account to be updated of specific user", required = true) @Valid @RequestBody Account body
            , @ApiParam(value = "the IBAN of the account.", required = true) @PathVariable("IBAN") String IBAN
    ) {
        try {
            return new ResponseEntity<Account>(accountService.editAccount(IBAN, body), HttpStatus.OK);

        } catch (Exception e) {
            LOGGER.warning("Could not edit account" + e.getMessage());
            return new ResponseEntity<Account>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<List<Account>> getAllAccounts(@ApiParam(value = "returns all accounts of the bank with their details.", defaultValue = "20") @Valid @RequestParam(value = "limit", required = false, defaultValue = "0") Integer limit
            , @ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset
            , @ApiParam(value = "returns account(s) based on the account's holder name") @Valid @RequestParam(value = "accountOwner", required = false) Integer accountOwner
            , @ApiParam(value = "type of the requested accounts.") @Valid @RequestParam(value = "type", required = false) String type
            , @ApiParam(value = "type of the requested accounts.") @Valid @RequestParam(value = "status", required = false) String status
    ) {
        try {
            Filter filter = new Filter(limit, offset == null ? 0 : offset, accountOwner == null ? 0 : accountOwner, type == null ? "" : type, status == null ? "" : status);
            return new ResponseEntity<List<Account>>((List<Account>) accountService.getAllAccounts(filter), HttpStatus.OK); // return all accounts
        } catch (Exception e) {
            LOGGER.warning("Could not get accounts" + e.getMessage());
            //we have to check what exception it is and log the right message
            //e.getClass().getName();
            return new ResponseEntity<List<Account>>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE','CUSTOMER')")
    public ResponseEntity<Account> getSpecificAccount(@ApiParam(value = "the iban of the requested account.", required = true) @PathVariable("IBAN") String IBAN
    ) {
        try {
            return new ResponseEntity<Account>(accountService.getSpecificAccount(IBAN), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.warning("Could not get account" + e.getMessage());
            return new ResponseEntity<Account>(HttpStatus.NOT_FOUND);
        }
    }
}
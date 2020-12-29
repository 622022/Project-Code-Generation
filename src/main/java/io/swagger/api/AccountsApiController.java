package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import io.swagger.filter.Filter;
import io.swagger.model.Account;
import io.swagger.model.JSONResponse;
import io.swagger.model.ApiError;
import io.swagger.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.ArrayList;
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
    public ResponseEntity<Void> deleteAccount(@ApiParam(value = "the user who ownes these accounts", required = true) @PathVariable("IBAN") String iBan
    ) {
        try {
            accountService.deleteAccount(iBan); // delete account
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            LOGGER.warning("Could not delete account. " + e.getMessage());
            return new ResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<Account> editAccount(@ApiParam(value = "The account to be updated of specific user", required = true) @Valid @RequestBody Account account
            , @ApiParam(value = "the IBAN of the account.", required = true) @PathVariable("IBAN") String iBan
    ) {
        try {
            return new ResponseEntity<Account>(accountService.editAccount(iBan, account), HttpStatus.OK);

        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            LOGGER.warning("Could not edit account" + e.getMessage());
            return new ResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public HttpEntity<JSONResponse> getAllAccounts(@ApiParam(value = "returns all accounts of the bank with their details.", defaultValue = "20") @Valid @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit
            , @ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset
            , @ApiParam(value = "returns account(s) based on the account's holder name") @Valid @RequestParam(value = "accountOwner", required = false) Integer accountOwner
            , @ApiParam(value = "type of the requested accounts.") @Valid @RequestParam(value = "type", required = false) String type
            , @ApiParam(value = "type of the requested accounts.") @Valid @RequestParam(value = "status", required = false) String status
    ) {
        try
        {
            Filter filter = new Filter(limit, offset, accountOwner , type, status);
            JSONResponse response = new JSONResponse(accountService.getAllAccounts(filter), new JSONResponse.UserMessage("Hanldled", HttpStatus.OK, true));
            return new ResponseEntity<JSONResponse>((JSONResponse) response, HttpStatus.OK);
        }
        catch(IllegalArgumentException e)
        {
            Iterable<Account> accounts = new ArrayList<>();
            JSONResponse respons = new JSONResponse(accounts, new JSONResponse.UserMessage("Filter params are not filled correctly" ,HttpStatus.NOT_ACCEPTABLE, false));
            return new ResponseEntity<JSONResponse>((JSONResponse) respons, HttpStatus.NOT_ACCEPTABLE);
        }
        catch (Exception e)
        {
            LOGGER.warning("GetAllAccounts: " + e.getMessage());
            Iterable<Account> accounts = new ArrayList<>();
            JSONResponse respons = new JSONResponse(accounts, new JSONResponse.UserMessage("An unexpected error occured" ,HttpStatus.NOT_ACCEPTABLE, false));
            return new ResponseEntity<JSONResponse>((JSONResponse) respons, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE','CUSTOMER')")
    public ResponseEntity<Account> getSpecificAccount(@ApiParam(value = "the iban of the requested account.", required = true) @PathVariable("IBAN") String iBan
    ) {
        try {
            return new ResponseEntity<Account>(accountService.getSpecificAccount(iBan), HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            LOGGER.warning("Could not find account " + e.getMessage());
            return new ResponseEntity(new ApiError(HttpStatus.NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
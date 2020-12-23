package io.swagger.api;

import io.swagger.annotations.*;
import io.swagger.model.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-05-21T18:10:30.703Z[GMT]")
@Api(value = "accounts", description = "the accounts API")
public interface IAccountsApi {

    @ApiOperation(value = "Delete an account.", nickname = "deleteAccount", notes = "delete an account based on IBAN.", authorizations = {
            @Authorization(value = "bearerAuth")}, tags = {"Accounts",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "the account been closed.")})
    @RequestMapping(value = "/accounts/{IBAN}",
            method = RequestMethod.DELETE)
    ResponseEntity<Void> deleteAccount(@ApiParam(value = "the user who ownes these accounts", required = true) @PathVariable("IBAN") String IBAN
    );


    @ApiOperation(value = "Edit an account.", nickname = "editAccount", notes = "Edit an account based on IBAN.", response = Account.class, authorizations = {
            @Authorization(value = "bearerAuth")}, tags = {"Accounts",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "the account has been edited.", response = Account.class)})
    @RequestMapping(value = "/accounts/{IBAN}",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.PUT)
    ResponseEntity<Account> editAccount(@ApiParam(value = "", required = true) @Valid @RequestBody Account body
            , @ApiParam(value = "the IBAN of the account.", required = true) @PathVariable("IBAN") String IBAN
    );


    @ApiOperation(value = "Get accounts with their details", nickname = "getAllAccounts", notes = "Get accounts with their details", response = Account.class, responseContainer = "List", authorizations = {
            @Authorization(value = "bearerAuth")}, tags = {"Accounts",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account details", response = Account.class, responseContainer = "List")})
    @RequestMapping(value = "/accounts",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<Account>> getAllAccounts(@ApiParam(value = "returns all accounts of the bank with their details.", defaultValue = "20") @Valid @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit
            , @ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset
            , @ApiParam(value = "returns account(s) based on the account's holder name") @Valid @RequestParam(value = "accountOwner", required = false) Integer accountOwner
            , @ApiParam(value = "type of the requested accounts.") @Valid @RequestParam(value = "type", required = false) String type
            , @ApiParam(value = "type of the requested accounts.") @Valid @RequestParam(value = "status", required = false) String status
    );


    @ApiOperation(value = "get a specific account using IBAN.", nickname = "getSpecificAccount", notes = "Return account details", response = Account.class, authorizations = {
            @Authorization(value = "bearerAuth")}, tags = {"Accounts",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account details", response = Account.class)})
    @RequestMapping(value = "/accounts/{IBAN}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<Account> getSpecificAccount(@ApiParam(value = "the iban of the requested account.", required = true) @PathVariable("IBAN") String IBAN
    );

}

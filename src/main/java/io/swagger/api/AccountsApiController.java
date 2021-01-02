package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import io.swagger.utils.Filter;
import io.swagger.model.content.Account;
import io.swagger.model.api.JsonResponse;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
    public ResponseEntity<JsonResponse> deleteAccount(@ApiParam(value = "the user who ownes these accounts", required = true) @PathVariable("IBAN") String iBan
    ) {
        try {
            accountService.deleteAccount(iBan);
            JsonResponse response = new JsonResponse(null, new JsonResponse.UserMessage("UnHanldled", HttpStatus.ACCEPTED, true));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.ACCEPTED);
        } catch (IllegalArgumentException e) {
            JsonResponse response = new JsonResponse(null, new JsonResponse.UserMessage("UnHanldled", HttpStatus.BAD_REQUEST, false));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.warning("DeleteAccount: " + e.getMessage());
            JsonResponse response = new JsonResponse(null, new JsonResponse.UserMessage("UnHanldled", HttpStatus.INTERNAL_SERVER_ERROR, false));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<JsonResponse> editAccount(@ApiParam(value = "The account to be updated of specific user", required = true) @Valid @RequestBody Account account
            , @ApiParam(value = "the IBAN of the account.", required = true) @PathVariable("IBAN") String iBan
    ) {
        try {
            JsonResponse response = new JsonResponse(accountService.editAccount(iBan, account), new JsonResponse.UserMessage("Handled", HttpStatus.ACCEPTED, false));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            JsonResponse response = new JsonResponse(null, new JsonResponse.UserMessage("UnHanldled", HttpStatus.BAD_REQUEST, false));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.warning("EditAccount: " + e.getMessage());
            JsonResponse response = new JsonResponse(null, new JsonResponse.UserMessage("UnHanldled", HttpStatus.INTERNAL_SERVER_ERROR, false));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public HttpEntity<JsonResponse> getAllAccounts(@ApiParam(value = "returns all accounts of the bank with their details.", defaultValue = "20") @Valid @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit
            , @ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset
            , @ApiParam(value = "returns account(s) based on the account's holder name") @Valid @RequestParam(value = "accountOwner", required = false) Integer accountOwner
            , @ApiParam(value = "type of the requested accounts.") @Valid @RequestParam(value = "type", required = false) String type
            , @ApiParam(value = "type of the requested accounts.") @Valid @RequestParam(value = "status", required = false) String status
    ) {
        try {
            Filter filter = new Filter(limit, offset, accountOwner, type, status);
            JsonResponse response = new JsonResponse(accountService.getAllAccounts(filter), new JsonResponse.UserMessage("Hanldled", HttpStatus.OK, true));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            JsonResponse respons = new JsonResponse(null, new JsonResponse.UserMessage(e.getMessage(), HttpStatus.NOT_ACCEPTABLE, false));
            return new ResponseEntity<JsonResponse>(respons, HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            LOGGER.warning("GetAllAccounts: " + e.getMessage());
            JsonResponse respons = new JsonResponse(null, new JsonResponse.UserMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, false));
            return new ResponseEntity<JsonResponse>(respons, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE','CUSTOMER')")
    public ResponseEntity<JsonResponse> getSpecificAccount(@ApiParam(value = "the iban of the requested account.", required = true) @PathVariable("IBAN") String iBan
    ) {
        try {
            JsonResponse response = new JsonResponse(accountService.getSpecificAccount(iBan), new JsonResponse.UserMessage("Handled", HttpStatus.OK, true));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            JsonResponse respons = new JsonResponse(null, new JsonResponse.UserMessage(e.getMessage(), HttpStatus.NOT_ACCEPTABLE, false));
            return new ResponseEntity<JsonResponse>(respons, HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            LOGGER.warning("GetSpecificAccount: " + e.getMessage());
            JsonResponse respons = new JsonResponse(null, new JsonResponse.UserMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, false));
            return new ResponseEntity<JsonResponse>(respons, HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
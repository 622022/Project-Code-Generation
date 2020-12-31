package io.swagger.api;

import io.swagger.model.ApiError;
import io.swagger.model.JsonResponse;
import io.swagger.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import io.swagger.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-05-21T18:10:30.703Z[GMT]")
@Controller
public class TransactionsApiController implements ITransactionsApi {

    @Autowired
    TransactionService transactionService;

    private static final Logger log = LoggerFactory.getLogger(TransactionsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE','CUSTOMER')")
    public ResponseEntity createTransaction(@ApiParam(value = "", required = true) @Valid @RequestBody Transaction body
    ) {
        try {
            transactionService.createTransaction(body);
            JsonResponse response = new JsonResponse(null, new JsonResponse.UserMessage("Handled", HttpStatus.CREATED, true));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            JsonResponse respons = new JsonResponse(null, new JsonResponse.UserMessage(e.getMessage(), HttpStatus.BAD_REQUEST, false));
            return new ResponseEntity<JsonResponse>(respons, HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            log.error("Could not create transactions - " + e.getMessage());
            JsonResponse respons = new JsonResponse(null, new JsonResponse.UserMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, false));
            return new ResponseEntity<JsonResponse>(respons, HttpStatus.INTERNAL_SERVER_ERROR);        }
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE','CUSTOMER')")
    public ResponseEntity<JsonResponse> getTransactions(@NotNull @ApiParam(value = "Filter transactions by IBAN.", required = true) @Valid @RequestParam(value = "IBAN", required = true) String IBAN
            , @ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset
            , @ApiParam(value = "returns transaction(s) based on the reciever's name") @Valid @RequestParam(value = "reciever", required = false) String reciever
            , @ApiParam(value = "Limit the number of transactions to display.", defaultValue = "20") @Valid @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit
    ) {

        try {
            //Use the filter here
            List<Transaction> transactions = transactionService.getTransactions(IBAN);
            if (transactions.isEmpty()) {
                JsonResponse respons = new JsonResponse(null, new JsonResponse.UserMessage("No transactions", HttpStatus.NO_CONTENT, false));
                return new ResponseEntity<JsonResponse>(respons, HttpStatus.NO_CONTENT);
            }
            JsonResponse response = new JsonResponse(transactions, new JsonResponse.UserMessage("Handled", HttpStatus.CREATED, true));
            return new ResponseEntity<JsonResponse>(response, HttpStatus.CREATED);

        }
        catch (Exception e) {
            log.error(e.getMessage());
            JsonResponse respons = new JsonResponse(null, new JsonResponse.UserMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, false));
            return new ResponseEntity<JsonResponse>(respons, HttpStatus.INTERNAL_SERVER_ERROR);        }
        }
    }


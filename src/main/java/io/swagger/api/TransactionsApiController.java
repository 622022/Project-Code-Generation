package io.swagger.api;

import io.swagger.model.JsonResponse;
import io.swagger.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import io.swagger.service.TransactionService;
import javassist.NotFoundException;
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
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(TransactionsApiController.class.getName());


    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE','CUSTOMER')")
    public ResponseEntity<JsonResponse> createTransaction(@ApiParam(value = "", required = true) @Valid @RequestBody Transaction body
    ) {
        try {
            Transaction newTransaction = transactionService.createTransaction(body);
            JsonResponse response = new JsonResponse(newTransaction , new JsonResponse.UserMessage("Handled", HttpStatus.OK, true));
            if (newTransaction != null) {
                throw new NotFoundException("Transaction not found");
            }
            return new ResponseEntity<JsonResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            if (e.getClass().getName().toLowerCase().equals("notfoundexception")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            LOGGER.warning("Could not create Transactions");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE','CUSTOMER')")
    public ResponseEntity<List<Transaction>> getTransactions(@NotNull @ApiParam(value = "Filter transactions by IBAN.", required = true) @Valid @RequestParam(value = "IBAN", required = true) String IBAN
            , @ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset
            , @ApiParam(value = "returns transaction(s) based on the reciever's name") @Valid @RequestParam(value = "reciever", required = false) String reciever
            , @ApiParam(value = "Limit the number of transactions to display.", defaultValue = "20") @Valid @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit
    ) {

        try {
            List<Transaction> transactions = transactionService.getTransactions(IBAN);
            if (transactions.isEmpty()) {
                return new ResponseEntity<List<Transaction>>(transactions, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<List<Transaction>>(transactions, HttpStatus.OK);
            }

        } catch (Exception e) {
            LOGGER.warning(e.getMessage());
            return new ResponseEntity<List<Transaction>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}

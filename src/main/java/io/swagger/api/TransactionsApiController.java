package io.swagger.api;

import io.swagger.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import io.swagger.service.AccountService;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class TransactionsApiController implements TransactionsApi {

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
    public ResponseEntity<Transaction> createTransaction(@ApiParam(value = "" ,required=true )  @Valid @RequestBody Transaction body
)   {
        try{
            Transaction newTransaction = transactionService.createTransaction(body);

            if(newTransaction != null)
            {
                return new ResponseEntity<Transaction>(newTransaction, HttpStatus.OK);
            }
        }catch (Exception e)
        {
            LOGGER.warning("Could not create Transactions");
            System.out.println(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE','CUSTOMER')")
    public ResponseEntity<List<Transaction>> getTransactions(@NotNull @ApiParam(value = "Filter transactions by IBAN.", required = true) @Valid @RequestParam(value = "IBAN", required = true) String IBAN
,@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset
,@ApiParam(value = "returns transaction(s) based on the reciever's name") @Valid @RequestParam(value = "reciever", required = false) String reciever
,@ApiParam(value = "Limit the number of transactions to display.", defaultValue = "20") @Valid @RequestParam(value = "limit", required = false, defaultValue="20") Integer limit
) {

        try{
            return new ResponseEntity<List<Transaction>>(transactionService.getTransactions(IBAN), HttpStatus.OK);

        }catch (Exception e)
        {
            LOGGER.warning("Could not get Transactions");
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<List<Transaction>>(HttpStatus.NOT_FOUND);
    }

}

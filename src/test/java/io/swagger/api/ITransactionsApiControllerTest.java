package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.content.Role;
import io.swagger.model.content.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ITransactionsApiControllerTest {

    @Autowired
    private MockMvc mvc;

    private String employeeToken;
    private ObjectMapper mapper = new ObjectMapper();
    private Token token;
    private MockedUser loginMockedUser;
    private Transaction transaction;
    private String senderIban;
    private String receiverIban;


    @BeforeEach
    public void getFreshTokenAndIbanNumbers() throws Exception {
        token = new Token(mvc, mapper);
        employeeToken = token.getTokenFromSpecificUser("username_1", "password_1");

        // getting the accounts of specific user to test the transaction on them.
        MvcResult accountResults = this.mvc
                .perform(get("/users/{userid}/accounts", "2")
                        .header("Authorization", employeeToken))
                .andReturn();
        String userAccounts = accountResults.getResponse().getContentAsString();
        try {
            String[] dividedAccountResponse = userAccounts.split("iban\":\"");
            senderIban = dividedAccountResponse[1].split("\",\"amount")[0];
            receiverIban = dividedAccountResponse[2].split("\",\"amount")[0];
        } catch (Exception ex) {
            System.out.println(String.format("Something went wrong reading the account object: %s", ex.getMessage()));
        }


    }


    @Test
    public void gettingTransactionsByIbanForAccountsWithNoTransactionsReturn204Status() throws Exception {
        this.mvc
                .perform(get("/transactions?IBAN={iban}", senderIban)
                        .header("Authorization", employeeToken))
                .andExpect(status().isOk());
    }


    @Test
    public void creatingTransactionReturns201Response() throws Exception {
        transaction = new Transaction(senderIban, receiverIban, "username_2", 10.0, Role.EMPLOYEE);
        this.mvc
                .perform(post("/transactions")
                        .header("Authorization", employeeToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.mapper.writeValueAsString(transaction)))
                .andExpect(status().isCreated());
    }


    @Test
    public void gettingTransactionsByIbanForAccountWithTransactionHistoryReturn201Status() throws Exception {
        this.mvc
                .perform(get("/transactions?IBAN={iban}", senderIban)
                        .header("Authorization", employeeToken))
                .andExpect(status().isOk());
    }
}
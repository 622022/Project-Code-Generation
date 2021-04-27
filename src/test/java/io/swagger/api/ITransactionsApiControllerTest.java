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

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ITransactionsApiControllerTest {

    @Autowired
    private MockMvc mvc;

    private String employeeToken;
    private String customer1Token;
    private ObjectMapper mapper = new ObjectMapper();
    private Token token;
    private Transaction transaction;
    private String customer1SavingAccount;
    private String customer1CheckingAccount;
    private String customer2SavingAccount;
    private String customer2CheckingAccount;



    @BeforeEach
    public void getFreshTokenAndIbanNumbers() throws Exception {
        token = new Token(mvc, mapper);
        employeeToken = token.getTokenFromSpecificUser("username_1", "password_1");
        customer1Token = token.getTokenFromSpecificUser("username_9","password_9");
        // getting the accounts of specific user to test the transaction on them.
        ArrayList<String> customer1Accounts = getSavingAndCheckingAccountsOfSpecificUser(8);
        customer1SavingAccount = customer1Accounts.get(0);
        customer1CheckingAccount = customer1Accounts.get(1);
        ArrayList<String> customer2Accounts = getSavingAndCheckingAccountsOfSpecificUser(7);
        customer2SavingAccount = customer2Accounts.get(0);
        customer2CheckingAccount = customer2Accounts.get(1);
    }


    @Test
    public void gettingTransactionsByIbanForAccountsWithNoTransactionsReturn200Status() throws Exception {
        this.mvc
                .perform(get("/transactions?IBAN={iban}", customer1SavingAccount)
                        .header("Authorization", employeeToken))
                .andExpect(status().isOk());
    }


    @Test
    public void creatingTransactionFromSavingToCheckingForSameUserReturns201Response() throws Exception {
        transaction = new Transaction(customer1SavingAccount, customer1CheckingAccount, "username_8", 10.0, Role.EMPLOYEE);
        this.mvc
                .perform(post("/transactions")
                        .header("Authorization", employeeToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.mapper.writeValueAsString(transaction)))
                .andExpect(status().isCreated());
    }

    @Test
    public void creatingTransactionFromSavingToCheckingForDifferentUserReturns400Response() throws Exception {
        transaction = new Transaction(customer1SavingAccount, customer2CheckingAccount, "username_7", 10.0, Role.EMPLOYEE);
        this.mvc
                .perform(post("/transactions")
                        .header("Authorization", employeeToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.mapper.writeValueAsString(transaction)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void creatingTransactionFromCheckingToCheckingFromDifferentUserAccountReturns401Response() throws Exception {
        transaction = new Transaction(customer2CheckingAccount, customer1CheckingAccount, "username_8", 10.0, Role.CUSTOMER);
        this.mvc
                .perform(post("/transactions")
                        .header("Authorization", customer1Token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.mapper.writeValueAsString(transaction)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void gettingTransactionsByIbanForAccountWithTransactionHistoryReturn200Status() throws Exception {
        this.mvc
                .perform(get("/transactions?IBAN={iban}", customer1SavingAccount)
                        .header("Authorization", employeeToken))
                .andExpect(status().isOk());
    }

    @Test
    public void gettingTransactionsByIbanForAccountOfDifferentUserReturn401Status() throws Exception {
        this.mvc
                .perform(get("/transactions?IBAN={iban}", customer2SavingAccount)
                        .header("Authorization", customer1Token))
                .andExpect(status().isUnauthorized());
    }

    private ArrayList<String> getSavingAndCheckingAccountsOfSpecificUser(int userId) throws Exception{
        ArrayList<String> accountsList = new ArrayList<>();
        MvcResult accountResults = this.mvc
                .perform(get("/users/{userid}/accounts", userId)
                        .header("Authorization", employeeToken))
                .andReturn();
        String userAccounts = accountResults.getResponse().getContentAsString();
        try {
            String[] dividedAccountResponse = userAccounts.split("iban\":\"");
            String savingAccount = dividedAccountResponse[1].split("\",\"amount")[0];
            String checkingAccount = dividedAccountResponse[2].split("\",\"amount")[0];
            accountsList.add(savingAccount);
            accountsList.add(checkingAccount);
        } catch (Exception ex) {
            System.out.println(String.format("Something went wrong reading the account object: %s", ex.getMessage()));
        }
        return accountsList;
    }
}
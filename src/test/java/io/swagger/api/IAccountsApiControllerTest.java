package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class IAccountsApiControllerTest {
    @Autowired
    private MockMvc mvc;
    private ObjectMapper mapper = new ObjectMapper();
    private String employeeToken;
    private String customerToken;
    private Token utility;
    private Account updatedAccount;
    private String specificAccountIban;


    @BeforeEach
    public void getFreshToken() throws Exception {
        utility = new Token(mvc, mapper);
        customerToken = utility.getTokenFromSpecificUser("username_9", "password_9");
        employeeToken = utility.getTokenFromSpecificUser("username_1", "password_1");

        //get a valid existing account is required for some tests
        MvcResult oneAccountResult =
                this.mvc
                        .perform(get("/accounts")
                                .header("Authorization", employeeToken)
                                .param("limit", "1"))
                        .andReturn();
        String ibanResponseContent = oneAccountResult.getResponse().getContentAsString();
        try {
            String[] dividedAccountResponse = ibanResponseContent.split(",");
            specificAccountIban = dividedAccountResponse[0].split(":\"")[1].substring(0, dividedAccountResponse[0].split(":\"")[1].length() - 1);
            System.out.println(specificAccountIban);
        } catch (Exception ex) {
            System.out.println(String.format("Something went wrong reading the account object: %s", ex.getMessage()));
        }


    }

    @Test
    public void getAllAccountsAsEmployeeShouldReturn200Response() throws Exception {
        this.mvc
                .perform(get("/accounts")
                        .header("Authorization", employeeToken)
                )
                .andExpect(status().isOk());

    }

    @Test
    public void getAllAccountsAsCustomerShouldReturn403ForbiddenStatus() throws Exception {
        this.mvc
                .perform(get("/accounts")
                        .header("Authorization", customerToken)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    public void gettingNonExistingAccountReturns406Response() throws Exception {
        this.mvc
                .perform(get("/accounts/NL12ING01234")
                        .header("Authorization", employeeToken))
                .andExpect(status().isNotAcceptable());

    }

    @Test
    public void gettingExistingAccountReturns200Response() throws Exception {
        this.mvc
                .perform(get("/accounts/{Iban}", specificAccountIban)
                        .header("Authorization", employeeToken))
                .andExpect(status().isOk());

    }

    @Test
    public void editAccountOfSpecificIbanReturns200Response() throws Exception {
        updatedAccount = new Account(200, 1, Account.TypeEnum.SAVING, Account.StatusEnum.ACTIVE,
                10.10, 50, 80);

        this.mvc
                .perform(put("/accounts/{Iban}", specificAccountIban)
                        .header("Authorization", employeeToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.mapper.writeValueAsString(updatedAccount)))
                .andExpect(status().isOk());

    }

    @Test
    public void deleteAccountReturns202Response() throws Exception {
        this.mvc
                .perform(delete("/accounts/{IBAN}", specificAccountIban)
                        .header("Authorization", employeeToken)
                )
                .andExpect((status().isAccepted()));
    }


}